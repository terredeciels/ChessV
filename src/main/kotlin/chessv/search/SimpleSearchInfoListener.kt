package chessv.search

import chessv.uci.UciOutput


class SimpleSearchInfoListener : SearchInfoListener {

    override fun searchInfo(depth: Int, elapsedTime: Long, searchInfo: SearchInfo) {
        UciOutput.searchInfo(depth, elapsedTime, searchInfo.searchNodes, searchInfo)
    }

    override fun bestMove(searchInfo: SearchInfo) {
        UciOutput.hashfullInfo(searchInfo)
        UciOutput.bestMove(searchInfo.bestMove)
    }
}