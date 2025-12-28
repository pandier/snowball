package io.github.pandier.snowball.world

public enum class HeightMap {

    /**
     * The highest non-air block.
     */
    WORLD_SURFACE,

    /**
     * The highest non-air block. Used for world generation.
     */
    WORLD_SURFACE_WG,

    /**
     * The highest solid block.
     */
    OCEAN_FLOOR,

    /**
     * The highest solid block. Used for world generation.
     */
    OCEAN_FLOOR_WG,

    /**
     * The highest block that is a solid or contains a fluid.
     */
    MOTION_BLOCKING,

    /**
     * The highest block that is a solid or contains a fluid, excluding leaves.
     */
    MOTION_BLOCKING_NO_LEAVES,
}
