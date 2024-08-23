package dev.dxnny.prism.storage

import dev.dxnny.prism.Prism.Companion.instance
import dev.dxnny.prism.utils.ConsoleLog
import org.bukkit.entity.Player
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

class LiteManager(databasePath: String) {

    private val connection: Connection
    private val playerGradients: MutableMap<UUID, String> = mutableMapOf()

    init {
        connection = DriverManager.getConnection("jdbc:sqlite:$databasePath")
        createTable()
    }

    private fun createTable() {
        val sql = """
            CREATE TABLE IF NOT EXISTS player_gradients (
                uuid TEXT PRIMARY KEY,
                gradientId TEXT NOT NULL
            )
        """.trimIndent()

        connection.createStatement().use { statement ->
            statement.execute(sql)
        }
    }

    // Load all online players' gradients into the map
    fun loadAllOnlinePlayersGradients(onlinePlayers: Collection<Player>) {
        ConsoleLog.debug("Loading gradients for all online players...")
        onlinePlayers.forEach { player ->
            loadPlayerGradient(player.uniqueId)
        }
    }

    // Add a player's data to the map by fetching from the database
    fun loadPlayerGradient(uuid: UUID) {
        ConsoleLog.debug("Loading gradient for ${instance.server.getPlayer(uuid)?.name}...")
        val sql = "SELECT gradientId FROM player_gradients WHERE uuid = ?"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setString(1, uuid.toString())
            preparedStatement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    val gradientId = resultSet.getString("gradientId")
                    playerGradients[uuid] = gradientId
                    ConsoleLog.debug("Loaded gradient $gradientId for ${instance.server.getPlayer(uuid)?.name}")
                }
            }
        }
    }

    // Insert or update a player's gradient in the map
    fun insertOrUpdatePlayerGradient(uuid: UUID, gradientId: String) {
        ConsoleLog.debug("Set data for ${instance.server.getPlayer(uuid)?.name} to $gradientId")
        playerGradients[uuid] = gradientId
    }

    // Get a player's gradient ID from the map
    fun getGradientId(uuid: UUID): String? {
        return playerGradients[uuid]
    }

    // Delete a player's gradient entry from the map
    fun deletePlayerGradient(uuid: UUID) {
        ConsoleLog.debug("Removed data from map for ${instance.server.getPlayer(uuid)?.name}")
        playerGradients.remove(uuid)
    }

    // Save all player gradients to the database
    fun saveAllPlayerGradients() {
        ConsoleLog.debug("Saving data for all players...")
        val sql = """
            INSERT INTO player_gradients (uuid, gradientId)
            VALUES (?, ?)
            ON CONFLICT(uuid) DO UPDATE SET gradientId = excluded.gradientId
        """.trimIndent()

        connection.prepareStatement(sql).use { preparedStatement ->
            playerGradients.forEach { (uuid, gradientId) ->
                ConsoleLog.debug("Saving data for ${instance.server.getPlayer(uuid)?.name}...")
                preparedStatement.setString(1, uuid.toString())
                preparedStatement.setString(2, gradientId)
                preparedStatement.addBatch()
            }
            preparedStatement.executeBatch()
        }
    }

    // Saves a player's gradient to database
    fun savePlayerGradient(uuid: UUID, gradientId: String) {
        ConsoleLog.debug("Saving data for ${instance.server.getPlayer(uuid)?.name}...")
        val sql = """
            INSERT INTO player_gradients (uuid, gradientId)
            VALUES (?, ?)
            ON CONFLICT(uuid) DO UPDATE SET gradientId = excluded.gradientId
        """.trimIndent()

        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setString(1, uuid.toString())
            preparedStatement.setString(2, gradientId)
            preparedStatement.addBatch()
            preparedStatement.executeBatch()
        }
    }
    // Close the database connection
    fun close() {
        try {
            ConsoleLog.info("SQLite connection shutting down...")
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}