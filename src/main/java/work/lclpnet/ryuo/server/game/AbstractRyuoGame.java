package work.lclpnet.ryuo.server.game;

import net.minecraft.util.Identifier;
import work.lclpnet.ryuo.module.RyuoModule;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractRyuoGame implements RyuoGame {

    protected final Identifier id;
    protected final String name;
    protected final Set<RyuoModule> modules;

    public AbstractRyuoGame(Identifier id, String name, Set<RyuoModule> modules) {
        this.id = id;
        this.name = name;
        this.modules = Collections.unmodifiableSet(modules);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<RyuoModule> getModules() {
        return modules;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
