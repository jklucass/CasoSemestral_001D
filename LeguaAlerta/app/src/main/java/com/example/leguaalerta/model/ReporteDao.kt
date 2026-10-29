package com.example.leguaalerta.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDao {

    @Insert
    suspend fun insertar(reporte: Reporte): Long

    @Update
    suspend fun actualizar(reporte: Reporte)

    @Query("SELECT * FROM reportes WHERE vecinoId = :vecinoId ORDER BY fechaCreacion DESC")
    fun observarPorVecino(vecinoId: Int): Flow<List<Reporte>>

    @Query("SELECT * FROM reportes ORDER BY fechaCreacion DESC")
    fun observarTodos(): Flow<List<Reporte>>
}
