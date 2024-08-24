package dev.dxnny.prism.storage

import dev.dxnny.prism.Prism
import org.bukkit.entity.Player
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

class LiteManager(databasePath: String) {

    private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:$databasePath")
    private val playerGradients: MutableMap<UUID, String> = mutableMapOf()

    init {
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
        onlinePlayers.forEach { player ->
            loadPlayerGradient(player.uniqueId)
        }
    }

    // Add a player's data to the map by fetching from the database
    fun loadPlayerGradient(uuid: UUID) {
        val sql = "SELECT gradientId FROM player_gradients WHERE uuid = ?"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setString(1, uuid.toString())
            preparedStatement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    val gradientId = resultSet.getString("gradientId")
                    playerGradients[uuid] = gradientId
                }
            }
        }
    }

    // check if uuid is in the table
    fun playerGradientExists(uuid: UUID): Boolean {
        val sql = "SELECT COUNT(*) FROM player_gradients WHERE uuid = ?"
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.setString(1, uuid.toString())
            preparedStatement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0
                }
            }
        }
        return false
    }

    // Insert or update a player's gradient in the map
    fun insertOrUpdatePlayerGradient(uuid: UUID, gradientId: String) {
        playerGradients[uuid] = gradientId
    }

    // Get a player's gradient ID from the map
    fun getGradientId(uuid: UUID): String? {
        return playerGradients[uuid]
    }

    // Delete a player's gradient entry from the map
    fun deletePlayerGradient(uuid: UUID) {
        playerGradients.remove(uuid)
    }

    // Save all player gradients to the database
    fun saveAllPlayerGradients() {
        val sql = """
            INSERT INTO player_gradients (uuid, gradientId)
            VALUES (?, ?)
            ON CONFLICT(uuid) DO UPDATE SET gradientId = excluded.gradientId
        """.trimIndent()

        connection.prepareStatement(sql).use { preparedStatement ->
            playerGradients.forEach { (uuid, gradientId) ->
                preparedStatement.setString(1, uuid.toString())
                preparedStatement.setString(2, gradientId)
                preparedStatement.addBatch()
            }
            preparedStatement.executeBatch()
        }
    }

    // Saves a player's gradient to database
    fun savePlayerGradient(uuid: UUID, gradientId: String) {
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
    fun close(plugin: Prism) {
        try {
            plugin.logger.info("SQLite connection shutting down...")
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}