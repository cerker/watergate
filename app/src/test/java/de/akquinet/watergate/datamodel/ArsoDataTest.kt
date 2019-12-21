package de.akquinet.watergate.datamodel

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat


class ArsoDataTest {

    private val mapper = XmlMapper()
        .registerKotlinModule()
        .setDateFormat(SimpleDateFormat("yyyy-MM-dd HH:mm"))

    @Test
    fun `unmarshalls all 177 stations with base data`() {
        val xml = loadFile("/arso_data.xml")

        val arsoData = mapper.readValue(xml, ArsoData::class.java)

        assertThat(arsoData.stations).hasSize(177)
        assertThat(arsoData.proposedTimeOfUpdate).isEqualTo("5 minut čez polno uro ali pol ure")
        assertThat(arsoData.proposedPeriodOfUpdate).isEqualTo("30 min")
        assertThat(arsoData.source).isEqualTo("Agencija RS za okolje")
        assertThat(arsoData.timestampOfProvision).isEqualTo("2019-12-07T16:31:00")
        assertThat(arsoData.version).isEqualTo("1.3")
    }

    @Test
    fun `unmarshalls first station`() {
        val xml = loadFile("/arso_data.xml")

        val arsoData = mapper.readValue(xml, ArsoData::class.java)

        val station = arsoData.stations[0]
        assertThat(station.flowRate).isEqualTo(146.0)
        assertThat(station.flowRateText).isEqualTo("srednji pretok")
        assertThat(station.heigthAboveSeaLevel).isEqualTo(202.34)
        assertThat(station.highWaterThreshold1).isNull()
        assertThat(station.highWaterThreshold2).isNull()
        assertThat(station.highWaterThreshold3).isNull()
        assertThat(station.id).isEqualTo(1060)
        assertThat(station.latitude).isEqualTo(46.68151)
        assertThat(station.longitude).isEqualTo(16.000253)
        assertThat(station.shortName).isEqualTo("Mura - Gor. Radgona")
        assertThat(station.stationName).isEqualTo("Gornja Radgona")
        assertThat(station.timestamp).isEqualTo("2019-12-07T16:00:00")
        assertThat(station.waterLevel).isEqualTo(110)
        assertThat(station.waterTemperature).isEqualTo(4.4)
        assertThat(station.waterbody).isEqualTo("Mura")
        assertThat(station.flowRateThreshold1).isEqualTo(600.0)
        assertThat(station.flowRateThreshold2).isEqualTo(905.0)
        assertThat(station.flowRateThreshold3).isEqualTo(1180.0)
        assertThat(station.typicalWaveHeight).isNull()
        assertThat(station.directionOfWaves).isNull()

    }

    @Test
    fun `unmarshalls typical sea station`() {
        val xml = loadFile("/arso_data.xml")

        val arsoData = mapper.readValue(xml, ArsoData::class.java)

        val station = arsoData.stations[175]
        assertThat(station.flowRate).isNull()
        assertThat(station.flowRateText).isNull()
        assertThat(station.heigthAboveSeaLevel).isEqualTo(-2.09)
        assertThat(station.highWaterThreshold1).isEqualTo(300.0)
        assertThat(station.highWaterThreshold2).isEqualTo(330.0)
        assertThat(station.highWaterThreshold3).isEqualTo(350.0)
        assertThat(station.id).isEqualTo(9350)
        assertThat(station.latitude).isEqualTo(45.54839)
        assertThat(station.longitude).isEqualTo(13.72915)
        assertThat(station.shortName).isEqualTo("Jadransko morje - Koper")
        assertThat(station.stationName).isEqualTo("Koper - kapitanija")
        assertThat(station.timestamp).isEqualTo("2019-12-07T16:00:00")
        assertThat(station.waterLevel).isEqualTo(213)
        assertThat(station.waterTemperature).isEqualTo(14.9)
        assertThat(station.waterbody).isEqualTo("Jadransko morje")
        assertThat(station.flowRateThreshold1).isNull()
        assertThat(station.flowRateThreshold2).isNull()
        assertThat(station.flowRateThreshold3).isNull()
        assertThat(station.typicalWaveHeight).isNull()
        assertThat(station.directionOfWaves).isNull()
    }

    @Test
    fun `unmarshalls minimal sea station`() {
        val xml = loadFile("/arso_data.xml")

        val arsoData = mapper.readValue(xml, ArsoData::class.java)

        val station = arsoData.stations[176]
        assertThat(station.flowRate).isNull()
        assertThat(station.flowRateText).isNull()
        assertThat(station.heigthAboveSeaLevel).isNull()
        assertThat(station.highWaterThreshold1).isNull()
        assertThat(station.highWaterThreshold2).isNull()
        assertThat(station.highWaterThreshold3).isNull()
        assertThat(station.id).isEqualTo(9400)
        assertThat(station.latitude).isEqualTo(45.551116)
        assertThat(station.longitude).isEqualTo(13.550017)
        assertThat(station.shortName).isEqualTo("Jadransko morje - boja Piran (NIB)")
        assertThat(station.stationName).isEqualTo("OB Piran (NIB)")
        assertThat(station.timestamp).isEqualTo("2019-12-07T16:00:00")
        assertThat(station.waterLevel).isNull()
        assertThat(station.waterTemperature).isNull()
        assertThat(station.waterbody).isEqualTo("Jadransko morje")
        assertThat(station.flowRateThreshold1).isNull()
        assertThat(station.flowRateThreshold2).isNull()
        assertThat(station.flowRateThreshold3).isNull()
        assertThat(station.typicalWaveHeight).isEqualTo(0.12)
        assertThat(station.directionOfWaves).isEqualTo(101)
    }

    private fun loadFile(fileName: String) =
        Files.readAllBytes(Paths.get(ArsoDataTest::class.java.getResource(fileName)!!.toURI()))
}