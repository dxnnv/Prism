package dev.dxnny.prism.commands

import dev.dxnny.infrastructure.commands.CommandManager
import dev.dxnny.infrastructure.commands.ICommand
import dev.dxnny.infrastructure.utils.Permissions.Companion.hasPerm
import dev.dxnny.infrastructure.utils.Permissions.PermNode
import dev.dxnny.infrastructure.utils.text.MessageUtils.message
import dev.dxnny.infrastructure.utils.text.MessageUtils.mmParseWithTags
import dev.dxnny.infrastructure.utils.text.MessageUtils.sendSpacer
import dev.dxnny.prism.Prism
import dev.dxnny.prism.Prism.Companion.storage
import dev.dxnny.prism.files.Lang.Companion.CommandMessages
import dev.dxnny.prism.files.Lang.Companion.ErrorMessages
import dev.dxnny.prism.files.Lang.Companion.HelpMessages
import dev.dxnny.prism.manager.StorageManager.Companion.playerGradients
import dev.dxnny.prism.manager.GradientManager
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PrismCommand(private var plugin: Prism) : ICommand {
    override val name = "prism"
    override val syntax = "/prism <help|set|clear|reload> [player] [gradient]"
    override val description = "Prism admin command"
    override val permission = PermNode.Admin

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPerm(PermNode.Admin)) {
            sender.message(ErrorMessages.noPermission)
            return true
        }

        val subcommand = args[0]
        if (args.isEmpty() || subcommand.equals("help", ignoreCase = true)) {
            sender.help()
            return true
        }

        when (subcommand.lowercase()) {
            "set" -> if (validateArgs(sender, args.size, 3))
                change(sender, args)

            "reload" -> {
                plugin.onDisable()
                plugin.onEnable()
                sender.message(CommandMessages.reloaded)
            }

            "clear" -> if (validateArgs(sender, args.size, 2))
                clear(sender, args)
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        if (sender !is Player || !sender.hasPerm(PermNode.Admin)) return null
        var completions = mutableListOf<String>()
        val subcommand = args[0]

        when (args.size) {
            1 -> completions = mutableListOf("help", "set", "clear", "reload")
            2 -> if (subcommand == "clear" || subcommand == "set")
                completions = Bukkit.getOnlinePlayers().map { it.name }.toMutableList()
            3 -> if (subcommand == "set")
                completions = plugin.configuration.getConfigurationSection("gradients")!!.getKeys(false).toMutableList()
        }
        return completions
    }

    private fun validateArgs(sender: CommandSender, size: Int, min: Int): Boolean {
        if (size >= min)
            return true
        sender.message(ErrorMessages.notEnoughArgs)
        return false
    }

    private fun clear(sender: CommandSender, args: Array<out String>) {
        val target = plugin.server.getPlayer(args[1]) ?: return sender.message(ErrorMessages.invalidPlayer)

        sender.sendMessage(mmParseWithTags(CommandMessages.clearedOther, Placeholder.component("target", target.name())))
        playerGradients.put(target.uniqueId, null)
    }

    private fun change(sender: CommandSender, args: Array<out String>?) {
        val target = plugin.server.getPlayer(args!![1]) ?: return sender.message(ErrorMessages.invalidPlayer)

        val gradientId = args[2]
        val gradientData = GradientManager.gradientMap[gradientId] ?: return sender.message(ErrorMessages.invalidGradient)

        sender.sendMessage(mmParseWithTags(CommandMessages.appliedOther, Placeholder.component("target", target.name()), Placeholder.component("gradient", gradientData.gradientComponent)))
        storage.updatePlayer(target.uniqueId, GradientManager.gradientMap[gradientId])
    }

    private fun CommandSender.help() {
        this.run {
            message(HelpMessages.header, null)
            sendSpacer()
            CommandManager.commandList.forEach { command ->
                if (hasPerm(command.permission)) {
                    message(" " + HelpMessages.syntaxColor + command.syntax, null)
                    message("    <dark_grey>»</dark_grey> " + HelpMessages.descColor + command.description, null)
                }
                sendSpacer()
            }
            message(HelpMessages.footer, null)
        }
    }
}