@file:Suppress("DEPRECATION")

package dev.dxnny.prism.utils.text

import net.kyori.adventure.text.flattener.ComponentFlattener
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import net.kyori.adventure.text.serializer.legacy.CharacterAndFormat
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.ChatColor
import java.util.regex.Matcher
import java.util.regex.Pattern

object ColorUtils {
    private var formats: MutableList<CharacterAndFormat> = CharacterAndFormat.defaults()
    private var builder: LegacyComponentSerializer.Builder = LegacyComponentSerializer.builder()
            .flattener(ComponentFlattener.basic())
            .formats(formats)
            .useUnusualXRepeatedCharacterHexFormat()
            .hexColors()
    private val legacySerializer = builder.build()

    fun miniToLegacy(format: String): String {
        return legacySerializer.serialize(miniMessage().deserialize(format))
    }
    fun legacyColor(s: String): String {
        var text = s
        text = text.replace("&#".toRegex(), "#")

        val pattern: Pattern = Pattern.compile("#[a-fA-F0-9]{6}")
        var matcher: Matcher = pattern.matcher(text)

        if (text.isNotEmpty()) {
            while (matcher.find()) {
                val color = text.substring(matcher.start(), matcher.end())

                text = try {
                    text.replace(color, ChatColor.valueOf(color).toString() + "")
                } catch (e: NoSuchMethodError) {
                    text.replace(color, "")
                }

                matcher = pattern.matcher(text)
            }
        }

        return ChatColor.translateAlternateColorCodes('&', text)
    }
}