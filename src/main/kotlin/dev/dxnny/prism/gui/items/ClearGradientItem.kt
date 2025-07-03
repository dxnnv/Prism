package dev.dxnny.prism.gui.items

import dev.dxnny.infrastructure.utils.text.MessageUtils.message
import dev.dxnny.prism.Prism.Companion.storage
import dev.dxnny.prism.files.Lang.Companion.CommandMessages
import dev.dxnny.prism.files.Lang.Companion.ErrorMessages
import dev.dxnny.prism.manager.StorageManager.Companion.playerGradients
import dev.dxnny.prism.utils.GetItem.getGuiItem
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.impl.AbstractItem

class ClearGradientItem : AbstractItem() {
    override fun getItemProvider() = getGuiItem("clear-gradient")

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        playerGradients.remove(player.uniqueId)?.let {
            storage.updatePlayer(player.uniqueId, null)
            player.message(CommandMessages.cleared)
        } ?: return player.message(ErrorMessages.noGradientActive)
    }
}