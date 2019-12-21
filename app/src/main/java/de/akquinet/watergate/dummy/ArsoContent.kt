package de.akquinet.watergate.dummy

import de.akquinet.watergate.ArsoRepository
import de.akquinet.watergate.datamodel.ArsoData
import java.util.*

object ArsoContent {

    var data: ArsoData? = createEmptyData()

    fun load(): ArsoData {
        val result = ArsoRepository.loadArsoData() ?: createEmptyData()
        data = result
        return result
    }

    private fun createEmptyData(): ArsoData {
        return ArsoData("0", "", "", "", Date(0), listOf())
    }
}
