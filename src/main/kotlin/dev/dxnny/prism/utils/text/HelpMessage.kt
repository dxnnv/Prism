package dev.dxnny.prism.utils.text

import dev.dxnny.prism.commands.manager.CommandManager.Companion.getStandaloneCommands
import dev.dxnny.prism.commands.manager.Command
import dev.dxnny.prism.files.Lang
import dev.dxnny.prism.utils.CheckPermission.hasPerm
import dev.dxnny.prism.utils.Permissions
import dev.dxnny.prism.utils.text.MessageUtils.sendMessage
import dev.dxnny.prism.utils.text.MessageUtils.sendSpacer
import org.bukkit.command.CommandSender

class HelpMessage {
    companion object {

        fun sendHelp(s: CommandSender) {
            sendMessage(s, Lang.helpHeader, null)
            sendSpacer(s)
            if (hasPerm(s, Permissions.GRADIENT_GUI)) sendSpacer(s)
            getStandaloneCommands().forEach { helpLine(s, it) }
            sendSpacer(s)
            sendMessage(s, Lang.helpFooter, null)
        }

        private fun helpLine(s: CommandSender, c: Command) {
            if (hasPerm(s, c.permission())) {
                sendMessage(s, (" " + Lang.helpSyntaxColor + c.syntax()), null)
                sendMessage(s, ("    <dark_grey>»</dark_grey> " + Lang.helpDescColor + c.description()), null)
            }
        }

    }
}
