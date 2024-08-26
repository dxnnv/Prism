package dev.dxnny.prism.gui.items

import dev.dxnny.infrastructure.utils.text.MessageUtils.mmParseWithTags
import dev.dxnny.infrastructure.utils.text.MessageUtils.sendMessage
import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.files.Lang
import dev.dxnny.prism.utils.GradientManager
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.impl.AbstractItem

class GUIItem(itemProvider: ItemProvider, private var gradient: String?) : AbstractItem() {

    private var provider = itemProvider
    private var gradientId = gradient
    private var gradientFull = gradient?.let { GradientManager.getGradientComponent(it) }

    override fun getItemProvider(): ItemProvider {
        return provider
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        if (gradient.isNullOrEmpty()) return
        if (GradientManager.hasGradientPermission(player, gradient!!)) {
            if (instance.getStorage().getGradientId(player.uniqueId) == gradient) {
                sendMessage(player, Lang.gradientAlreadyActive)
                return
            }
            player.sendMessage(mmParseWithTags(Lang.gradientApplied, Placeholder.component("gradient", gradientFull!!)))
            instance.getStorage().insertOrUpdatePlayerGradient(player.uniqueId, gradientId!!)
        } else {
            sendMessage(player, Lang.noPermissionGradient)
        }
        event.inventory.close()
    }
}