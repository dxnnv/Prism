package dev.dxnny.prism.files

import dev.dxnny.prism.Prism
import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.utils.ConsoleLog
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class Config(private var plugin: Prism) {
    private var file: File? = null
    private var customFile: FileConfiguration? = null
    private val version = instance.version

    init {
        val path = plugin.dataFolder
        file = File(path, "config.yml")

        if (file?.exists() == false) {
            plugin.saveDefaultConfig()
        } else {
            // If the file doesn't exist, create it
            customFile = YamlConfiguration.loadConfiguration(file!!)
            if (getConfigVersion() != this.version) {
                // rename config to config_old.yml and save new config in its place
                file!!.renameTo(File(path, "config_old.yml"))
                plugin.saveDefaultConfig()
                file = File(path, "config.yml")
                plugin.reloadConfig()
                ConsoleLog.info("\n\n\tA new config update has been applied!\n\tPlease copy over your settings from\n\tconfig_old.yml\n\tto config.yml")
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file!!)
    }

    fun get() = customFile
    fun reload() {
        customFile = YamlConfiguration.loadConfiguration(file!!)
        plugin.reloadConfig()
    }

    // Config Version
    private fun getConfigVersion() = get()?.getString("version", "1.0.0") ?: "0.0.0"
}