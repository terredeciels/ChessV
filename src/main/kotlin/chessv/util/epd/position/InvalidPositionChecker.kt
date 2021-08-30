package chessv.util.epd.position

import chessv.board.Board
import chessv.board.factory.BoardFactory
import chessv.eval.AttackInfo
import chessv.move.MoveGenerator
import chessv.move.OrderedMoveList
import chessv.search.History

class InvalidPositionChecker {
    private val moveGenerator = MoveGenerator(History())

    private val board = Board()

    private val moveList = OrderedMoveList()
    private val attackInfo = AttackInfo()

    fun isValid(fenPosition: String): Boolean {
        moveList.reset()

        BoardFactory.setBoard(fenPosition, board)

        moveGenerator.generateQuiet(board, attackInfo, moveList)
        moveGenerator.generateNoisy(board, attackInfo, moveList)

        while (moveList.hasNext()) {
            if (board.isLegalMove(moveList.next())) {
                return true
            }
        }

        return false
    }
}