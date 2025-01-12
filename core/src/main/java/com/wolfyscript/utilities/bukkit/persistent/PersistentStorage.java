package com.wolfyscript.utilities.bukkit.persistent;

import com.google.common.base.Preconditions;
import com.wolfyscript.utilities.bukkit.WolfyCoreBukkit;
import com.wolfyscript.utilities.bukkit.persistent.world.WorldStorage;
import me.wolfyscript.utilities.api.WolfyUtilCore;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class PersistentStorage {

    private final Map<UUID, WorldStorage> WORLD_STORAGE = new HashMap<>();
    private final WolfyUtilCore core;

    public PersistentStorage(WolfyUtilCore core) {
        this.core = core;
    }

    public WorldStorage getOrCreateWorldStorage(@NotNull World world) {
        Preconditions.checkNotNull(world, "The world cannot be null!");
        return WORLD_STORAGE.computeIfAbsent(world.getUID(), uuid -> new WorldStorage(core, uuid));
    }

    public WolfyUtilCore getCore() {
        return core;
    }
}
