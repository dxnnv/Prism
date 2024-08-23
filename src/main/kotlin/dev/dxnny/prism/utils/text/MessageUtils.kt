package dev.dxnny.prism.utils.text

import dev.dxnny.prism.files.Lang
import dev.dxnny.prism.utils.ConsoleLog
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

@Suppress("MemberVisibilityCanBePrivate")
object MessageUtils {

    fun getStrippedMessage(s: String?): String {
        return MiniMessage.miniMessage().stripTags(s!!)
    }

    fun mmParseWithPrefix(m: String, p: String): Component {
        return MiniMessage.miniMessage().deserialize(p + m)
    }

    fun mmParse(m: String): Component {
        return MiniMessage.miniMessage().deserialize(m)
    }

    fun mmParseWithTags(m: String, vararg t: TagResolver): Component {
        val tags = TagResolver.builder().resolvers(t.toMutableList()).build()
        return MiniMessage.miniMessage().deserialize(m, tags)
    }

    fun sendMessage(sender: CommandSender, s: String, prefix: String? = Lang.prefix) {
        if (sender is Player) {
            if (prefix.isNullOrEmpty()) {
                sender.sendMessage(mmParse(s))
            } else {
                sender.sendMessage(mmParseWithPrefix(s, prefix))
            }
        } else if (sender is ConsoleCommandSender) {
            ConsoleLog.info(getStrippedMessage(s))
        }
    }

    @Suppress("unused")
    fun sendString(sender: CommandSender, s: String) {
        if (sender is Player) {
            sender.sendMessage(s)
        } else if (sender is ConsoleCommandSender) {
            ConsoleLog.info(s)
        }
    }

    fun sendSpacer(sender: CommandSender) {
        if (sender is Player) {
            sender.sendMessage(" ")
        } else if (sender is ConsoleCommandSender) {
            ConsoleLog.info(" ")
        }
    }

}
