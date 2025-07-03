package dev.dxnny.prism.manager

import dev.dxnny.infrastructure.utils.ConsoleLog
import dev.dxnny.prism.Prism.Companion.storage
import dev.dxnny.prism.manager.GradientManager.gradientMap
import dev.dxnny.prism.objects.GradientData
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.UUID

private const val QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS player_gradients (uuid TEXT PRIMARY KEY, gradientId TEXT NOT NULL)"
private const val QUERY_DELETE = "DELETE FROM player_gradients WHERE uuid = ?"
private const val QUERY_INSERT_OR_UPDATE = "INSERT INTO player_gradients (uuid, gradientId)VALUES (?, ?)ON CONFLICT(uuid) DO UPDATE SET gradientId = excluded.gradientId"
private const val SQL_LOAD_PLAYER = "SELECT gradientId FROM player_gradients WHERE uuid = ?"
private const val SQL_CHECK_PLAYER_DATA = "SELECT COUNT(*) FROM player_gradients WHERE uuid = ?"

class StorageManager(databasePath: String) {
    private val connection = DriverManager.getConnection("jdbc:sqlite:$databasePath")

    companion object {
        val playerGradients = mutableMapOf<UUID, GradientData?>()
    }

    init {
        connection.createStatement().use { it.execute(QUERY_CREATE_TABLE) }
    }

    /**
     * Updates the gradient data for the specified player's uuid in the local cache.
     * If the provided gradient data is `null`, the player's data is cleared.
     *
     * @param uuid The [UUID] of the player whose gradient data is being updated.
     * @param gradientData The new [GradientData] to associate with the player, or `null` to clear it.
     */
    fun updatePlayer(uuid: UUID, gradientData: GradientData?) {
        playerGradients[uuid] = gradientData
    }

    /**
     * Loads the gradient data associated with the specified player's UUID from the database
     * and updates the local cache within [playerGradients] if data is found.
     *
     * @param uuid The UUID of the player whose gradient data should be loaded.
     */
    fun loadPlayer(uuid: UUID) {
        executeQuery(SQL_LOAD_PLAYER) {
            setString(1, uuid.toString())
        }.use { resultSet ->
            if (resultSet.next())
                playerGradients[uuid] = gradientMap[resultSet.getString("gradientId")]
        }
    }

    /**
     * Checks if the database contains the player's UUID.
     *
     * @param uuid The UUID of the player to check for.
     * @return `true` if the database contains the [uuid], `false` otherwise.
     */
    fun hasData(uuid: UUID): Boolean {
        executeQuery(SQL_CHECK_PLAYER_DATA) {
            setString(1, uuid.toString())
        }.use { resultSet ->
            if (resultSet.next())
                return resultSet.getInt(1) > 0
        }
        return false

    }

    // Save all player gradients to the database
    fun saveAllPlayers() {
        connection.prepareStatement(QUERY_INSERT_OR_UPDATE).use { insertStatement ->
            connection.prepareStatement(QUERY_DELETE).use { deleteStatement ->
                playerGradients.forEach { (uuid, gradientData) ->
                    if (gradientData == null) {
                        deleteStatement.setString(1, uuid.toString())
                        deleteStatement.addBatch()
                    } else {
                        insertStatement.setString(1, uuid.toString())
                        insertStatement.setString(2, gradientData.identifier)
                        insertStatement.addBatch()
                    }
                }
                deleteStatement.executeBatch()
                insertStatement.executeBatch()
            }
        }
    }

    /**
     * Saves the player's gradient to the database, identified by their UUID.
     * If the provided gradient ID is `null`, the row matching the [uuid] will be removed.
     * Otherwise, the gradient data is inserted or updated with the specified [gradientData].
     *
     * @param uuid The UUID of the player whose data should be saved or removed.
     * @param gradientData The data of the gradient to save for the player, or `null` to remove the player's data.
     */
    fun savePlayer(uuid: UUID, gradientData: GradientData?) {
        if (gradientData == null) {
            executeUpdate(QUERY_DELETE) {
                setString(1, uuid.toString())
            }
        } else {
            executeUpdate(QUERY_INSERT_OR_UPDATE) {
                setString(1, uuid.toString())
                setString(2, gradientData.identifier)
            }
        }
    }

    /**
     * Saves all active gradients for each player defined
     * in the [playerGradients] map, then closes the
     * [StorageManager]'s [connection].
     */
    fun close() {
        try {
            ConsoleLog.info("Saving all players...")
            storage.saveAllPlayers()
            connection.close()
            ConsoleLog.info("Saved all players!")
        } catch (e: SQLException) {
            ConsoleLog.fatal("Error during database save: ${e.message}", e)
        }
    }

    private fun executeQuery(sql: String, parameterSetter: PreparedStatement.() -> Unit): ResultSet {
        val preparedStatement = connection.prepareStatement(sql)
        preparedStatement.parameterSetter()
        return preparedStatement.executeQuery()
    }

    private fun executeUpdate(sql: String, parameterSetter: PreparedStatement.() -> Unit) {
        connection.prepareStatement(sql).use { preparedStatement ->
            preparedStatement.parameterSetter()
            preparedStatement.executeUpdate()
        }
    }
}