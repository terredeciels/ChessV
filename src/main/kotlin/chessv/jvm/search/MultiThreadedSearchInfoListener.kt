package chessv.jvm.search

import chessv.search.SearchInfo
import chessv.search.SearchInfoListener
import chessv.uci.UciOutput

class MultiThreadedSearchInfoListener : SearchInfoListener {

    override fun searchInfo(depth: Int, elapsedTime: Long, searchInfo: SearchInfo) {
        UciOutput.searchInfo(depth, elapsedTime, MultiThreadedSearch.countNodes(), searchInfo)
    }

    override fun bestMove(searchInfo: SearchInfo) {
        UciOutput.hashfullInfo(searchInfo)
        UciOutput.bestMove(searchInfo.bestMove)
    }
}
