package io.github.pandier.snowball.item.component

public data class FireworksComponent(
    public val duration: Int = 1,
    public val explosions: List<FireworkExplosion> = listOf(),
) {
    public companion object {
        @JvmField
        public val DEFAULT: FireworksComponent = FireworksComponent()

        @JvmStatic
        public fun of(duration: Int, explosions: List<FireworkExplosion>): FireworksComponent =
            FireworksComponent(duration, explosions)
    }
}
