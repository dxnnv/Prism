package dev.dxnny.prism.utils

object ArraySplitter {
    fun <T> splitIntoGroups(array: Array<T>, groupSize: Int): List<Array<T>> {
        if (groupSize <= 0) throw IllegalArgumentException("Group size must be greater than 0")

        val result = mutableListOf<Array<T>>()
        var currentIndex = 0

        while (currentIndex < array.size) {
            val nextIndex = (currentIndex + groupSize).coerceAtMost(array.size)
            result.add(array.copyOfRange(currentIndex, nextIndex))
            currentIndex = nextIndex
        }

        return result
    }

}