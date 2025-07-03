package dev.dxnny.prism.files

import dev.dxnny.infrastructure.files.Messages.Companion.messagesConfig

class Lang {
    companion object {
        var prefix = "<gradient:#9cc2fd:#aa93ff:#e2b5fd:#fdadf8><b>GRADIENTS <reset><dark_grey>»</dark_grey> <white>"

        object CommandMessages {
            var applied = "<green>You applied the <gradient> gradient!"
            var cleared = "<grey>Your current gradient has been cleared."
            var alreadyActive = "<red>You are already using this gradient!"
            var appliedOther = "<green>Set <target>'s gradient to <gradient>."
            var clearedOther = "<green>Cleared <target>'s gradient."
            var reloaded = "<green>Reloaded successfully!"
        }

        object ErrorMessages {
            var noPermissionGradient = "<red>You don't have access to this gradient!"
            var noPermission = "<red>You do not have permission to do this!"
            var noGradientActive = "<red>You don't have any gradients applied"
            var notEnoughArgs = "<red>Incorrect number of arguments."
            var invalidGradient = "<red>This gradient does not exist!"
            var invalidPlayer = "<red>Invalid player"
        }

        object HelpMessages {
            var header = "<gray><b><st>⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯</st> <gradient:#9cc2fd:#aa93ff:#e2b5fd:#fdadf8>Gradient Commands</gradient> <gray><b><st>⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯<reset>"
            var footer = "<gray><b><st>⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯-<reset>"
            var syntaxColor = "<c:#6EB9FD>"
            var descColor = "<grey>"
        }

        fun updateMessages() {
            fun updateField(currentValue: String, configKey: String) = messagesConfig.getString(configKey) ?: currentValue

            prefix = updateField(prefix, "prefix")

            CommandMessages.applied = updateField(CommandMessages.applied, "commands.gradient.applied")
            CommandMessages.cleared = updateField(CommandMessages.cleared, "commands.gradient.cleared")
            CommandMessages.alreadyActive = updateField(CommandMessages.alreadyActive, "commands.gradient.alreadyActive")
            CommandMessages.appliedOther = updateField(CommandMessages.appliedOther, "commands.prism.appliedOther")
            CommandMessages.clearedOther = updateField(CommandMessages.clearedOther, "commands.prism.clearedOther")
            CommandMessages.reloaded = updateField(CommandMessages.reloaded, "commands.prism.reloaded")

            ErrorMessages.noPermissionGradient = updateField(ErrorMessages.noPermissionGradient, "errors.noPermissionGradient")
            ErrorMessages.noPermission = updateField(ErrorMessages.noPermission, "errors.noPermission")
            ErrorMessages.noGradientActive = updateField(ErrorMessages.noGradientActive, "errors.noGradientActive")
            ErrorMessages.notEnoughArgs = updateField(ErrorMessages.notEnoughArgs, "errors.notEnoughArgs")
            ErrorMessages.invalidGradient = updateField(ErrorMessages.invalidGradient, "errors.invalidGradient")
            ErrorMessages.invalidPlayer = updateField(ErrorMessages.invalidPlayer, "errors.invalidPlayer")

            HelpMessages.header = updateField(HelpMessages.header, "help.header")
            HelpMessages.syntaxColor = updateField(HelpMessages.syntaxColor, "help.colors.syntax")
            HelpMessages.descColor = updateField(HelpMessages.descColor, "help.colors.description")
            HelpMessages.footer = updateField(HelpMessages.footer, "help.footer")
        }
    }
}