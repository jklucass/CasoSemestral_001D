package com.example.leguaalerta.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.leguaalerta.ui.navigation.AppNavegacion
import com.example.leguaalerta.ui.theme.LeguaAlertaTheme
import com.example.leguaalerta.viewmodel.LeguaAlertaViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val fabrica = LeguaAlertaViewModelFactory(applicationContext)
        setContent {
            LeguaAlertaTheme {
                AppNavegacion(fabrica = fabrica)
            }
        }
    }
}
