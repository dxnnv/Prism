package dev.dxnny.prism.events

import dev.dxnny.prism.Prism.Companion.instance
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitEvent : Listener {
    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent){
        val storage = instance.getStorage()
        val uuid = e.player.uniqueId
        if (!storage.getGradientId(uuid).isNullOrEmpty()) {
            val gradientId = storage.getGradientId(uuid)
            storage.savePlayerGradient(uuid, gradientId!!)

        }
    }
}