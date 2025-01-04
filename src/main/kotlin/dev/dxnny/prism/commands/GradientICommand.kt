package dev.dxnny.prism.commands

import dev.dxnny.infrastructure.utils.text.MessageUtils.sendMessage
import dev.dxnny.infrastructure.commands.ICommand
import dev.dxnny.prism.gui.menus.GradientMenu
import dev.dxnny.prism.utils.Permissions
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GradientICommand : ICommand {
    override fun name(): String {
        return "gradients"
    }

    override fun syntax(): String {
        return "/gradients"
    }

    override fun description(): String {
        return "Opens the gradients GUI"
    }

    override fun permission(): String {
        return Permissions.GRADIENT_GUI.p
    }

    override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            GradientMenu.open(sender)
            return true
        } else {
            sendMessage(sender, "This command can only be run by a player!")
            return true
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String>? {
        return null
    }
}