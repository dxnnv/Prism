package dev.dxnny.prism.manager

import dev.dxnny.infrastructure.utils.text.MessageUtils.mmParse
import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.objects.GradientData

object GradientManager {
    val config = instance.configuration
    var gradientMap: MutableMap<String, GradientData> = mutableMapOf()
    val defaultLoreAvailable = config.getStringList("gui.gradient.lore-available")
    val defaultLoreLocked = config.getStringList("gui.gradient.lore-locked")

    fun loadGradientItems() {
        val gradientSection = config.getConfigurationSection("gradients")!!

        gradientMap.clear()
        gradientSection.getKeys(false).forEach { key ->
            val gradientColor = gradientSection.getString("$key.gradient")!!
            val gradientName = gradientSection.getString("$key.name") ?: "Unnamed Gradient"
            gradientMap.put(
                key, GradientData(
                    identifier = key,
                    name = gradientName,
                    gradientColor = gradientColor,
                    gradientComponent = mmParse("$gradientColor$gradientName<reset>"),
                    permission = "prism.gradient.$key",
                    hideFromGui = gradientSection.getBoolean("$key.remove-from-gui", false),
                    loreAvailable = gradientSection.getStringList("$key.lore-available").takeIf { it.isNotEmpty() }
                        ?: defaultLoreAvailable,
                    loreLocked = gradientSection.getStringList("$key.lore-locked").takeIf { it.isNotEmpty() }
                        ?: defaultLoreLocked
                )
            )
        }
    }
}