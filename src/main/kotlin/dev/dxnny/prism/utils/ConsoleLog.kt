package dev.dxnny.prism.utils

import dev.dxnny.prism.Prism.Companion.instance
import java.util.logging.Level
import java.util.logging.Logger

@Suppress("unused")
object ConsoleLog {
    private val logger: Logger = instance.logger

    fun info(msg: String?) {
        logger.info(msg)
    }

    fun warn(msg: String?) {
        logger.warning(msg)
    }

    fun severe(msg: String?) {
        logger.severe(msg)
    }

    fun logExp(level: Level?, msg: String?, throwable: Throwable?) {
        logger.log(level, msg, throwable)
    }
}