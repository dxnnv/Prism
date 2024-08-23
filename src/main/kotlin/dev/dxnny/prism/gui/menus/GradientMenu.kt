package dev.dxnny.prism.gui.menus

import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.gui.items.*
import dev.dxnny.prism.utils.ArraySplitter
import dev.dxnny.prism.utils.GetItem
import dev.dxnny.prism.utils.gradients.GradientManager
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.window.Window

class GradientMenu {

    companion object {
        fun open(player: Player) {
            val gradients: ConfigurationSection = instance.config.getConfigurationSection("gradients")!!
            val guiConfig: ConfigurationSection = instance.config.getConfigurationSection("gui")!!
            val perPage = guiConfig.getInt("per-page")

            val allGradients = gradients.getKeys(false).toTypedArray()
            val gradientPages = ArraySplitter.splitIntoGroups(allGradients, perPage)

            // make pages
            val guiMap = mutableMapOf<Int, Gui>()
            val items = mutableListOf<Item>()

            gradientPages.forEachIndexed { index, page ->
                val gui = Gui.normal()
                    .setStructure(*guiConfig.getStringList("structure").toTypedArray())
                    .addIngredient('#', FillerItem())
                    .addIngredient('<', PageBackItem())
                    .addIngredient('>', PageForwardItem())
                    .addIngredient('!', ClearGradientItem())
                page.forEach { gradientId ->
                    val unlocked = GradientManager.hasGradientPermission(player, gradientId)
                    val gradientItem = GetItem.getItem("gradients.$gradientId", unlocked)
                    val invuiItem: Item = GUIItem(ItemBuilder(gradientItem!!), gradientId)
                    items.add(invuiItem)
                }

                gui.build().addItems(*items.toTypedArray())
                val builtGui = gui.build()
                guiMap[index + 1] = builtGui
            }

            // make gui
            val gui = PagedGui.items()
                .setStructure(*guiConfig.getStringList("structure").toTypedArray())
                .addIngredient('#', FillerItem())
                .addIngredient('<', PageBackItem())
                .addIngredient('>', PageForwardItem())
                .addIngredient('!', ClearGradientItem())
                .addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .setContent(items)
                .build()
            // create window
            val window: Window = Window.single()
                .setTitle(guiConfig.getString("title")!!)
                .setGui(gui)
                .build(player)
            window.open()
        }
    }

}