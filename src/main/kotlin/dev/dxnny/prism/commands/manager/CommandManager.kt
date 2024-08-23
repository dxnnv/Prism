package dev.dxnny.prism.commands.manager

import dev.dxnny.prism.Prism
import dev.dxnny.prism.commands.PrismCommand
import dev.dxnny.prism.commands.GradientCommand
import dev.dxnny.prism.utils.CheckPermission.hasPerm
import dev.dxnny.prism.utils.Permissions
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.entity.Player

class CommandManager(plugin: Prism) : CommandExecutor, TabCompleter {

    init {
        commands.add(PrismCommand())
        commands.add(GradientCommand())
        commands.forEach { plugin.getCommand(it.name())!!.setExecutor(it) }
    }

    companion object {
        var commands: MutableList<Command> = mutableListOf()
        fun getStandaloneCommands(): MutableList<Command> {
            return commands
        }
    }

    override fun onCommand(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        label: String,
        args: Array<out String>?
    ): Boolean {
        commands.forEach { c ->
            if (command.name.equals(c.name(), ignoreCase = true)) {
                if ((sender is Player && hasPerm(sender, c.permission()) || sender is ConsoleCommandSender)) {
                    c.onCommand(sender, command, c.name(), args)
                    return true
                }
            }
        }
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String>? {
        if (sender !is Player) return null
        if (args?.size == 3 && hasPerm(sender, Permissions.PRISM_ADMIN)) {
            val l: MutableList<String> = mutableListOf()
            Bukkit.getOnlinePlayers().forEach { l.add(it.name) }
            return l
        }
        return null
    }
}