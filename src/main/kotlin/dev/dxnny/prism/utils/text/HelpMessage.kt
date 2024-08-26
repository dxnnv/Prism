package dev.dxnny.prism.utils.text

import dev.dxnny.infrastructure.commands.ICommand
import dev.dxnny.infrastructure.utils.text.MessageUtils.sendMessage
import dev.dxnny.infrastructure.utils.text.MessageUtils.sendSpacer
import dev.dxnny.prism.commands.manager.CommandManager.Companion.getCommands
import dev.dxnny.prism.files.Lang
import dev.dxnny.prism.utils.CheckPermission.hasPerm
import org.bukkit.command.CommandSender

class HelpMessage {
    companion object {

        fun sendHelp(s: CommandSender) {
            sendMessage(s, Lang.helpHeader, null)
            sendSpacer(s)
            getCommands().forEach { helpLine(s, it) }
            sendSpacer(s)
            sendMessage(s, Lang.helpFooter, null)
        }

        private fun helpLine(s: CommandSender, c: ICommand) {
            if (hasPerm(s, c.permission())) {
                sendMessage(s, (" " + Lang.helpSyntaxColor + c.syntax()), null)
                sendMessage(s, ("    <dark_grey>»</dark_grey> " + Lang.helpDescColor + c.description()), null)
            }
        }

    }
}
