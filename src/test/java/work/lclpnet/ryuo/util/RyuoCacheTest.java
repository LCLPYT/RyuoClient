package work.lclpnet.ryuo.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.SAME_THREAD)
class RyuoCacheTest {

    private static final Path CACHE_DIR = Path.of(".cache", "ryuo_test");;

    @Test
    void requestUrl() throws ExecutionException, InterruptedException, IOException {
        RyuoCache.initCache(CACHE_DIR).get();
        RyuoCache.clearCache().get();

        try (var list = Files.list(CACHE_DIR)) {
            assertEquals(0, list.count());
        }

        var path = RyuoCache.request(URI.create("https://lclpnet.work/dl/lobby_1.18.2")).get();

        System.out.println(path);

        assertTrue(Files.exists(path));
    }

    @Test
    void requestUrlHit() throws ExecutionException, InterruptedException, IOException {
        RyuoCache.initCache(CACHE_DIR).get();
        RyuoCache.clearCache().get();

        try (var list = Files.list(CACHE_DIR)) {
            assertEquals(0, list.count());
        }

        var path = RyuoCache.request(URI.create("https://lclpnet.work/dl/lobby_1.18.2")).get();
        var path2 = RyuoCache.request(URI.create("https://lclpnet.work/dl/lobby_1.18.2")).get();

        System.out.println(path);

        assertEquals(path, path2);
        assertTrue(Files.exists(path));
    }

    @Test
    void requestUrlSimultaneously() throws ExecutionException, InterruptedException, IOException {
        RyuoCache.initCache(CACHE_DIR).get();
        RyuoCache.clearCache().get();

        try (var list = Files.list(CACHE_DIR)) {
            assertEquals(0, list.count());
        }

        var fut1 = RyuoCache.request(URI.create("https://lclpnet.work/dl/lobby_1.18.2"));
        var fut2 = RyuoCache.request(URI.create("https://lclpnet.work/dl/lobby_1.18.2"));

        CompletableFuture.allOf(fut1, fut2).get();

        var path = fut1.get();
        var path2 = fut2.get();

        System.out.println(path);

        assertEquals(path, path2);
        assertTrue(Files.exists(path));
    }
}