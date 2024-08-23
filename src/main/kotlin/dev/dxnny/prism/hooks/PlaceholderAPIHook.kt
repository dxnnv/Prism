package dev.dxnny.prism.hooks

import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.utils.ConsoleLog
import dev.dxnny.prism.utils.text.MessageUtils.mmParse
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
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

        val gradientId = instance.getStorage().getGradientId(uuid) ?: "None"
        ConsoleLog.debug("gradientId: $gradientId")

        val gradientConfig = instance.config.getConfigurationSection("gradients.$gradientId")
        ConsoleLog.debug("gradientConfig: ${gradientConfig.toString()}")
        val gradientColor = gradientConfig!!.getString("gradient")!!
        val playerName = Bukkit.getPlayer(uuid)?.displayName()

        val displayName: Component = mmParse("$gradientColor$playerName<reset>")

        return when (placeholder) {
            "gradient_color" -> {
                gradientColor
            }
            "displayname" -> {
                LegacyComponentSerializer.builder().hexColors().build().serialize(displayName)
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