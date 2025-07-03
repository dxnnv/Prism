package dev.dxnny.prism

import dev.dxnny.infrastructure.Infrastructure
import dev.dxnny.infrastructure.commands.CommandManager
import dev.dxnny.infrastructure.files.Config
import dev.dxnny.infrastructure.files.Messages
import dev.dxnny.infrastructure.utils.ConsoleLog
import dev.dxnny.prism.commands.GradientCommand
import dev.dxnny.prism.commands.PrismCommand
import dev.dxnny.prism.events.PlayerJoinEvent
import dev.dxnny.prism.events.PlayerQuitEvent
import dev.dxnny.prism.files.Lang
import dev.dxnny.prism.hooks.PlaceholderAPIHook
import dev.dxnny.prism.manager.StorageManager
import dev.dxnny.prism.manager.GradientManager
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

private const val LOG_LIST_PREFIX = " <dark_gray>\\-<dark_gray><green>"

class Prism : JavaPlugin() {
    companion object {
        lateinit var instance: Prism
        lateinit var messages: Messages
        lateinit var storage: StorageManager
    }

    lateinit var console: ConsoleCommandSender
    lateinit var configuration: Config
    lateinit var manager: PluginManager
    lateinit var infrastructure: Infrastructure
    val version: String = pluginMeta.version
    val configVersion = "2.0.0"
    val messagesVersion = "2.0.0"

    override fun onLoad() {
        this.manager = server.pluginManager
    }

    override fun onEnable() {
        instance = this
        infrastructure = Infrastructure(this)
        infrastructure.logPrefix = "<dark_gray>[<light_purple>Prism</light_purple>]<reset> "
        console = server.consoleSender
        sendStartupMessage()

        setupConfig()
        setupMessages()
        setupStorage()

        registerCommands()
        registerListeners()
        registerHooks()
    }

    override fun onDisable() {
        storage.close()
    }

    private fun setupConfig() {
        configuration = Config(this, configVersion)
        GradientManager.loadGradientItems()
    }

    private fun setupMessages() {
        messages = Messages(this, messagesVersion)
        Lang.updateMessages()
        infrastructure.prefix = Lang.prefix
    }

    private fun setupStorage() {
        ConsoleLog.info("Initializing storage...")
        storage = StorageManager("${dataFolder.path}/prism.db")
        server.onlinePlayers.forEach {
            storage.loadPlayer(it.uniqueId)
        }
        ConsoleLog.success("$LOG_LIST_PREFIX Loaded storage!")
    }

    private fun registerCommands() {
        ConsoleLog.info("Registering commands...")
        CommandManager(this, PrismCommand(this), GradientCommand())
        ConsoleLog.success("$LOG_LIST_PREFIX Registered commands!")
    }

    private fun registerListeners() {
        ConsoleLog.info("Registering listeners...")
        manager.registerEvents(PlayerJoinEvent(), this)
        manager.registerEvents(PlayerQuitEvent(), this)
        ConsoleLog.success("$LOG_LIST_PREFIX Registered listeners!")
    }

    private fun registerHooks() {
        ConsoleLog.info("Registering PlaceholderAPI expansion...")
        if (manager.isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPIHook().register()
        } else {
            ConsoleLog.fatal("Unable to find PlaceholderAPI! Disabling plugin...")
            manager.disablePlugin(this)
        }
    }

    private fun sendStartupMessage() {
        ConsoleLog.info(" ")
        ConsoleLog.info("<rainbow> ______   ______     __     ______     __    __")
        ConsoleLog.info("<rainbow>/\\  == \\ /\\  == \\   /\\ \\   /\\  ___\\   /\\ \"-./  \\")
        ConsoleLog.info("<rainbow>\\ \\  _-/ \\ \\  __<   \\ \\ \\  \\ \\___  \\  \\ \\ \\-./\\ \\")
        ConsoleLog.info("<rainbow> \\ \\_\\    \\ \\_\\ \\_\\  \\ \\_\\  \\/\\_____\\  \\ \\_\\ \\ \\_\\")
        ConsoleLog.info("<rainbow>  \\/_/     \\/_/ /_/   \\/_/   \\/_____/   \\/_/  \\/_/")
        ConsoleLog.info(" ")
        ConsoleLog.info("<light_purple>Starting Prism v$version, created by dxnnv...")
    }
}
