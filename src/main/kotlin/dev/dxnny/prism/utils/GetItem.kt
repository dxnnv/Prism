package dev.dxnny.prism.utils

import dev.dxnny.infrastructure.utils.text.MessageUtils.mmParse
import dev.dxnny.prism.Prism.Companion.instance
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.inventory.ItemStack

object GetItem {

    private val plugin get() = instance
    private val itemConfig: FileConfiguration
        get() = plugin.config

    fun getItem(itemPath: String, unlocked: Boolean): ItemStack? {
        val state = if (unlocked) "available" else "locked"
        if (itemPath.startsWith("gradients.")) {
            if (itemConfig.getString("${itemPath}.name") != null) {
                val gradientEntry: String? = if (itemConfig.getString("${itemPath}.gui-item") != null) {
                    itemConfig.getString("${itemPath}.gui-item")?.uppercase()
                } else {
                    itemConfig.getString("gui.gradient.material")
                }

                val material = Material.getMaterial(gradientEntry!!)!!
                val customItem = ItemStack(material)
                val itemMeta = customItem.itemMeta

                val fullGradientName = GradientManager.getGradientComponent(itemPath.removePrefix("gradients.")).decoration(TextDecoration.ITALIC, false)
                itemMeta.displayName(fullGradientName)

                val loreList = itemConfig.getList("$itemPath.lore-$state") ?: itemConfig.getStringList("gui.gradient.lore-$state")
                if (loreList.isNotEmpty()) {
                    val itemLore: MutableList<Component> = mutableListOf()
                    loreList.forEach {
                        itemLore.add(mmParse(it.toString()).decoration(TextDecoration.ITALIC, false))
                    }
                    itemMeta.lore(itemLore)
                }
                customItem.setItemMeta(itemMeta)
                return customItem
            }
        } else if (itemPath.startsWith("gui.")) {
            if (itemConfig.getString("${itemPath}.name") != null) {
                val itemEntry = itemConfig.getString("${itemPath}.material")!!.uppercase()
                val itemName: String? = itemConfig.getString("${itemPath}.name")

                val material = Material.valueOf(itemEntry)
                val customItem = ItemStack(material)
                val itemMeta = customItem.itemMeta

                itemMeta.displayName(mmParse(itemName!!))

                val loreList = itemConfig.getStringList("$itemPath.lore-$state")
                if (loreList.isNotEmpty()) {
                    val itemLore: MutableList<Component> = mutableListOf()
                    loreList.forEach {
                        itemLore.add(mmParse(it.toString()).decoration(TextDecoration.ITALIC, false))
                    }
                    itemMeta.lore(itemLore)
                }
                customItem.setItemMeta(itemMeta)
                return customItem
            }
        }
        return null
    }
}