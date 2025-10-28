package com.example.appagendita_grupo1.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appagendita_grupo1.data.local.note.NoteDao
import com.example.appagendita_grupo1.data.local.note.NoteEntity

// Define las entidades (tablas) y la versión de la base de datos
@Database(
    entities = [NoteEntity::class], // Añade aquí más entidades si creas EventEntity, etc.
    version = 1,                    // Incrementa si cambias el esquema
    exportSchema = false            // Puedes poner true para exportar el esquema (útil para migraciones complejas)
)
abstract class AgendaVirtualDatabase : RoomDatabase() {

    // Expone el DAO para las notas
    abstract fun noteDao(): NoteDao
    // Añade aquí más DAOs si los necesitas (ej. abstract fun eventDao(): EventDao)

    companion object {
        @Volatile // Asegura que la instancia sea visible inmediatamente para todos los hilos
        private var INSTANCE: AgendaVirtualDatabase? = null
        private const val DATABASE_NAME = "agenda_virtual.db"

        // Obtiene la instancia Singleton de la base de datos
        fun getInstance(context: Context): AgendaVirtualDatabase {
            // Si ya existe la instancia, la devuelve. Si no, la crea de forma segura (synchronized).
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgendaVirtualDatabase::class.java,
                    DATABASE_NAME
                )
                    // IMPORTANTE: En desarrollo, si cambias la versión y no quieres hacer migraciones,
                    // puedes usar fallbackToDestructiveMigration(). Esto borrará y recreará la BD.
                    // ¡¡NO USAR EN PRODUCCIÓN sin una estrategia de migración!!
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance // Devuelve la instancia recién creada
            }
        }
    }
}