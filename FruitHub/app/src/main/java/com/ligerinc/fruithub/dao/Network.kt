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
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",

    @SerialName("features")
    val features: String = "",
    @SerialName("uses")
    val uses: String = "",
    @SerialName("pests")
    val pests: String = "",
    @SerialName("funFacts")
    val funFacts: String = "",
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
    @SerialName("fertilizer")
    val fertilizer: String = "",
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
    constructor() : this(0, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
    fun toFruitDataRoom() = FruitDataRoom(
        id,
        name,
        features,
        uses,
        pests,
        funFacts,
        description,
        temperature,
        sunLight,
        hardinessZone,
        soil,
        growth,
        cautions,
        water,
        fertilizer,
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