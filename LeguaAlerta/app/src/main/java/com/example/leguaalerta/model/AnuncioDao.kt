package com.example.leguaalerta.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnuncioDao {

    @Insert
    suspend fun insertar(anuncio: Anuncio)

    @Delete
    suspend fun eliminar(anuncio: Anuncio)

    @Query("SELECT * FROM anuncios ORDER BY fechaPublicacion DESC")
    fun observarTodos(): Flow<List<Anuncio>>
}
