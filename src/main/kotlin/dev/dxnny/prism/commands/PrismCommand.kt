package dev.dxnny.prism.commands

import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.commands.manager.Command
import dev.dxnny.prism.files.Lang
import dev.dxnny.prism.utils.CheckPermission.hasPerm
import dev.dxnny.prism.utils.Permissions
import dev.dxnny.prism.utils.gradients.GradientManager
import dev.dxnny.prism.utils.text.HelpMessage
import dev.dxnny.prism.utils.text.MessageUtils.mmParseWithTags
import dev.dxnny.prism.utils.text.MessageUtils.sendMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PrismCommand : Command {

    override fun name(): String {
        return "prism"
    }

    override fun syntax(): String {
        return "/prism [setother|clear|reload]"
    }

    override fun description(): String {
        return "Prism admin command"
    }

    override fun permission(): Permissions {
        return Permissions.PRISM_ADMIN
    }

    override fun onCommand(sender: CommandSender, command: org.bukkit.command.Command, s: String, args: Array<out String>?): Boolean {
        if (args!!.isEmpty()) {
            sendMessage(sender, Lang.notEnoughArgs + "Use: ${syntax()}")
            return true
        } else {
            when (args[0].lowercase()) {
                "setother" -> {
                    if (!hasPerm(sender, Permissions.PRISM_ADMIN)) {
                        sendMessage(sender, Lang.noPermission)
                        return true
                    }

                    if (args.size != 3) {
                        sendMessage(sender, Lang.notEnoughArgs)
                        return false
                    }

                    change(sender, args)
                }

                "reload" -> {
                    if (!hasPerm(sender, Permissions.PRISM_ADMIN)) {
                        sendMessage(sender, Lang.noPermission)
                        return true
                    }

                    instance.reloadConfig()
                    instance.getMessages().reload()
                    sendMessage(sender, Lang.configReloaded)
                }

                "help" -> {
                    if (!hasPerm(sender, Permissions.PRISM_ADMIN)) {
                        sendMessage(sender, Lang.noPermission)
                        return true
                    }

                    HelpMessage.sendHelp(sender)
                }

                "clear" -> {
                    if (!hasPerm(sender, Permissions.PRISM_ADMIN)) {
                        sendMessage(sender, Lang.noPermission)
                        return true
                    }

                    if (args.size != 2) {
                        sendMessage(sender, Lang.notEnoughArgs)
                        return false
                    }

                    clear(sender, args)
                }

                "debug" -> {
                    if (!hasPerm(sender, Permissions.PRISM_ADMIN)) {
                        sendMessage(sender, Lang.noPermission)
                        return true
                    }

                    if (args.size != 2) {
                        sendMessage(sender, Lang.notEnoughArgs)
                        return false
                    }

                    val storage = instance.getStorage()
                    if(args[1] == "null") {
                        storage.readMap()
                    }
                    val target: Player? = instance.server.getPlayer(args[1])
                    if (target == null) {
                        sendMessage(sender, Lang.invalidPlayer)
                    } else {
                        val gradientIdFromMap = storage.getGradientId(target.uniqueId)
                        sendMessage(sender, "$gradientIdFromMap")
                    }
                }
            }
            return true
        }
    }

    private fun clear(sender: CommandSender, args: Array<out String>) {
        val target: Player? = instance.server.getPlayer(args[1])
        if (target == null) {
            sendMessage(sender, Lang.invalidPlayer)
        } else {
            sender.sendMessage(mmParseWithTags(Lang.gradientClearedOther, Placeholder.component("target", target.name())))
            instance.getStorage().deletePlayerGradient(target.uniqueId)
        }
    }
}

    private fun change(sender: CommandSender, args: Array<out String>?) {
        val target: Player? = instance.server.getPlayer(args!![1])
        if (target == null) {
            sendMessage(sender, Lang.invalidPlayer)
        } else {
            val gradientId = args[2]
            if (!GradientManager.gradientExists(gradientId)) {
                sendMessage(sender, Lang.gradientNonExistent)
                return
            }

            sender.sendMessage(mmParseWithTags(Lang.gradientAppliedOther, Placeholder.component("target", target.name()), Placeholder.component("gradient", GradientManager.getGradientComponent(gradientId))))
            instance.getStorage().insertOrUpdatePlayerGradient(target.uniqueId, gradientId)
        }
    }