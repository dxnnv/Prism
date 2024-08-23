package dev.dxnny.prism.commands

import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.commands.manager.Command
import dev.dxnny.prism.gui.menus.GradientMenu
import dev.dxnny.prism.utils.Permissions
import dev.dxnny.prism.utils.text.MessageUtils.sendMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GradientCommand : Command {
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

    override fun onCommand(sender: CommandSender, command: org.bukkit.command.Command, s: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            instance.runTaskAsynchronously {GradientMenu.open(sender)}
            return true
        } else {
            sendMessage(sender, "This command can only be run by a player!")
            return true
        }
    }
}