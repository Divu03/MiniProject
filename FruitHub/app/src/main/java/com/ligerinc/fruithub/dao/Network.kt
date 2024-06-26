package com.ligerinc.fruithub.dao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FruitNetwork(
    @SerialName("fruit")
    val fruit : FruitDataNetwork
)

@Serializable
data class FruitDataNetwork(
    @SerialName("id")
    val id: String = "",
    @SerialName("name")
    val name: String = "",

    @SerialName("feature")
    val feature: String = "",
    @SerialName("uses")
    val uses: String = "",
    @SerialName("pests")
    val pests: String = "",
    @SerialName("funFact")
    val funFact: String = "",
    @SerialName("description")
    val description: String = "",

    @SerialName("temperature")
    val temperature: String = "",
    @SerialName("sunLight")
    val sunLight: String = "",
    @SerialName("hardinessZone")
    val hardinessZone: String = "",
    @SerialName("soil")
    val soil: String = "",
    @SerialName("growth")
    val growth: String = "",
    @SerialName("cautions")
    val cautions: String = "",

    @SerialName("water")
    val water: String = "",
    @SerialName("fertilizers")
    val fertilizers: String = "",
    @SerialName("pruning")
    val pruning: String = "",
    @SerialName("propagation")
    val propagation: String = "",
    @SerialName("repotting")
    val repotting: String = "",
    @SerialName("humidity")
    val humidity: String = "",

    @SerialName("calories")
    val calories: String = "",
    @SerialName("vitamins")
    val vitamins: String = "",
    @SerialName("sugar")
    val sugar: String = "",
    @SerialName("protein")
    val protein: String = "",
    @SerialName("carbs")
    val carbs: String = "",
    @SerialName("fat")
    val fat: String = ""
) {
    constructor() : this("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
    fun toFruitDataRoom() = FruitDataRoom(
        id,
        name,
        feature,
        uses,
        pests,
        funFact,
        description,
        temperature,
        sunLight,
        hardinessZone,
        soil,
        growth,
        cautions,
        water,
        fertilizers,
        pruning,
        propagation,
        repotting,
        humidity,
        calories,
        vitamins,
        sugar,
        protein,
        carbs,
        fat
    )
}