package dev.dxnny.prism.utils.text

import net.kyori.adventure.text.flattener.ComponentFlattener
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import net.kyori.adventure.text.serializer.legacy.CharacterAndFormat
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

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
}