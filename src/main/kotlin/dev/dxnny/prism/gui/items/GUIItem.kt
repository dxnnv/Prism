package dev.dxnny.prism.gui.items

import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.files.Lang
import dev.dxnny.prism.utils.ConsoleLog
import dev.dxnny.prism.utils.gradients.GradientManager
import dev.dxnny.prism.utils.text.MessageUtils.mmParseWithTags
import dev.dxnny.prism.utils.text.MessageUtils.sendMessage
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
    private var gradientName = instance.config.getString("gradients.$gradient.name")

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
            ConsoleLog.debug("Set gradient for ${player.name} to $gradientId")
        } else {
            sendMessage(player, Lang.noPermissionGradient)
        }
        event.inventory.close()
    }
}