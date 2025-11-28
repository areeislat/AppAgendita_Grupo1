package com.example.appagendita_grupo1.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.appagendita_grupo1.data.local.database.converters.LocalDateTimeConverter
import com.example.appagendita_grupo1.data.local.event.EventDao
import com.example.appagendita_grupo1.data.local.event.EventEntity
import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.note.NoteEntity
import com.example.appagendita_grupo1.data.local.user.UserDao
import com.example.appagendita_grupo1.data.local.user.UserEntity

@Database(
    entities = [
        NoteEntity::class,
        UserEntity::class,
        EventEntity::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AgendaVirtualDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: AgendaVirtualDatabase? = null
        private const val DATABASE_NAME = "agenda_virtual.db"

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE notes ADD COLUMN userId INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `events` (`id` TEXT NOT NULL, `ownerId` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `eventTimestamp` TEXT NOT NULL, `location` TEXT, `createdAt` TEXT NOT NULL, `updatedAt` TEXT, PRIMARY KEY(`id`))")
            }
        }

        fun getInstance(context: Context): AgendaVirtualDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgendaVirtualDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}