package com.example.leguaalerta.repository

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

class FotoRepository(private val carpetaFotos: File) {

    fun guardar(foto: Bitmap): String {
        if (!carpetaFotos.exists()) carpetaFotos.mkdirs()
        val archivo = File(carpetaFotos, "reporte_${System.currentTimeMillis()}.jpg")
        FileOutputStream(archivo).use { salida ->
            foto.compress(Bitmap.CompressFormat.JPEG, CALIDAD_JPG, salida)
        }
        return archivo.absolutePath
    }

    private companion object {
        const val CALIDAD_JPG = 90
    }
}
