package dev.dxnny.prism.gui.items

import dev.dxnny.prism.utils.GetItem.getGuiItem
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.item.impl.controlitem.PageItem

class MetaItems {

    class FillerItem : SimpleItem(getGuiItem("filler"))

    class PageForwardItem : PageItem(true) {
        override fun getItemProvider(gui: PagedGui<*>?) = getGuiItem("page-forward")
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) = gui.goForward()
    }

    class PageBackItem : PageItem(false) {
        override fun getItemProvider(gui: PagedGui<*>?) = getGuiItem("page-back")
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) = gui.goBack()
    }
}