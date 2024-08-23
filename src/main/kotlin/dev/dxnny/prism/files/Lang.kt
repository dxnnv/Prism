package dev.dxnny.prism.files

import dev.dxnny.prism.Prism

class Lang {
    companion object{
        // prefix
        lateinit var prefix: String

        // command output
        lateinit var gradientApplied: String
        lateinit var gradientCleared: String
        lateinit var gradientAlreadyActive: String
        lateinit var gradientAppliedOther: String
        lateinit var gradientClearedOther: String

        // reload
        lateinit var configReloaded: String

        // errors
        lateinit var noPermissionGradient: String
        lateinit var noPermission: String
        lateinit var noGradientActive: String
        lateinit var notEnoughArgs: String
        lateinit var gradientNonExistent: String
        lateinit var invalidPlayer: String

        // help
        lateinit var helpHeader: String
        lateinit var helpSyntaxColor: String
        lateinit var helpDescColor: String
        lateinit var helpFooter: String

        fun updateMessages(plugin: Prism) {
            prefix = plugin.getMessagesFile().getString("prefix") ?: "<gradient:#FF0000:#FF7000><b>GRADIENTS <reset><dark_grey>≫</dark_grey> <white>"

            gradientApplied = plugin.getMessagesFile().getString("gradientApplied") ?: "<green>You applied the <gradient> gradient!"
            gradientCleared = plugin.getMessagesFile().getString("gradientCleared") ?: "<grey>Your current gradient has been cleared."
            gradientAlreadyActive = plugin.getMessagesFile().getString("gradientAlreadyActive") ?: "<red>You are already using this gradient!"
            gradientAppliedOther = plugin.getMessagesFile().getString("gradientAppliedOther") ?: "<green>Set <target>'s gradient to <gradient>."
            gradientClearedOther = plugin.getMessagesFile().getString("gradientClearedOther") ?: "<green>Cleared <target>'s gradient."

            configReloaded = plugin.getMessagesFile().getString("configReloaded") ?: "<green>Reloaded successfully!"

            noPermissionGradient = plugin.getMessagesFile().getString("noPermissionGradient") ?: "<red>You don't have access to this gradient!"
            noPermission = plugin.getMessagesFile().getString("noPermission") ?: "<red>You do not have permission to do this!"
            noGradientActive = plugin.getMessagesFile().getString("noGradientActive") ?: "<red>You don't have any gradients applied"
            notEnoughArgs = plugin.getMessagesFile().getString("notEnoughArgs") ?: "<red>Incorrect number of arguments."
            gradientNonExistent = plugin.getMessagesFile().getString("gradientNonExistent") ?: "<red>This gradient does not exist!"
            invalidPlayer = plugin.getMessagesFile().getString("invalidPlayer") ?: "<red>Invalid player"

            helpHeader = plugin.getMessagesFile().getString("helpHeader") ?: "<gray><b><st>⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯</st> <gradient:#5E00D6:#9D00FD:#5E00D6>Toolbox Commands<reset> <gray><b><st>⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯<reset>"
            helpSyntaxColor = plugin.getMessagesFile().getString("helpSyntaxColor") ?: "<c:#f7bb2b>"
            helpDescColor = plugin.getMessagesFile().getString("helpDescColor") ?: "<grey>"
            helpFooter = plugin.getMessagesFile().getString("helpFooter") ?: "<gray><b><st>⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯<reset>"
        }
    }
}