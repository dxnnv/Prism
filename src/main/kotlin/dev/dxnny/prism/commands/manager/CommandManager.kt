package dev.dxnny.prism.commands.manager

import dev.dxnny.infrastructure.commands.ICommand
import dev.dxnny.prism.Prism
import dev.dxnny.prism.commands.PrismICommand
import dev.dxnny.prism.commands.GradientICommand
import dev.dxnny.prism.utils.CheckPermission.hasPerm
import org.bukkit.command.*
import org.bukkit.entity.Player

class CommandManager(private var plugin: Prism) : CommandExecutor {

    init {
        ICommands.add(PrismICommand(plugin))
        ICommands.add(GradientICommand(plugin))
        ICommands.forEach {
            plugin.getCommand(it.name())!!.setExecutor(it)
            plugin.getCommand(it.name())!!.tabCompleter = it
        }
    }

    companion object {
        var ICommands: MutableList<ICommand> = mutableListOf()
        fun getCommands(): MutableList<ICommand> {
            return ICommands
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        ICommands.forEach { c ->
            if (command.name.equals(c.name(), ignoreCase = true)) {
                if ((sender is Player && hasPerm(sender, c.permission()) || sender is ConsoleCommandSender)) {
                    c.onCommand(sender, command, c.name(), args)
                    return true
                }
            }
        }
        return false
    }
}