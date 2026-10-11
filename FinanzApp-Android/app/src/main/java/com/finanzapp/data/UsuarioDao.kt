package com.finanzapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.finanzapp.model.Usuario
import kotlinx.coroutines.flow.Flow

/** Acceso a datos de la tabla `usuarios` (equivalente a JpaRepository<Usuario, Long>). */
@Dao
interface UsuarioDao {

    /** Lanza SQLiteConstraintException si el email o la cédula ya existen. */
    @Insert
    suspend fun insertar(usuario: Usuario): Long

    @Update
    suspend fun actualizar(usuario: Usuario)

    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun eliminarPorId(id: Long)

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun buscarPorId(id: Long): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email COLLATE NOCASE LIMIT 1")
    suspend fun buscarPorEmail(email: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE cedula = :cedula LIMIT 1")
    suspend fun buscarPorCedula(cedula: String): Usuario?

    @Query("SELECT * FROM usuarios ORDER BY id")
    fun observarTodos(): Flow<List<Usuario>>
}
