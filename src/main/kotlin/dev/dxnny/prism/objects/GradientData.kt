package dev.dxnny.prism.objects

import dev.dxnny.infrastructure.utils.text.MessageUtils.mmParse
import net.kyori.adventure.text.Component

data class GradientData(
    val identifier: String,
    val name: String,
    val permission: String,
    val gradientColor: String,
    val gradientString: String = "$gradientColor$name<reset>",
    val gradientComponent: Component = mmParse(gradientString),
    val hideFromGui: Boolean = false,
    val loreAvailable: List<String>,
    val loreLocked: List<String>,
)
