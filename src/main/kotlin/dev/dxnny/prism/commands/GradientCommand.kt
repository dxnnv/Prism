package dev.dxnny.prism.commands

import dev.dxnny.infrastructure.commands.ICommand
import dev.dxnny.infrastructure.utils.Permissions.PermNode
import dev.dxnny.prism.gui.menus.GradientMenu
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GradientCommand : ICommand {
    override val name = "gradients"
    override val syntax = "/gradients"
    override val description = "Opens the gradients GUI"
    override val permission = PermNode.create("use")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as? Player ?: return false
        GradientMenu.open(player)
        return true
    }
}