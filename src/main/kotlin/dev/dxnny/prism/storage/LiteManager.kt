package dev.dxnny.prism.storage

import dev.dxnny.infrastructure.utils.ConsoleLog.logMessage
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

    // Mark a player's gradient entry as deleted in the map, so that it can be removed from table when saving
    fun clearPlayerGradient(uuid: UUID) {
        playerGradients[uuid] = "null"
    }

    // Save all player gradients to the database
    fun saveAllPlayerGradients() {
        val insertOrUpdateSql = """
        INSERT INTO player_gradients (uuid, gradientId)
        VALUES (?, ?)
        ON CONFLICT(uuid) DO UPDATE SET gradientId = excluded.gradientId
        """.trimIndent()

        val deleteSql = "DELETE FROM player_gradients WHERE uuid = ?"

        connection.prepareStatement(insertOrUpdateSql).use { insertStatement ->
            connection.prepareStatement(deleteSql).use { deleteStatement ->
                playerGradients.forEach { (uuid, gradientId) ->
                    if (gradientId == "null") {
                        deleteStatement.setString(1, uuid.toString())
                        deleteStatement.addBatch()
                    } else {
                        insertStatement.setString(1, uuid.toString())
                        insertStatement.setString(2, gradientId)
                        insertStatement.addBatch()
                    }
                }
                deleteStatement.executeBatch()
                insertStatement.executeBatch()
            }
        }
    }

    // Saves a player's gradient to database
    fun savePlayerGradient(uuid: UUID, gradientId: String) {
        if (gradientId == "null") {
            val deleteSql = "DELETE FROM player_gradients WHERE uuid = ?"
            connection.prepareStatement(deleteSql).use { preparedStatement ->
                preparedStatement.setString(1, uuid.toString())
                preparedStatement.executeUpdate()
            }
        } else {
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
    }
    // Close the database connection
    fun close() {
        try {
            logMessage("&7SQLite connection shutting down...")
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}