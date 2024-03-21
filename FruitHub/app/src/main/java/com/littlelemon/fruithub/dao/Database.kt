package com.littlelemon.fruithub.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FruitData(
    @PrimaryKey val id: Int,
    val name:String,

    val feature:String,
    val uses:String,
    val pests:String,
    val funFact:String,
    val description:String,

    val temperature:String,
    val sunLight: String,
    val hardinessZone:String,
    val soil:String,
    val growth:String,
    val cautions: String,

    val water:String,
    val fertilizer:String,
    val pruning:String,
    val propagation:String,
    val repotting:String,
    val humidity:String,

    val calories:String,
    val vitamins:String,
    val sugar:String,
    val protein:String,
    val carbs:String,
    val fat:String,
)