package dev.dxnny.prism.events

import dev.dxnny.prism.Prism.Companion.instance
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinEvent : Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent){
        val storage = instance.getStorage()
        val uuid = e.player.uniqueId
        if (storage.playerGradientExists(uuid)) storage.loadPlayerGradient(uuid)
    }
}