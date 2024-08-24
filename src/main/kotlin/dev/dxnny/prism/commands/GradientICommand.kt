package dev.dxnny.prism.commands

import dev.dxnny.prism.Prism
import dev.dxnny.prism.commands.manager.ICommand
import dev.dxnny.prism.gui.menus.GradientMenu
import dev.dxnny.prism.utils.Permissions
import dev.dxnny.prism.utils.text.MessageUtils.sendMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GradientICommand(private var plugin: Prism) : ICommand {
    override fun name(): String {
        return "gradients"
    }

    override fun syntax(): String {
        return "/gradients"
    }

    override fun description(): String {
        return "Opens the gradients GUI"
    }

    override fun permission(): Permissions {
        return Permissions.GRADIENT_GUI
    }

    override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            plugin.runTaskAsynchronously {GradientMenu.open(sender)}
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