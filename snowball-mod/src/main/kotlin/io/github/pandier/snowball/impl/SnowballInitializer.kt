package io.github.pandier.snowball.impl

import io.github.pandier.snowball.Snowball
import io.github.pandier.snowball.impl.fabric.FabricListeners
import net.fabricmc.api.ModInitializer

class SnowballInitializer : ModInitializer {

    override fun onInitialize() {
        injectInstance()
        FabricListeners.register()
        SnowballImpl.pluginManager.initialize()
    }

    @Suppress("JAVA_CLASS_ON_COMPANION")
    private fun injectInstance() {
        try {
            val instanceField = Snowball.Holder::class.java.getDeclaredField("instance")
            instanceField.isAccessible = true
            instanceField.set(Snowball.Holder, SnowballImpl)
        } catch (ex: Exception) {
            throw IllegalStateException("Failed to inject Snowball instance into holder", ex)
        }
    }
}