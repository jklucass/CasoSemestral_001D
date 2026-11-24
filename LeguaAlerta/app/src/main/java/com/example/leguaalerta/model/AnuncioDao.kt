package com.example.leguaalerta.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnuncioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(anuncio: Anuncio)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(anuncios: List<Anuncio>)

    @Query("DELETE FROM anuncios")
    suspend fun borrarTodos()

    @Delete
    suspend fun eliminar(anuncio: Anuncio)

    @Query("SELECT * FROM anuncios ORDER BY fechaPublicacion DESC")
    fun observarTodos(): Flow<List<Anuncio>>
}
