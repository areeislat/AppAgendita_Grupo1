package com.example.appagendita_grupo1.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.note.NoteEntity
// --- INICIO DE CAMBIOS: IMPORTAR USER ---
import com.example.appagendita_grupo1.data.local.user.UserDao
import com.example.appagendita_grupo1.data.local.user.UserEntity
// --- FIN DE CAMBIOS: IMPORTAR USER ---

// Define las entidades (tablas) y la versión de la base de datos
@Database(
    entities = [
        NoteEntity::class,
        UserEntity::class // <-- CAMBIO 1: Añadir UserEntity
    ],
    version = 3,            // <-- CAMBIO 2: Incrementar la versión de 2 a 3
    exportSchema = false
)
abstract class AgendaVirtualDatabase : RoomDatabase() {

    // Expone el DAO para las notas
    abstract fun noteDao(): NoteDao

    // --- INICIO DE CAMBIOS: AÑADIR NUEVO DAO ---
    // Expone el DAO para los usuarios
    abstract fun userDao(): UserDao
    // --- FIN DE CAMBIOS: AÑADIR NUEVO DAO ---

    companion object {
        @Volatile // Asegura que la instancia sea visible inmediatamente para todos los hilos
        private var INSTANCE: AgendaVirtualDatabase? = null
        private const val DATABASE_NAME = "agenda_virtual.db"

        // Migration from version 2 to 3: Add userId column to notes table
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add userId column with default value 0 for existing notes
                database.execSQL("ALTER TABLE notes ADD COLUMN userId INTEGER NOT NULL DEFAULT 0")
            }
        }

        // Obtiene la instancia Singleton de la base de datos
        fun getInstance(context: Context): AgendaVirtualDatabase {
            // Si ya existe la instancia, la devuelve. Si no, la crea de forma segura (synchronized).
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgendaVirtualDatabase::class.java,
                    DATABASE_NAME
                )
                    // Add migration strategy
                    .addMigrations(MIGRATION_2_3)
                    // NOTA IMPORTANTE:
                    // fallbackToDestructiveMigration() borrará la base de datos si no hay migración disponible
                    // Esto es útil para desarrollo, pero en producción deberías manejar migraciones apropiadamente
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance // Devuelve la instancia recién creada
            }
        }
    }
}