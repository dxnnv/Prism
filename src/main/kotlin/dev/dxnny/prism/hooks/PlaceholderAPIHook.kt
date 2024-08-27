package dev.dxnny.prism.hooks

import dev.dxnny.infrastructure.utils.text.ColorUtils.miniToLegacy
import dev.dxnny.prism.Prism.Companion.instance
import me.clip.placeholderapi.PlaceholderAPI
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

    override fun persist(): Boolean {
        return true
    }

    override fun canRegister(): Boolean {
        return true
    }

    override fun onPlaceholderRequest(player: Player, params: String): String {
        val uuid: UUID = player.uniqueId
        return prismPlaceholders(params, uuid)
    }

    private fun prismPlaceholders(placeholder: String, uuid: UUID): String {

        var gradientId = "null"
        var gradientColor = "null"
        var hasGradient = false

        val id: String? = instance.getStorage().getGradientId(uuid)
        if (id != "null" && id != null) {
            hasGradient = true
            gradientId = id
            gradientColor = instance.config.getConfigurationSection("gradients.$id")!!.getString("gradient")!!
        }

        val player: Player? = Bukkit.getPlayer(uuid)
        if (player != null) {
            val customName = instance.config.getString("options.alt-placeholder.placeholder")?.let { PlaceholderAPI.setPlaceholders(player, it) }
            val ign: String = miniMessage().serialize(Bukkit.getPlayer(uuid)?.name()!!)
            val display: String = miniMessage().serialize(player.displayName())

            when (placeholder) {
                "gradient_color" -> {
                    return gradientColor
                }

                "gradient_id" -> {
                    return gradientId
                }

                "name" -> {
                    return if (hasGradient) {
                        miniToLegacy("$gradientColor$ign<reset>")
                    } else {
                        return ign
                    }
                }

                "displayname" -> {
                    return if (hasGradient) {
                        miniToLegacy("$gradientColor$display<reset>")
                    } else {
                        miniToLegacy(display)
                    }
                }

                "customname" -> {
                    return if (customName != null) {
                        if (hasGradient) {
                            miniToLegacy("$gradientColor$customName<reset>")
                        } else {
                            customName
                        }
                    } else {
                        "Invalid customname"
                    }
                }
                else -> {
                    return "Invalid Placeholder"
                }
            }
        }
        return "Player is null"
    }
}