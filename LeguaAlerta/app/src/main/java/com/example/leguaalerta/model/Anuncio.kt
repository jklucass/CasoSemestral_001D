package com.example.leguaalerta.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "anuncios")
data class Anuncio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val mensaje: String,
    @SerializedName("created_at") val fechaPublicacion: Long = System.currentTimeMillis()
)
