package dev.dxnny.prism

import dev.dxnny.prism.commands.manager.CommandManager
import dev.dxnny.prism.events.PlayerJoinEvent
import dev.dxnny.prism.events.PlayerQuitEvent
import dev.dxnny.prism.files.Config
import dev.dxnny.prism.files.Lang.Companion.updateMessages
import dev.dxnny.prism.files.Messages
import dev.dxnny.prism.hooks.PlaceholderAPIHook
import dev.dxnny.prism.storage.LiteManager
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

class Prism : JavaPlugin() {
    companion object {
        lateinit var instance: Prism
    }

    private var enabled = false
    lateinit var configuration: Config
    private lateinit var messages: Messages
    lateinit var manager: PluginManager
    private lateinit var storage: LiteManager
    val version = "1.0.0"

    override fun onLoad() {
        this.manager = server.pluginManager
    }

    override fun onEnable() {
        instance = this

        // config
        logger.info("Loading config.yml...")
        this.configuration = Config(this)
        saveConfig()
        configuration.reload()
        logger.info("Loaded config.yml")

        // messages
        logger.info("Loading messages.yml...")
        this.messages = Messages(this)
        messages.setup()
        updateMessages(this)
        logger.info("Loaded messages.yml")

        // Load data
        logger.info("Loading player data...")
        val dbPath = this.dataFolder.path + "/prism.db"
        this.storage = LiteManager(dbPath)
        if (this.server.onlinePlayers.isNotEmpty()) {
            storage.loadAllOnlinePlayersGradients(this.server.onlinePlayers)
        }
        logger.info("Loaded player data")

        // commands
        logger.info("Registering Commands...")
        CommandManager(this)
        logger.info("Registered Commands!")

        // events
        logger.info("Registering Events...")
        manager.registerEvents(PlayerJoinEvent(), this)
        manager.registerEvents(PlayerQuitEvent(), this)
        logger.info("Registered Events!")

        // PlaceholderAPI
        logger.info("Attempting to find PlaceholderAPI...")
        if (manager.getPlugin("PlaceholderAPI") != null) {
            PlaceholderAPIHook().register()
            logger.info("PlaceholderAPI expansion registered.")
        } else {
            logger.warning("Unable to find PlaceholderAPI! Disabling plugin...")
            this.manager.disablePlugin(this)
        }

        this.enabled = true
    }

    override fun onDisable() {
        try {
            storage.saveAllPlayerGradients()
        } catch (e: Exception) {
            logger.severe("Error saving data: ${e.message}")
            e.printStackTrace()
        } finally {
            storage.close(this)
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
