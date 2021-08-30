package chessv.search

import chessv.board.Board
import chessv.game.GameConstants
import chessv.hash.HashConstants
import chessv.hash.TranspositionTable
import chessv.jvm.PlatformSpecific
import chessv.move.Move
import chessv.move.MoveGenerator


class SearchInfo(
    val transpositionTable: TranspositionTable,
    history: History
) {

    private val moveGenerator = MoveGenerator(history)
    var searchNodes = 0L
    val plyInfoList = Array(GameConstants.MAX_PLIES + 4) { SearchPlyInfo(moveGenerator) }

    fun reset() {
        searchNodes = 0
        for (info in plyInfoList) {
            info.clear()
        }
        PlatformSpecific.arrayFill(bestMoveList, 0)
        bestMove = Move.NONE
        bestScore = 0
    }

    var bestMove = Move.NONE
    private val bestMoveList = IntArray(PV_DEPTH)
    var bestScore = 0

    fun save(board: Board) {
        var ply = 0
        while (ply < PV_DEPTH) {
            val info = transpositionTable.findEntry(board)
            if (info == HashConstants.EMPTY_INFO) {
                break
            }
            val firstMove = transpositionTable.getMove(info)
            if (firstMove != Move.NONE) {
                if (ply == 0) {
                    bestMove = firstMove
                    bestScore = transpositionTable.getScore(info, ply)
                }
                bestMoveList[ply] = firstMove
                board.doMove(firstMove)
            } else {
                break
            }
            ply++
        }
        if (ply < PV_DEPTH) {
            bestMoveList[ply] = Move.NONE
        }
        while (ply > 0) {
            ply--
            board.undoMove(bestMoveList[ply])
        }
    }

    override fun toString(): String {
        val result = StringBuilder()
        for (move in bestMoveList) {
            if (move == Move.NONE) {
                break
            }
            result.append(Move.toString(move)).append(" ")
        }
        return result.toString()
    }

    companion object {
        private const val PV_DEPTH = 10
    }
}