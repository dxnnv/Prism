package dev.dxnny.prism.gui.items

import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.utils.text.MessageUtils.mmParse
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.builder.setDisplayName
import xyz.xenondevs.invui.item.impl.SimpleItem

class FillerItem : SimpleItem(ItemStack(Material.getMaterial(instance.getConfiguration().get()!!.getConfigurationSection("gui.filler")!!.get("material").toString())!!)) {
    private val fillerGradientConfig: ConfigurationSection = instance.getConfiguration().get()!!.getConfigurationSection("gui.filler")!!
    override fun getItemProvider(): ItemProvider {
        val builder = ItemBuilder(Material.getMaterial(fillerGradientConfig.get("material").toString())!!)
            .setDisplayName(mmParse(fillerGradientConfig.getString("name")!!))
        return builder
    }
}