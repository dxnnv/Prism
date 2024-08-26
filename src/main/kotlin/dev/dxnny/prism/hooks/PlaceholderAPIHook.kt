package dev.dxnny.prism.hooks

import dev.dxnny.infrastructure.utils.text.ColorUtils.miniToLegacy
import dev.dxnny.prism.Prism.Companion.instance
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class PlaceholderAPIHook : PlaceholderExpansion() {

    override fun getIdentifier(): String {
        return "prism"
    }

    override fun getAuthor(): String {
        return "dxnny"
    }

    override fun getVersion(): String {
        return instance.version
    }

    override fun canRegister(): Boolean {
        return true
    }

    override fun onPlaceholderRequest(player: Player, params: String): String {
        val uuid: UUID = player.uniqueId
        return prismPlaceholders(params, uuid)
    }

    private fun prismPlaceholders(placeholder: String, uuid: UUID): String {
        val gradientId = instance.getStorage().getGradientId(uuid) ?: return "Null"

        val gradientConfig = instance.config.getConfigurationSection("gradients.$gradientId")
        val gradientColor = gradientConfig!!.getString("gradient")!!
        val username = Bukkit.getPlayer(uuid)?.name()!!
        val displayName = miniMessage().serialize(Bukkit.getPlayer(uuid)?.displayName()!!)
        // val customName = getPAPIPlaceholder(instabce.config.getString("options.alt-placeholder.placeholder")!!

        return when (placeholder) {
            "gradient_color" -> {
                gradientColor
            }
            "gradient_id" -> {
                gradientId
            }
            "name" -> {
                miniToLegacy("$gradientColor$username<reset>")
            "displayname" -> {
                miniToLegacy("$gradientColor$displayName<reset>")
            }
            "customname" -> {
                if (customName != null) {
                    miniToLegacy("$gradientColor$customName<reset>")
                } else {
                    "Null"
                }
            }
            else -> {
                "Null"
            }
        }
    }


}