package dev.dxnny.prism.gui.items

import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.files.Lang
import dev.dxnny.prism.utils.text.MessageUtils.mmParse
import dev.dxnny.prism.utils.text.MessageUtils.sendMessage
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.builder.setDisplayName
import xyz.xenondevs.invui.item.impl.controlitem.PageItem

class ClearGradientItem : PageItem(false) {
    private val storage = instance.getStorage()
    private val clearGradientConfig: ConfigurationSection = instance.getConfiguration().get()!!.getConfigurationSection("gui.clear-gradient")!!
    override fun getItemProvider(gui: PagedGui<*>?): ItemProvider {
        val builder = ItemBuilder(Material.getMaterial(clearGradientConfig.get("material").toString())!!)
        builder.setDisplayName(mmParse(clearGradientConfig.getString("name")!!))
        return builder
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        if (clickType == ClickType.LEFT){
            if (!storage.getGradientId(player.uniqueId).isNullOrEmpty()) {
                storage.deletePlayerGradient(player.uniqueId)
                sendMessage(player, Lang.gradientCleared)
            } else {
                sendMessage(player, Lang.noGradientActive)
            }
        }
    }
}