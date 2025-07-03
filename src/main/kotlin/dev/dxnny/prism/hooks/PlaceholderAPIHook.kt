package dev.dxnny.prism.hooks

import dev.dxnny.infrastructure.utils.text.ColorUtils.miniToLegacy
import dev.dxnny.infrastructure.utils.text.MessageUtils.serialize
import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.manager.StorageManager.Companion.playerGradients
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class PlaceholderAPIHook : PlaceholderExpansion() {
    override fun getIdentifier() = "prism"
    override fun getAuthor() = "dxnnv"
    override fun getVersion() = instance.version
    override fun persist() = true
    override fun canRegister() = true

    override fun onPlaceholderRequest(player: Player?, params: String): String {
        val uuid = player?.uniqueId ?: return "Invalid Player"
        val gradient = playerGradients[uuid]
        var gradientColor = gradient?.gradientColor

        val ign = player.name().serialize()
        val displayName = player.displayName().serialize()
        val customName = instance.configuration.getString("options.alt-placeholder.placeholder")?.let { PlaceholderAPI.setPlaceholders(player, it) }

        return when (params) {
            "gradient_color" -> gradientColor ?: "No Gradient"
            "gradient_id" -> gradient?.identifier ?: "No Gradient"
            "name" -> gradientName(ign, gradientColor)
            "displayname" -> gradientName(displayName, gradientColor)
            "customname" -> gradientName(customName ?: return "Invalid Custom Name", gradientColor)
            else -> "Invalid Placeholder"
        }
    }

    private fun gradientName(name: String, gradientColor: String?) = miniToLegacy("${gradientColor ?: ""}$name<reset>")
}