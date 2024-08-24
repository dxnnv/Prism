package dev.dxnny.prism.commands.manager

import dev.dxnny.prism.utils.Permissions
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

@Suppress("SameReturnValue")
interface ICommand : CommandExecutor, TabCompleter {

    fun name(): String
    fun syntax(): String
    fun description(): String
    fun permission(): Permissions
    override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>?): Boolean
    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String>?
}