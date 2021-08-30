package chessv.jvm

import chessv.board.*
import chessv.board.factory.BoardFactory
import chessv.board.factory.FenFactory
import chessv.eval.*
import chessv.game.GameConstants
import chessv.hash.HashConstants
import chessv.hash.Zobrist
import chessv.jvm.search.MultiThreadedSearch
import chessv.move.BitboardMove
import chessv.move.Move
import chessv.move.MoveType
import chessv.tuning.TunableConstants
import chessv.uci.UciInput
import chessv.util.Perft
import chessv.util.SplitValue
import chessv.util.XorShiftRandom
import chessv.jvm.uci.InputHandler


import kotlin.system.measureNanoTime

fun main(args: Array<String>) {
    initialize()
    var threads = "1"
    if (args.isNotEmpty()) {
        when (args[0]) {
//            "bench" -> {
//                val benchDepth = if (args.size > 1) {
//                    Integer.parseInt(args[1])
//                } else {
//                    Benchmark.DEFAULT_BENCHMARK_DEPTH
//                }
//                Benchmark.runBenchmark(depth = benchDepth)
//                return
//            }
            "threads" -> {
                if (args.size > 1) {
                    threads = args[1]
                }
            }
            else -> {
                println("Unknown argument")
            }
        }
    }

    val inputHandler = InputHandler()
    val uciInput = UciInput(inputHandler)
    inputHandler.setOption("threads", threads)
    while (true) {
        try {
            val line = readLine()
            line ?: return
            uciInput.process(line)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

/**
 * Pre load needed classes.
 */
fun initialize() {
    val nanoTime = measureNanoTime {
        BoardFactory
        FenFactory
        Bitboard
        BoardUtil
        CastlingRights
        Color
        File
        Piece
        Rank
        Square
        DrawEvaluator
        EvalConstants
        Evaluator
        PawnEvaluator
        StaticExchangeEvaluator
        GameConstants
        HashConstants
        Zobrist
        BitboardMove
        Move
        MoveType
        TunableConstants
        Perft
        SplitValue
        XorShiftRandom
        MultiThreadedSearch
    }
    val msTime = nanoTime / 1_000_000
    println("Initialized in $msTime ms")
}
