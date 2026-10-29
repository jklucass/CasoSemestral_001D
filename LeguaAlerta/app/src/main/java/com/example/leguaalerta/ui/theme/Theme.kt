package com.example.leguaalerta.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaColoresLegua = lightColorScheme(
    primary = VerdeLegua,
    onPrimary = BlancoTarjeta,
    primaryContainer = VerdeLeguaClaro,
    onPrimaryContainer = VerdeLegua,
    secondary = AmbarSuave,
    onSecondary = TextoPrincipal,
    secondaryContainer = AmbarClaro,
    onSecondaryContainer = TextoPrincipal,
    background = CremaFondo,
    onBackground = TextoPrincipal,
    surface = BlancoTarjeta,
    onSurface = TextoPrincipal,
    onSurfaceVariant = TextoSecundario,
    outline = BordeSuave,
    error = RojoError
)

@Composable
fun LeguaAlertaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaColoresLegua,
        typography = Typography,
        content = content
    )
}
