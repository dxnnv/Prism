package dev.dxnny.prism.files

import dev.dxnny.prism.Prism
import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.utils.ConsoleLog
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class Config(plugin: Prism): YamlConfiguration() {
    private var file: File? = null
    private val version = instance.version

    init {
        val path = plugin.dataFolder
        file = File(path, "config.yml")

        if (!file!!.exists()) {
            plugin.saveDefaultConfig()
        } else {
            // If the file doesn't exist, create it
            loadConfiguration()
            if (getConfigVersion() != version) {
                // rename config to config_old.yml and save new config in its place
                file!!.renameTo(File(path, "config_old.yml"))
                plugin.saveDefaultConfig()
                file = File(path, "config.yml")
                ConsoleLog.info("\n\n\tA new config update has been applied!\n\tPlease copy over your settings from\n\tconfig_old.yml\n\tto config.yml")
                plugin.reloadConfig()
                loadConfiguration()
            }
        }
    }

    fun reload() {
        loadConfiguration()
    }
    private fun loadConfiguration() {
        this.load(file!!)
    }
    // Config Version
    private fun getConfigVersion() = getString("version", "1.0.0") ?: "0.0.0"
}