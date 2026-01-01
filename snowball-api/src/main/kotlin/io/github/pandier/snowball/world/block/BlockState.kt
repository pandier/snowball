package io.github.pandier.snowball.world.block

import java.util.function.Supplier

public interface BlockState {
    public companion object {
        @JvmStatic
        public fun of(type: Supplier<BlockType>): BlockState {
            return of(type.get())
        }

        @JvmStatic
        public fun of(type: BlockType): BlockState {
            return type.defaultBlockState
        }
    }

    public val type: BlockType
}
