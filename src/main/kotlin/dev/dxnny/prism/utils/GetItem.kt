package dev.dxnny.prism.utils

import dev.dxnny.infrastructure.utils.ConsoleLog
import dev.dxnny.infrastructure.utils.text.MessageUtils.mmParse
import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.objects.GradientData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.builder.setDisplayName

object GetItem {
    private val itemConfig = instance.configuration
    private val DEFAULT_AVAILABLE_LORE = itemConfig.getStringList("gui.gradient.lore-available")
    private val DEFAULT_LOCKED_LORE = itemConfig.getStringList("gui.gradient.lore-locked")
    private val DEFAULT_GRADIENT_MATERIAL = itemConfig.getString("gui.gradient.material")

    private fun createGradientItem(gradientData: GradientData, material: Material?, unlocked: Boolean): ItemStack? {
        if (material == null) {
            ConsoleLog.warning("Invalid material for ${gradientData.identifier}")
            return null
        }

        val loreList = getGradientLore(gradientData.identifier, unlocked)
        val itemStack = ItemStack.of(material).apply {
            itemMeta = itemMeta.apply {
                displayName(gradientData.gradientComponent.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                if (loreList.isNotEmpty()) lore(loreList)
            }
        }
        return itemStack
    }

    private fun getGradientLore(identifier: String, unlocked: Boolean): List<Component> {
        val state = if (unlocked) "available" else "locked"
        val default = if (unlocked) DEFAULT_AVAILABLE_LORE else DEFAULT_LOCKED_LORE
        val lore = itemConfig.getStringList("gradients.$identifier.lore-$state").takeIf { it.isNotEmpty() } ?: default

        return lore.map { mmParse(it.toString()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
    }

    fun getName(path: String) = itemConfig.getString("$path.name", null)

    fun getMaterial(path: String): Material? {
        val materialKey = itemConfig.getString("$path.material", DEFAULT_GRADIENT_MATERIAL)!!.uppercase()
        val material = Material.getMaterial(materialKey)
        if (material == null)
            ConsoleLog.warning("Invalid material for $path: $materialKey")
        return material
    }

    fun getGradientItem(data: GradientData, unlocked: Boolean): ItemStack? {
        return getMaterial("gradients.${data.identifier}")?.let {
             createGradientItem(data, it, unlocked)
        }
    }

    fun getGuiItem(identifier: String): ItemBuilder {
        val path = "gui.$identifier"
        return ItemBuilder(getMaterial(path)!!)
            .setDisplayName(mmParse(getName(path)!!))
    }
}