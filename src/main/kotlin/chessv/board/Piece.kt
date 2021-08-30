package chessv.board

object Piece {

    const val NONE = 0

    const val PAWN = 1
    const val KNIGHT = 2
    const val BISHOP = 3
    const val ROOK = 4
    const val QUEEN = 5
    const val KING = 6

    const val SIZE = 7

    private val CHARACTER = charArrayOf('-', 'P', 'N', 'B', 'R', 'Q', 'K', '+', 'p', 'n', 'b', 'r', 'q', 'k')

    fun getPiece(token: Char): Int {
        val indexOf = CHARACTER.indexOf(token)
        return if (indexOf <= NONE) {
            NONE
        } else indexOf.rem(SIZE)
    }

    fun getPieceColor(token: Char): Int {
        return if (CHARACTER.indexOf(token) < SIZE) Color.WHITE else Color.BLACK
    }

    fun toString(piece: Int): Char {
        return toString(Color.BLACK, piece)
    }

    fun toString(color: Int, piece: Int): Char {
        return CHARACTER[piece + SIZE * color]
    }

    fun isValidPiece(piece: Int): Boolean {
        return piece in PAWN until SIZE
    }

    fun isValid(piece: Int): Boolean {
        return piece in NONE until SIZE
    }
}
