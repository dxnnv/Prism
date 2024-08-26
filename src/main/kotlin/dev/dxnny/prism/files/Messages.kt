package dev.dxnny.prism.files

import dev.dxnny.infrastructure.utils.ConsoleLog
import dev.dxnny.prism.files.Lang.Companion.updateMessages
import dev.dxnny.prism.Prism
import dev.dxnny.prism.Prism.Companion.instance
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.logging.Level

class Messages(private var plugin: Prism) {
    private var messagesFile: File? = null
    private var messagesConfig: FileConfiguration? = null
    private val version = instance.version

    fun setup() {
        val path = plugin.dataFolder
        messagesFile = File(path, "messages.yml")

        saveDefaultMessages()

        // If file exists, load file and check if it's the correct version
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile!!)
        if (this.get()?.getString("version") != this.version) {
            // rename config to messages_old.yml and save new messages file in its place
            messagesFile?.renameTo(File(path, "messages_old.yml"))
            saveDefaultMessages()
            messagesFile = File(path, "messages.yml")
            reload()
            ConsoleLog.info("\n\n\tA new messages.yml update has been applied!\n\tPlease copy over your settings from\n\tmessages_old.yml\n\tto messages.yml")
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile!!)
    }

    fun get() = messagesConfig

    fun reload() {
        // Loads file contents to config var
        messagesConfig = messagesFile?.let { YamlConfiguration.loadConfiguration(it) }
        updateMessages(plugin)
    }

    private fun saveDefaultMessages() {
        if (!messagesFile!!.exists()) {
            try {
                // If the file doesn't exist, create it
                plugin.saveResource("messages.yml", false)
            } catch (e: IOException) {
                ConsoleLog.logExp(Level.SEVERE, "Failed to create messages.yml", e)

                // Disable this plugin bc no reason to run it if all messages break
                plugin.manager.disablePlugin(plugin)
            }
        }
    }
}