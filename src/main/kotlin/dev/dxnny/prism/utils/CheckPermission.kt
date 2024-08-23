package dev.dxnny.prism.utils

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

object CheckPermission {
    fun hasPerm(s: CommandSender, perm: Permissions): Boolean {

        if (s is ConsoleCommandSender) return true

        if (s.hasPermission(Permissions.WILDCARD.perm)) return true

        return s.hasPermission(perm.perm)

    }
}