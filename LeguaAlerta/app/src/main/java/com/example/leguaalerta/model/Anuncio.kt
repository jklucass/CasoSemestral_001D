package com.example.leguaalerta.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anuncios")
data class Anuncio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val mensaje: String,
    val fechaPublicacion: Long = System.currentTimeMillis()
)
