package dev.dxnny.prism.gui.items

import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.utils.text.MessageUtils.mmParse
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

class PageForwardItem: PageItem(false) {
    private val forwardItemConfig: ConfigurationSection = instance.config.getConfigurationSection("gui.page-forward")!!
    override fun getItemProvider(gui: PagedGui<*>?): ItemProvider {
        val builder = ItemBuilder(Material.getMaterial(forwardItemConfig.get("material").toString())!!)
        builder.setDisplayName(mmParse(forwardItemConfig.getString("name")!!))
        return builder
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        if (clickType == ClickType.LEFT){
            gui.goForward()
        }
    }
}