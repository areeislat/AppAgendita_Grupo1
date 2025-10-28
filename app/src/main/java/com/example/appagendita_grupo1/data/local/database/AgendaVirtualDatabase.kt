package com.example.appagendita_grupo1.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
    version = 2,            // <-- CAMBIO 2: Incrementar la versión de 1 a 2
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

        // Obtiene la instancia Singleton de la base de datos
        fun getInstance(context: Context): AgendaVirtualDatabase {
            // Si ya existe la instancia, la devuelve. Si no, la crea de forma segura (synchronized).
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgendaVirtualDatabase::class.java,
                    DATABASE_NAME
                )
                    // NOTA IMPORTANTE:
                    // Como cambiamos la versión de 1 a 2,
                    // esta línea borrará la base de datos existente (incluidas tus notas de prueba)
                    // y la creará desde cero con la nueva estructura (tabla Notes + Users).
                    // ¡Esto es perfecto para desarrollo!
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance // Devuelve la instancia recién creada
            }
        }
    }
}