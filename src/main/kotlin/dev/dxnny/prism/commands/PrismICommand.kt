package dev.dxnny.prism.commands

import dev.dxnny.prism.Prism
import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.commands.manager.ICommand
import dev.dxnny.prism.files.Lang
import dev.dxnny.prism.files.Messages
import dev.dxnny.prism.utils.CheckPermission.hasPerm
import dev.dxnny.prism.utils.Permissions
import dev.dxnny.prism.utils.gradients.GradientManager
import dev.dxnny.prism.utils.text.HelpMessage
import dev.dxnny.prism.utils.text.MessageUtils.mmParseWithTags
import dev.dxnny.prism.utils.text.MessageUtils.sendMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PrismICommand(private var plugin: Prism) : ICommand {

    override fun name(): String {
        return "prism"
    }

    override fun syntax(): String {
        return "/prism [set|clear|reload] [player] [gradient]"
    }

    override fun description(): String {
        return "Prism admin command"
    }

    override fun permission(): Permissions {
        return Permissions.PRISM_ADMIN
    }

    override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>?): Boolean {
        if (args!!.isEmpty()) {
            sendMessage(sender, Lang.notEnoughArgs + "Use: ${syntax()}")
            return true
        } else {
            when (args[0].lowercase()) {
                "set" -> {
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

                    plugin.reloadConfiguration()
                    Messages(plugin).reload()
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

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String>? {
        if (sender !is Player) return null
        if (args?.size == 1) {
            return mutableListOf("set", "clear", "reload")
        } else if (args?.size == 2 && (args[0] == "clear" || args[0] == "set") && hasPerm(sender, Permissions.PRISM_ADMIN)) {
            val l: MutableList<String> = mutableListOf()
            Bukkit.getOnlinePlayers().forEach { l.add(it.name) }
            return l
        } else if (args?.size == 3 && args[0] == "set" && hasPerm(sender, Permissions.PRISM_ADMIN)) {
            val l: MutableList<String> = mutableListOf()
            instance.getConfiguration().get()!!.getConfigurationSection("gradients")!!.getKeys(false).toTypedArray().forEach {
                l.add(it)
            }
            return l
        } else {
            return mutableListOf()
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