package com.littlelemon.fruithub.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity
data class FruitDataRoom(
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

@Dao
interface FruitDataDao{

    @Query("SELECT * FROM FruitDataRoom")
    fun getAll():LiveData<FruitDataRoom>

    @Query("SELECT * FROM FruitDataRoom WHERE name = :name")
    fun getByName(name: String): LiveData<FruitDataRoom>

    @Insert
    fun insertObj(fruitData: FruitDataRoom)

    @Insert
    fun insertAll(vararg fruitData: FruitDataRoom)

    @Query("SELECT (SELECT COUNT(*) FROM FruitDataRoom) == 0")
    fun isEmpty(): Boolean


}

@Database(entities = [FruitDataRoom::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fruitDataDao(): FruitDataDao
}