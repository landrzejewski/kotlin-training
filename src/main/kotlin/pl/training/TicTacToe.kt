package pl.training

import pl.training.Player.CROSS
import java.util.Collections.disjoint

class TicTacToe(private var crossFields: MutableSet<Int> = mutableSetOf(), private var circleFields: MutableSet<Int> = mutableSetOf(), player: Player = CROSS) {

    var player = player
        private set
    val isGameOver: Boolean
        get() = allFieldsAreTake() || playerTookWinningSequence()
    val winner: Player?
        get() = getFinalWinner()

    init {
        if (!disjoint(crossFields, circleFields)) {
            throw IllegalArgumentException()
        }
    }

    private fun allFieldsAreTake() = BOARD_SIZE - crossFields.size - circleFields.size <= 0

    private fun playerTookWinningSequence() = winningSequences.any { playerFields().containsAll(it) }

    fun makeMove(field: Int): Boolean {
        if (!isOnBoard(field) || isTaken(field)) return false
        playerFields().add(field)
        if (!isGameOver) {
            player = player.reverse()
        }
        return true
    }

    private fun playerFields() = if (player == CROSS) crossFields else circleFields

    private fun isOnBoard(field: Int) = field in 1..9

    private fun isTaken(field: Int) = field in crossFields.union(circleFields)

    private fun getFinalWinner() = if (isGameOver) player else null

    companion object {

        private const val BOARD_SIZE = 9

        private val winningSequences = setOf(
            setOf(1, 2, 3), setOf(4, 5, 6), setOf(7, 8, 9),
            setOf(1, 4, 7), setOf(2, 5, 8), setOf(3, 6, 9),
            setOf(1, 5, 9), setOf(3, 5, 7)
        )

    }

}
