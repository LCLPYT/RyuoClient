package work.lclpnet.ryuo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.lclpnet.ryuo.Ryuo;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class RyuoCache {

    /** Cache entry max age, in seconds */
    public static final int MAX_AGE = 60 * 60 * 24 * 30;
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(RyuoCacheIndex.Entry.class, RyuoCacheIndex.Entry.CODEC)
            .create();
    private static final HttpClient client = HttpClient.newBuilder()
            .executor(Util.getIoWorkerExecutor())
            .build();
    private static final ReentrantReadWriteLock indexLock = new ReentrantReadWriteLock();
    private static final Map<String, CompletableFuture<Path>> fetching = new HashMap<>();

    private static Path cacheDir = null, indexPath;
    private static RyuoCacheIndex index = null;

    public static CompletableFuture<Void> initCache() {
        final var basedir = FabricLoader.getInstance().getGameDir().resolve(".cache");

        return CompletableFuture.runAsync(() -> {
            try {
                Files.setAttribute(basedir, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
            } catch (IOException e) {
                throw new IllegalStateException("Could not set hidden file attribute", e);
            }
        }, Util.getIoWorkerExecutor()).thenCompose(unused -> initCache(basedir.resolve(Ryuo.MOD_ID)));
    }

    public static CompletableFuture<Void> initCache(Path cacheDir) {
        RyuoCache.cacheDir = cacheDir;
        RyuoCache.indexPath = RyuoCache.cacheDir.resolve("index.json.gz");

        return CompletableFuture.runAsync(() -> {
            // create cache directory
            if (!Files.exists(cacheDir)) {
                try {
                    Files.createDirectories(cacheDir);
                } catch (IOException e) {
                    throw new IllegalStateException("Could not create cache directory", e);
                }
            }

            // read or create cache index
            if (!Files.exists(indexPath)) {
                // clear old cache entries on disk, if there are any.
                try {
                    clearCache().get();
                } catch (InterruptedException e) {
                    logger.error("Cache clear was interrupted", e);
                    return;
                } catch (ExecutionException e) {
                    logger.warn("Could not delete cache. Old cache entries may remain...", e.getCause());
                    return;
                }

                // write new cache index
                index = new RyuoCacheIndex();

                try {
                    writeIndex();
                } catch (IOException e) {
                    throw new IllegalStateException("Could not write cache index", e);
                }
            } else {
                // load cache index from disk
                try {
                    index = readIndex();
                } catch (IOException e) {
                    throw new IllegalStateException("Could not read cache index", e);
                }
            }
        }, Util.getIoWorkerExecutor());
    }

    public static CompletableFuture<Void> clearCache() {
        return clearCache(cacheDir);
    }

    public static CompletableFuture<Void> clearCache(final Path cacheDir) {
        Objects.requireNonNull(cacheDir, "Cache directory might not be null");

        return CompletableFuture.runAsync(() -> {
            try {
                // empty cache directory
                if (Files.exists(cacheDir)) {
                    try (var tree = Files.walk(cacheDir)) {
                        tree.sorted(Comparator.reverseOrder())
                                .map(Path::toFile)
                                .forEach(file -> {
                                    if (!file.delete() && !file.equals(cacheDir.toFile())) {
                                        logger.warn("Could not delete {}", file.getAbsolutePath());
                                    }
                                });
                    }
                }

                // clear index in memory
                index = new RyuoCacheIndex();

                Files.createDirectories(cacheDir);
            } catch (IOException e) {
                throw new IllegalStateException("Could not delete cache", e);
            }
        }, Util.getIoWorkerExecutor());
    }

    private static RyuoCacheIndex readIndex() throws IOException {
        indexLock.readLock().lock();

        try (var in = new GZIPInputStream(new FileInputStream(indexPath.toFile()));
             var reader = new JsonReader(new InputStreamReader(in))) {
            return gson.fromJson(reader, RyuoCacheIndex.class);
        } finally {
            indexLock.readLock().unlock();
        }
    }

    private static void writeIndex() throws IOException {
        if (index.size() <= 0) return;

        indexLock.writeLock().lock();

        try (var out = new GZIPOutputStream(new FileOutputStream(indexPath.toFile()));
             var writer = new JsonWriter(new OutputStreamWriter(out))) {
            gson.toJson(index, index.getClass(), writer);
        } finally {
            indexLock.writeLock().unlock();
        }
    }

    private static CompletableFuture<Void> writeIndexAsync() {
        return CompletableFuture.runAsync(() -> {
            try {
                writeIndex();
            } catch (IOException e) {
                throw new IllegalStateException("Could not write index", e);
            }
        }, Util.getIoWorkerExecutor());
    }

    /**
     * Download content from a URI. Uses cache if possible.
     * @param uri The URI to request.
     * @return A CompletableFuture with the downloaded file path.
     */
    public static CompletableFuture<Path> request(URI uri) {
        return getOrFetch(uri, MAX_AGE, path -> {
            final var request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(10L))
                    .GET()
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofFile(path))
                    .thenApply(HttpResponse::body);
        });
    }

    /**
     * Gets cached content, not older than <code>maxAge</code>. If there is no cache entry, fetch it.
     * @param uri The URI to request.
     * @param maxAge The max cache entry age, in seconds. Entries older than <code>maxAge</code> seconds will be ignored.
     * @param fetcher A function which fetches the content and saves it to the supplied path.
     * @return A CompletableFuture with the content file path.
     */
    public static CompletableFuture<Path> getOrFetch(URI uri, long maxAge, Function<Path, CompletableFuture<Path>> fetcher) {
        if (index == null) throw new IllegalStateException("Cache not initialized");

        final var uriStr = uri.toString();

        // check if there is already a pending fetch task
        var pending = RyuoCache.fetching.get(uriStr);
        if (pending != null) {
            return pending;
        }

        var entry = index.get(uri);
        if (entry != null && entry.isValid(maxAge)) {
            return getPathCreateParent(entry.id());
        }

        final var id = UUID.randomUUID();

        final var future = getPathCreateParent(id).thenCompose(fetcher).whenComplete((path, throwable) -> {
            if (throwable != null) {
                logger.error("Could not fetch content", throwable);
            } else if (path == null) {
                logger.error("Could not fetch content", new NullPointerException("Path is null"));
            } else {
                // create cache entry
                index.create(uri, id);

                writeIndexAsync().whenComplete(((unused, throwable1) -> {
                    if (throwable1 != null) logger.error("Could not write cache index", throwable1);
                }));
            }
        });

        // register fetching future
        fetching.put(uriStr, future);

        // remove fetching future, when completed
        future.thenRun(() -> fetching.remove(uriStr));

        return future;
    }

    private static Path getPath(UUID id) {
        final var uid = id.toString().replaceAll("-", "");
        return cacheDir.resolve(uid.substring(0, 2)).resolve(uid);
    }

    private static CompletableFuture<Path> getPathCreateParent(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            var path = getPath(id);
            var dir = path.getParent();

            if (!Files.exists(dir)) {
                try {
                    Files.createDirectories(dir);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }

            return path;
        }, Util.getIoWorkerExecutor());
    }
}
