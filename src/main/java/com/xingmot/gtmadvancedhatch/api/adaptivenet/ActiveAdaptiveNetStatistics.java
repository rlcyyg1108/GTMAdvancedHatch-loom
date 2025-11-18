package com.xingmot.gtmadvancedhatch.api.adaptivenet;

import com.xingmot.gtmadvancedhatch.common.data.TagConstants;

import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import static com.xingmot.gtmadvancedhatch.api.adaptivenet.AdaptiveConstants.NET_TYPE_EMPTY;

import joptsimple.internal.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// 活跃网络数量统计

/**
 * 本统计类不对主端负责，仅在从端加载和卸载时进行同步
 * 1. 从统计中添加/移除当前适配仓
 * 2. 获取统计数据
 */
public class ActiveAdaptiveNetStatistics {

    public static final HashMap<StatisticsKey, Set<Vec3>> activeAdaptiveNetSet = new HashMap<>();

    private ActiveAdaptiveNetStatistics() {}

    public static synchronized boolean existOrCleared(@Nonnull String type, long frequency, @Nullable UUID uuid, String special) {
        boolean exists = NetMasterRegistry.hasRegistered(type, frequency, uuid);
        if (!exists) activeAdaptiveNetSet.remove(new StatisticsKey(type, frequency, uuid, special));
        return exists;
    }

    public static synchronized boolean increment(@Nonnull String type, long frequency, @Nullable UUID uuid, String special, Vec3i location) {
        if (frequency == 0) return false;
        if (existOrCleared(type, frequency, uuid, special)) {
            StatisticsKey statisticsKey = new StatisticsKey(type, frequency, uuid, special);
            if (activeAdaptiveNetSet.containsKey(statisticsKey) && activeAdaptiveNetSet.get(statisticsKey) instanceof Set<Vec3> set) {
                set.add(Vec3.atLowerCornerOf(location));
            } else {
                HashSet<Vec3> set = new HashSet<>();
                set.add(Vec3.atLowerCornerOf(location));
                activeAdaptiveNetSet.put(statisticsKey, set);
            }
            return true;
        }
        return false;
    }

    public static synchronized boolean decrement(@Nonnull String type, long frequency, @Nullable UUID uuid, String special, Vec3i location) {
        if (frequency == 0) return false;
        if (existOrCleared(type, frequency, uuid, special)) {
            StatisticsKey statisticsKey = new StatisticsKey(type, frequency, uuid, special);
            return activeAdaptiveNetSet.containsKey(statisticsKey) && activeAdaptiveNetSet.get(statisticsKey) instanceof HashSet<Vec3> set && set.remove(Vec3.atLowerCornerOf(location));
        }
        return false;
    }

    public static synchronized int getNum(@Nonnull String type, long frequency, @Nullable UUID uuid, String special) {
        if (frequency == 0) return 0;
        if (existOrCleared(type, frequency, uuid, special)) {
            StatisticsKey statisticsKey = new StatisticsKey(type, frequency, uuid, special);
            if (activeAdaptiveNetSet.containsKey(statisticsKey) && activeAdaptiveNetSet.get(statisticsKey) instanceof HashSet<Vec3> set) {
                return set.size();
            }
        }
        return 0;
    }

    public record StatisticsKey(@Nonnull String type, long freq, UUID owner, String special) {

        @Override
        public @NotNull String toString() {
            return "$" + (Strings.isNullOrEmpty(type) ? NET_TYPE_EMPTY : type) + "#" + freq + "@" + owner + "*" + special;
        }

        public CompoundTag toTag() {
            final CompoundTag tag = new CompoundTag();
            tag.putString("type", type);
            tag.putLong(TagConstants.ADAPTIVE_NET_FREQUENCY, freq);
            tag.putUUID(TagConstants.ADAPTIVE_NET_UUID, owner);
            tag.putString("special", special);
            return tag;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (object instanceof StatisticsKey(String type1, long freq1, UUID owner1, String s)) {
                return type1.equals(this.type) && freq1 == this.freq && owner1.equals(this.owner) && s.equals(this.special);
            } else {
                return false;
            }
        }
    }
}
