package dev.dxnny.prism.gui.items

import dev.dxnny.infrastructure.utils.text.MessageUtils.message
import dev.dxnny.infrastructure.utils.text.MessageUtils.mmParseWithTags
import dev.dxnny.prism.Prism.Companion.storage
import dev.dxnny.prism.files.Lang.Companion.CommandMessages
import dev.dxnny.prism.files.Lang.Companion.ErrorMessages
import dev.dxnny.prism.objects.GradientData
import dev.dxnny.prism.manager.StorageManager.Companion.playerGradients
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.impl.AbstractItem

class GradientItem(private val provider: ItemProvider, private val data: GradientData) : AbstractItem() {
    override fun getItemProvider() = provider

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        if (player.hasPermission(data.permission)) {
            if (playerGradients[player.uniqueId] == data)
                return player.message(CommandMessages.alreadyActive)

            player.sendMessage(mmParseWithTags(CommandMessages.applied, Placeholder.component("gradient", data.gradientComponent)))
            storage.updatePlayer(player.uniqueId, data)
        } else
            player.message(ErrorMessages.noPermissionGradient)

        event.inventory.close()
    }
}