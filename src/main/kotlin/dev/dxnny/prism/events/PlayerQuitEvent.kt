package dev.dxnny.prism.events

import dev.dxnny.prism.Prism
import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.utils.ConsoleLog
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitEvent(private var plugin: Prism) : Listener {
    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent){
        ConsoleLog.debug("Fired PlayerQuitEvent!")
        val storage = instance.getStorage()
        val uuid = e.player.uniqueId
        if (!storage.getGradientId(uuid).isNullOrEmpty()) {
            ConsoleLog.debug("Saving gradient...")
            val gradientId = storage.getGradientId(uuid)
            ConsoleLog.debug("GradientID:$gradientId")
            storage.savePlayerGradient(uuid, gradientId!!)
        }
    }
}