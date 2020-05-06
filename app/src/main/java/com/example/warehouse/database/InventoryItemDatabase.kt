package com.example.warehouse.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.warehouse.models.entities.InventoryItem
import com.example.warehouse.utils.Constants.Companion.APR_29_2020
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(InventoryItem::class), version = APR_29_2020)
abstract class InventoryItemDatabase : RoomDatabase() {

    abstract fun inventoryItemDao() : InventoryItemDao

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
                    .addCallback(DbCallback(GlobalScope))
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
                    scope.launch {
                        dao.insertItem(InventoryItem(null, "Apples", 15, "USD", 20F))
                        dao.insertItem(InventoryItem(null, "Bananas", 50, "USD", 10F))
                        dao.insertItem(InventoryItem(null, "Pencils", 200, "USD", 5F))
                        dao.insertItem(InventoryItem(null, "Chicken", 50, "USD", 100F))
                        dao.insertItem(InventoryItem(null, "Computer Mouse", 5, "USD", 20F))
                    }
                }

            }
        }
    }


}