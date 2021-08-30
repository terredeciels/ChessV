package chessv.jvm

import chessv.tuning.TunableConstants
import chessv.uci.UciOutput
import java.util.*
import kotlin.system.exitProcess

 object PlatformSpecific {

     fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

     fun numberOfTrailingZeros(value: Long): Int {
        return java.lang.Long.numberOfTrailingZeros(value)
    }

     fun numberOfTrailingZeros(value: Int): Int {
        return java.lang.Integer.numberOfTrailingZeros(value)
    }

     fun bitCount(value: Long): Int {
        return java.lang.Long.bitCount(value)
    }

     fun reverseBytes(value: Long): Long {
        return java.lang.Long.reverseBytes(value)
    }

     fun arraySort(array: IntArray, start: Int, end: Int) {
        Arrays.sort(array, start, end)
    }

     fun arrayFill(array: ShortArray, value: Short) {
        Arrays.fill(array, value)
    }

     fun arrayFill(array: IntArray, value: Int) {
        Arrays.fill(array, value)
    }

     fun arrayFill(array: LongArray, value: Long) {
        Arrays.fill(array, value)
    }

     fun arrayFill(array: Array<IntArray>, value: Int) {
        array.forEach { arrayFill(it, value) }
    }

     fun arrayFill(array: Array<Array<IntArray>>, value: Int) {
        array.forEach { arrayFill(it, value) }
    }

     fun arrayCopy(src: IntArray, srcPos: Int, dest: IntArray, destPos: Int, length: Int) {
        System.arraycopy(src, srcPos, dest, destPos, length)
    }

     fun arrayCopy(src: Array<IntArray>, dest: Array<IntArray>) {
        for (index in src.indices) {
            arrayCopy(src[index], 0, dest[index], 0, src[index].size)
        }
    }

     fun arrayCopy(src: LongArray, srcPos: Int, dest: LongArray, destPos: Int, length: Int) {
        System.arraycopy(src, srcPos, dest, destPos, length)
    }

     fun arrayCopy(src: Array<LongArray>, dest: Array<LongArray>) {
        for (index in src.indices) {
            arrayCopy(src[index], 0, dest[index], 0, src[index].size)
        }
    }

     fun formatString(source: String, vararg args: Any): String {
        return String.format(source, args)
    }

     fun applyConfig(option: String, value: Int) {
        UciOutput.println("Setting $option with value $value")
        val optionList = option.split('-')

        val field = TunableConstants::class.java.getDeclaredField(optionList[0].toUpperCase())
        var constant: IntArray? = null
        when (optionList.size) {
            1 -> {
                field.isAccessible = true
                field.set(TunableConstants::class.java, value)
                println("Result " + field.get(TunableConstants::class.java))
            }
            2 -> {
                field.isAccessible = true
                constant = field.get(null) as IntArray
            }
            3 -> {
                field.isAccessible = true
                val arrayConstant = field.get(null) as Array<IntArray>
                constant = arrayConstant[optionList[1].toInt()]
            }
            4 -> {
                field.isAccessible = true
                val arrayConstant = field.get(null) as Array<Array<IntArray>>
                constant = arrayConstant[optionList[1].toInt()][optionList[2].toInt()]
            }
        }
        if (constant != null) {
            updateArray(constant, optionList[optionList.size - 1].toInt(), value)
            UciOutput.println("Result ${constant.toList()}")
        }
    }

    private fun updateArray(array: IntArray, position: Int, value: Int) {
        array[position] = value
    }

     fun exit(code: Int) {
        exitProcess(code)
    }

     fun gc() {
        System.gc()
    }

     fun getVersion(): String {
        var version: String? = null
        val pkg = PlatformSpecific::class.java.`package`
        if (pkg != null) {
            version = pkg.implementationVersion
            if (version == null) {
                version = pkg.specificationVersion
            }
        }
        version = if (version == null) "" else version.trim { it <= ' ' }
        return if (version.isEmpty()) "v?" else version
    }
}