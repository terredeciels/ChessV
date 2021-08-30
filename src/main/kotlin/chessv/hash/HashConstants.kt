package chessv.hash

object HashConstants {
    const val PAWN_HASH_DEFAULT_SIZE = 32

    const val TRANSPOSITION_TABLE_SIZE = 256
    const val TRANSPOSITION_TABLE_BUCKET_SIZE = 4

    const val SCORE_TYPE_EXACT_SCORE = 0
    const val SCORE_TYPE_BOUND_LOWER = 1
    const val SCORE_TYPE_BOUND_UPPER = 2

    const val EMPTY_INFO = 0L
}