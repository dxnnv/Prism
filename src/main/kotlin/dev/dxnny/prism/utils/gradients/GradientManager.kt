package dev.dxnny.prism.utils.gradients

import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.utils.text.MessageUtils.mmParse
import net.kyori.adventure.text.Component
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

object GradientManager {
    @Suppress("unused")
    fun getGradientString(gradientId: String): String? {
        val gradient: ConfigurationSection? = instance.config.getConfigurationSection("gradients.$gradientId")
        return gradient?.getString("gradient")
    }

    fun hasGradientPermission(player: Player, gradientId: String): Boolean {
        return player.hasPermission("prism.gradient.$gradientId")
    }

    fun gradientExists(id: String): Boolean {
        return instance.config.isConfigurationSection("gradients.$id")
    }

    fun getGradientComponent(id: String): Component {
        val gradientConfig = instance.config.getConfigurationSection("gradients.$id")
        val gradientColor = gradientConfig!!.getString("gradient")!!
        val gradientName = gradientConfig.getString("name")

        val fullGradient = mmParse("$gradientColor$gradientName<reset>")
        return fullGradient
    }
}