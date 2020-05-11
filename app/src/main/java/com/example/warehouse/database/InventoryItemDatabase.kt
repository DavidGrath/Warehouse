package com.example.warehouse.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.warehouse.models.entities.InventoryItem
import com.example.warehouse.models.entities.Warehouse
import com.example.warehouse.utils.Constants.Companion.APR_29_2020
import com.example.warehouse.utils.Constants.Companion.MAY_06_2020
import com.example.warehouse.utils.Constants.Companion.MAY_09_2020
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = arrayOf(InventoryItem::class, Warehouse::class), version = MAY_09_2020)
abstract class InventoryItemDatabase : RoomDatabase() {

    abstract fun inventoryItemDao() : InventoryItemDao
    abstract fun warehouseDao() : WarehouseDao

    companion object {

        @Volatile
        private var INSTANCE: InventoryItemDatabase? = null

        fun getDatabase(context: Context): InventoryItemDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room
                    .databaseBuilder(context.applicationContext, InventoryItemDatabase::class.java, "warehouse_db")
                    .fallbackToDestructiveMigration()
//                    .addCallback(DbCallback(GlobalScope))
                    .build()
                INSTANCE = instance
                return instance

            }
        }
        class DbCallback(var scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let {database ->
                    val dao = database.inventoryItemDao()
                    val wDao = database.warehouseDao()
                    scope.launch {
                        val wId = UUID.randomUUID().toString()
                        wDao.addWarehouse(Warehouse(null, wId,"Blue Danube", "Off the East Coast", wId))
                        dao.insertItem(InventoryItem(null, wId, UUID.randomUUID().toString(), "Apples", 15, "USD", 20F))
                        dao.insertItem(InventoryItem(null, wId, UUID.randomUUID().toString(), "Bananas", 50, "USD", 10F))
                        dao.insertItem(InventoryItem(null, wId, UUID.randomUUID().toString(), "Pencils", 200, "USD", 5F))
                        dao.insertItem(InventoryItem(null, wId, UUID.randomUUID().toString(), "Chicken", 50, "USD", 100F))
                        dao.insertItem(InventoryItem(null, wId, UUID.randomUUID().toString(), "Computer Mouse", 5, "USD", 20F))
                    }
                }
            }
        }
    }


}