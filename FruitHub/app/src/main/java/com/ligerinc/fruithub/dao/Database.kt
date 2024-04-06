package com.ligerinc.fruithub.dao

import androidx.annotation.DrawableRes
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
    @PrimaryKey val id: String,
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
    val fertilizers:String,
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
    fun getAllFDR():LiveData<FruitDataRoom>

    @Query("SELECT * FROM FruitDataRoom WHERE name = :name")
    fun getByNameFDR(name: String): LiveData<FruitDataRoom>

    @Insert
    fun insertObjFDR(fruitData: FruitDataRoom)

    @Insert
    fun insertAllFDR(vararg fruitData: FruitDataRoom)

    @Query("SELECT (SELECT COUNT(*) FROM FruitDataRoom) == 0")
    fun isEmptyFDR(): Boolean

    @Query("SELECT * FROM FruitList")
    fun getAllFL():LiveData<FruitList>

    @Query("SELECT * FROM FruitList WHERE name = :name")
    fun getByNameFL(name: String): LiveData<FruitList>

    @Insert
    fun insertObjFL(fruitList: FruitList)

    @Insert
    fun insertAllFL(vararg fruitList: FruitList)
    @Query("SELECT * FROM FruitList WHERE name LIKE '%' || :name || '%'")
    fun searchByNameFL(name: String): LiveData<List<FruitList>>
    @Query("SELECT (SELECT COUNT(*) FROM FruitList) == 0")
    fun isEmptyFL(): Boolean

}

@Database(entities = [FruitDataRoom::class,FruitList::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fruitDataDao(): FruitDataDao
}

@Entity
data class FruitList(
    @PrimaryKey val id: Int,
    val name: String,
    @DrawableRes val imageId : Int
)