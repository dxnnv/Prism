package dev.dxnny.prism.gui.menus

import dev.dxnny.infrastructure.utils.ConsoleLog
import dev.dxnny.infrastructure.utils.text.MessageUtils.mmParse
import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.gui.items.ClearGradientItem
import dev.dxnny.prism.gui.items.GradientItem
import dev.dxnny.prism.gui.items.MetaItems
import dev.dxnny.prism.manager.GradientManager.gradientMap
import dev.dxnny.prism.utils.GetItem.getGradientItem
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.window.Window
import xyz.xenondevs.invui.window.type.context.setTitle

object GradientMenu {

    private const val GUI_CONFIG_KEY = "gui"
    private val guiConfig = instance.configuration.getConfigurationSection(GUI_CONFIG_KEY)!!
    private val structure = guiConfig.getStringList("structure").toTypedArray()
    private val title = mmParse(guiConfig.getString("title", "Gradients")!!)
    private val guiTemplate = PagedGui.items()
        .setStructure(*structure)
        .addIngredient('#', MetaItems.FillerItem())
        .addIngredient('<', MetaItems.PageBackItem())
        .addIngredient('>', MetaItems.PageForwardItem())
        .addIngredient('!', ClearGradientItem())
        .addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL)

    fun open(player: Player) {
        val finalGui = guiTemplate.setContent(getGuiItems(player))

        Window.single()
            .setTitle(title)
            .setGui(finalGui)
            .build(player)
            .open()
    }

    private fun getGuiItems(player: Player): List<Item> {
        return gradientMap.values.mapNotNull { data ->
            if (data.hideFromGui) return@mapNotNull null
            val isUnlocked = player.hasPermission(data.permission)
            val gradientItem = getGradientItem(data, isUnlocked)
            if (gradientItem == null) {
                ConsoleLog.debug("Gradient item for ${data.identifier} ($isUnlocked) is null!")
                return@mapNotNull null
            }
            GradientItem(ItemBuilder(gradientItem), data)
        }
    }
}