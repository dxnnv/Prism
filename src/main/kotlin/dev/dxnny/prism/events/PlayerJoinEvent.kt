package dev.dxnny.prism.events

import dev.dxnny.prism.Prism
import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.utils.ConsoleLog
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinEvent(private var plugin: Prism): Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent){
        ConsoleLog.debug("Fired PlayerJoinEvent!")
        val storage = instance.getStorage()
        val uuid = e.player.uniqueId
        ConsoleLog.debug("Loading gradient...")
        if (storage.playerGradientExists(uuid)) {
            storage.loadPlayerGradient(uuid)
            ConsoleLog.debug("Loaded gradient ${storage.getGradientId(uuid)} for $uuid")
        } else {
            ConsoleLog.debug("no gradient found for user $uuid")
        }
    }
}