package dev.dxnny.prism.events

import dev.dxnny.prism.Prism.Companion.storage
import dev.dxnny.prism.manager.StorageManager.Companion.playerGradients
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitEvent : Listener {
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent){
        event.player.uniqueId.run {
            playerGradients.remove(this).let { gradientData -> storage.savePlayer(this, gradientData) }
        }
    }
}