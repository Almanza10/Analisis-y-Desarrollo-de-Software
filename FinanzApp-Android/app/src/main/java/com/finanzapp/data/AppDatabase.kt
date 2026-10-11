package com.finanzapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.finanzapp.model.Usuario

/**
 * Base de datos local (SQLite) de la app.
 * Reemplaza la conexión a MySQL (`spring.datasource.*`) del proyecto Spring Boot.
 */
@Database(entities = [Usuario::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        private const val NOMBRE_BD = "finanzapp_db"

        @Volatile
        private var instancia: AppDatabase? = null

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    NOMBRE_BD
                ).build().also { instancia = it }
            }
    }
}
