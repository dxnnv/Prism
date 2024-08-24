package dev.dxnny.prism.hooks

import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.utils.ConsoleLog
import dev.dxnny.prism.utils.text.ColorUtils.miniToLegacy
import dev.dxnny.prism.utils.text.MessageUtils.mmParse
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.kyori.adventure.text.minimessage.MiniMessage
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
        ConsoleLog.debug("PAPI Request received...")
        return prismPlaceholders(params, uuid)
    }

    private fun prismPlaceholders(placeholder: String, uuid: UUID): String {

        val gradientId = instance.getStorage().getGradientId(uuid) ?: return "Null"
        ConsoleLog.debug("gradientId: $gradientId")

        val gradientConfig = instance.config.getConfigurationSection("gradients.$gradientId")
        ConsoleLog.debug("gradientConfig: ${gradientConfig.toString()}")
        val gradientColor = gradientConfig!!.getString("gradient")!!
        val displayName = miniMessage().serialize(Bukkit.getPlayer(uuid)?.displayName()!!)

        return when (placeholder) {
            "gradient_color" -> {
                gradientColor
            }
            "displayname" -> {
                ConsoleLog.debug("gradientColor: $gradientColor")
                ConsoleLog.debug("displayName: $displayName")
                ConsoleLog.debug("gradientColor: $displayName")
                miniToLegacy("$gradientColor$displayName<reset>")
            }
            "gradient_id" -> {
                gradientId
            }
            else -> {
                "Null"
            }
        }
    }


}