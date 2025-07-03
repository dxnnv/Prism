package dev.dxnny.prism.events

import dev.dxnny.prism.Prism.Companion.storage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinEvent : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent){
        event.player.uniqueId.run {
            if (storage.hasData(this)) storage.loadPlayer(this)
        }
    }
}