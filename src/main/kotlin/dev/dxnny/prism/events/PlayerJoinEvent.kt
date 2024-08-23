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
        if (!storage.getGradientId(uuid).isNullOrEmpty()) {
            ConsoleLog.debug("Loading gradient...")
            instance.getStorage().loadPlayerGradient(uuid)
        }
    }
}