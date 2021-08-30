package chessv.search


import chessv.board.Board
import chessv.board.Piece
import chessv.cache.PawnEvaluationCache
import chessv.eval.DrawEvaluator
import chessv.eval.EvalConstants
import chessv.eval.Evaluator
import chessv.game.GameConstants
import chessv.move.Move
import chessv.move.MoveType
import chessv.tuning.TunableConstants
import kotlin.math.max

/**
 * https://chessprogramming.wikispaces.com/Quiescence+Search
 */
class QuiescenceSearch(private val searchInfo: SearchInfo, private val pawnEvaluationCache: PawnEvaluationCache) {

    fun search(
        board: Board,
        ply: Int,
        alpha: Int,
        beta: Int
    ): Int {
        searchInfo.searchNodes++

        val currentNode = searchInfo.plyInfoList[ply]
        val eval = GameConstants.COLOR_FACTOR[board.colorToMove] * Evaluator.evaluate(
            board,
            currentNode.attackInfo,
            pawnEvaluationCache
        )

        if (eval >= beta) {
            return eval
        }

        var bestScore = max(alpha, eval)

        val futilityQueenValue = eval + TunableConstants.QS_FUTILITY_VALUE[Piece.QUEEN]
        if (futilityQueenValue <= bestScore) {
            return futilityQueenValue
        }

        if (ply >= GameConstants.MAX_PLIES) {
            return bestScore
        }
        currentNode.setupMovePicker(board, 1)

        var moveCount = 0

        while (true) {
            val move = currentNode.next(true)
            if (move == Move.NONE) {
                break
            }
            if (!board.isLegalMove(move)) {
                continue
            }
            moveCount++

            val moveType = Move.getMoveType(move)

            if (MoveType.isPromotion(moveType) && moveType != MoveType.TYPE_PROMOTION_QUEEN) {
                continue
            }

            // Qsearch Futility
            val futilityValue = eval + getMoveValue(board, Move.getToSquare(move), moveType)
            if (futilityValue <= bestScore) {
                continue
            }

            board.doMove(move)
            val innerScore = if (!DrawEvaluator.hasSufficientMaterial(board)) {
                EvalConstants.SCORE_DRAW
            } else {
                -search(board, ply + 1, -beta, -bestScore)
            }
            board.undoMove(move)

            if (innerScore > bestScore) {
                bestScore = innerScore
            }
            if (innerScore >= beta) {
                break
            }
        }
        return bestScore
    }

    private fun getMoveValue(board: Board, toSquare: Int, moveType: Int): Int {
        return when {
            moveType == MoveType.TYPE_PASSANT -> {
                TunableConstants.QS_FUTILITY_VALUE[Piece.PAWN]
            }
            MoveType.isPromotion(moveType) -> {
                TunableConstants.QS_FUTILITY_VALUE[board.pieceTypeBoard[toSquare]] -
                    TunableConstants.QS_FUTILITY_VALUE[Piece.PAWN] +
                    TunableConstants.QS_FUTILITY_VALUE[MoveType.getPromotedPiece(moveType)]
            }
            else -> {
                TunableConstants.QS_FUTILITY_VALUE[board.pieceTypeBoard[toSquare]]
            }
        }
    }
}