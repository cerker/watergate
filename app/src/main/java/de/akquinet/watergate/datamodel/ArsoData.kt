package de.akquinet.watergate.datamodel

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import java.util.*

@JsonRootName("arsopodatki")
data class ArsoData(
    @JsonProperty("verzija")
    var version: String,
    @JsonProperty("vir")
    var source: String,
    @JsonProperty("predlagan_zajem")
    var proposedTimeOfUpdate: String,
    @JsonProperty("predlagan_zajem_perioda")
    var proposedPeriodOfUpdate: String,
    @JsonProperty("datum_priprave")
    var timestampOfProvision: Date,
    @JsonProperty("postaja")
    @JacksonXmlElementWrapper(useWrapping = false)
    var stations: List<Station>
)

@JsonRootName("postaja")
data class Station(
    @JsonProperty("sifra")
    var id: Int,
    @JsonProperty("ge_dolzina")
    var longitude: Double,
    @JsonProperty("ge_sirina")
    var latitude: Double,
    @JsonProperty("kota_0")
    var heigthAboveSeaLevel: Double?, // meters
    @JsonProperty("reka")
    var waterbody: String,
    @JsonProperty("merilno_mesto")
    var stationName: String,
    @JsonProperty("ime_kratko")
    var shortName: String,
    @JsonProperty("datum")
    var timestamp: Date,
    @JsonProperty("vodostaj")
    var waterLevel: Int?, // centimeters
    @JsonProperty("vodostaj_znacilni")
    var waterLevelText: String?,
    @JsonProperty("pretok")
    var flowRate: Double?, // qm per hour
    @JsonProperty("pretok_znacilni")
    var flowRateText: String?,
    @JsonProperty("temp_vode")
    var waterTemperature: Double?, // centigrade
    @JsonProperty("prvi_vv_pretok")
    var flowRateThreshold1: Double?, // centimeters
    @JsonProperty("drugi_vv_pretok")
    var flowRateThreshold2: Double?, // centimeters
    @JsonProperty("tretji_vv_pretok")
    var flowRateThreshold3: Double?, // centimeters
    @JsonProperty("prvi_vv_vodostaj")
    var highWaterThreshold1: Double?, // centimeters
    @JsonProperty("drugi_vv_vodostaj")
    var highWaterThreshold2: Double?, // centimeters
    @JsonProperty("tretji_vv_vodostaj")
    var highWaterThreshold3: Double?, // centimeters
    @JsonProperty("znacilna_visina_valov")
    var typicalWaveHeight: Double?, // centimeters
    @JsonProperty("smer_valovanja")
    var directionOfWaves: Int? // centimeters
)
