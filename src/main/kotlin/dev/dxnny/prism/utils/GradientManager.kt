package dev.dxnny.prism.utils

import dev.dxnny.infrastructure.utils.text.MessageUtils.mmParse
import dev.dxnny.prism.Prism.Companion.instance
import net.kyori.adventure.text.Component
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

object GradientManager {
    private val config: FileConfiguration
        get() = instance.configuration
    
    @Suppress("unused")
    fun getGradientString(gradientId: String): String? {
        val gradient: ConfigurationSection? = config.getConfigurationSection("gradients.$gradientId")
        return gradient?.getString("gradient")
    }

    fun hasGradientPermission(player: Player, gradientId: String): Boolean {
        return player.hasPermission("prism.gradient.$gradientId")
    }

    fun gradientExists(id: String): Boolean {
        return config.isConfigurationSection("gradients.$id")
    }

    fun getGradientComponent(id: String): Component {
        val gradientConfig = config.getConfigurationSection("gradients.$id")
            ?: throw IllegalArgumentException("Gradient ID $id does not exist in config")
        val gradientColor = gradientConfig.getString("gradient")
            ?: throw IllegalArgumentException("Gradient color not found for ID $id")
        val gradientName = gradientConfig.getString("name") ?: "Unnamed Gradient"

        val fullGradient = mmParse("$gradientColor$gradientName<reset>")
        return fullGradient
    }
}