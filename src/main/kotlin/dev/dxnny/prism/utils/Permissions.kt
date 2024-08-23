package dev.dxnny.prism.utils

enum class Permissions(val perm: String) {

    // WILDCARD PERMISSION
    WILDCARD("prism.*"),

    // PRISM SUBCOMMANDS
    PRISM_ADMIN("prism.admin"),

    // GRADIENT COMMAND
    GRADIENT_GUI("prism.use")

}