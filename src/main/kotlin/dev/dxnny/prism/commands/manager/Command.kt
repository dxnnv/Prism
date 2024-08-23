package dev.dxnny.prism.commands.manager

import dev.dxnny.prism.utils.Permissions
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

@Suppress("SameReturnValue")
interface Command : CommandExecutor {

    fun name(): String
    fun syntax(): String
    fun description(): String
    fun permission(): Permissions
    override fun onCommand(sender: CommandSender, command: org.bukkit.command.Command, s: String, args: Array<out String>?): Boolean

}