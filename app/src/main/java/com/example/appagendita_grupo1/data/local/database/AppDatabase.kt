package com.example.appagendita_grupo1.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors
import kotlinx.coroutines.runBlocking

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "appagendita_users.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val databaseExecutor = Executors.newSingleThreadExecutor()

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(appContext: Context): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        databaseExecutor.execute {
                            INSTANCE?.let { database ->
                                val seedUser = UserEntity(
                                    username = "Invitado",
                                    email = "invitado@appagendita.com",
                                    passwordHash = "guest"
                                )
                                runBlocking {
                                    database.userDao().insert(seedUser)
                                }
                            }
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
