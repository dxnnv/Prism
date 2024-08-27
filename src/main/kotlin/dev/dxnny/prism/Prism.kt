package dev.dxnny.prism

import dev.dxnny.infrastructure.Infrastructure
import dev.dxnny.infrastructure.files.Config
import dev.dxnny.infrastructure.utils.ConsoleLog.logMessage
import dev.dxnny.prism.commands.manager.CommandManager
import dev.dxnny.prism.events.PlayerJoinEvent
import dev.dxnny.prism.events.PlayerQuitEvent
import dev.dxnny.prism.files.Lang
import dev.dxnny.prism.files.Lang.Companion.updateMessages
import dev.dxnny.prism.files.Messages
import dev.dxnny.prism.hooks.PlaceholderAPIHook
import dev.dxnny.prism.storage.LiteManager
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

class Prism : JavaPlugin() {
    companion object {
        lateinit var instance: Prism
    }

    lateinit var console: ConsoleCommandSender
    lateinit var configuration: Config
    private lateinit var messages: Messages
    private lateinit var storage: LiteManager
    lateinit var manager: PluginManager
    lateinit var infrastructure: Infrastructure
    val version = "0.0.2"

    override fun onLoad() {
        this.manager = server.pluginManager
    }

    override fun onEnable() {
        instance = this
        infrastructure = Infrastructure(this)
        infrastructure.setPluginLogPrefix("&8[&dPrism&8] ")
        console = this.server.consoleSender

        // startup message
        logMessage(" ")
        logMessage("&c ______   &6______     &e__     &a______     &b__    __")
        logMessage("&c/\\  == \\ &6/\\  == \\   &e/\\ \\   &a/\\  ___\\   &b/\\ \"-./  \\")
        logMessage("&c\\ \\  _-/ &6\\ \\  __<   &e\\ \\ \\  &a\\ \\___  \\  &b\\ \\ \\-./\\ \\")
        logMessage("&c \\ \\_\\   &6 \\ \\_\\ \\_\\ &e \\ \\_\\ &a \\/\\_____\\ &b \\ \\_\\ \\ \\_\\")
        logMessage("&c  \\/_/   &6  \\/_/ /_/ &e  \\/_/ &a  \\/_____/ &b  \\/_/  \\/_/")
        logMessage(" ")
        // config
        logMessage("&7Loading files...")
        this.configuration = Config(this)
        logMessage(" &8\\- &aLoaded config.yml")

        // messages
        this.messages = Messages(this)
        messages.setup()
        updateMessages(this)
        infrastructure.setPluginPrefix(Lang.prefix)
        logMessage(" &8\\- &aLoaded messages.yml")

        // Load data
        val dbPath = this.dataFolder.path + "/prism.db"
        this.storage = LiteManager(dbPath)
        if (this.server.onlinePlayers.isNotEmpty()) {
            storage.loadAllOnlinePlayersGradients(this.server.onlinePlayers)
        }
        logMessage(" &8\\- &aLoaded prism.db")

        // commands
        logMessage("&7Registering commands + events...")
        CommandManager(this)
        logMessage(" &8\\- &aRegistered commands")

        // events
        manager.registerEvents(PlayerJoinEvent(), this)
        manager.registerEvents(PlayerQuitEvent(), this)
        logMessage(" &8\\- &aRegistered events")

        // PlaceholderAPI
        logMessage("&7Registering PlaceholderAPI expansion...")
        if (manager.isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPIHook().register()
        } else {
            logger.warning("Unable to find PlaceholderAPI! Disabling plugin...")
            this.manager.disablePlugin(this)
        }
    }

    override fun onDisable() {
        try {
            storage.saveAllPlayerGradients()
        } catch (e: Exception) {
            logger.severe("Error saving data: ${e.message}")
            e.printStackTrace()
        } finally {
            storage.close()
        }
    }

    fun runTaskAsynchronously(run: Runnable): BukkitTask {
        return this.server.scheduler.runTaskAsynchronously(this, run)
    }

    // getters
    fun getMessagesFile(): FileConfiguration {
        return messages.get()!!
    }

    fun getMessages(): Messages {
        return messages
    }

    fun getStorage(): LiteManager {
        return storage
    }
}
