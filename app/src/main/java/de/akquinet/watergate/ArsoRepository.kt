package de.akquinet.watergate

import android.annotation.SuppressLint
import android.util.Log
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.akquinet.watergate.datamodel.ArsoData
import java.text.SimpleDateFormat

object ArsoRepository {

    @SuppressLint("SimpleDateFormat")
    private val mapper = XmlMapper()
        .registerKotlinModule()
        .setDateFormat(SimpleDateFormat("yyyy-MM-dd HH:mm"))

    fun loadArsoData(): ArsoData? {
        return try {
            val response = khttp.get(
                url = "http://www.arso.gov.si/xml/vode/hidro_podatki_zadnji.xml",
                headers = mapOf("Accept" to "application/xml")
            )
            if (response.statusCode == 200) {
                Log.i(this.javaClass.name, "New data downloaded")
                val arsoData = mapper.readValue(response.content, ArsoData::class.java)
                Log.i(this.javaClass.name, "Arso data deserialized")
                arsoData
            } else {
                Log.i(this.javaClass.name, "Data could not be loaded, status code: ${response.statusCode}")
                null
            }
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "Data could not be loaded, exception occurred", e)
            null
        }
    }
}