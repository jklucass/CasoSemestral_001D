# LeguaAlerta

Aplicación Android para la **gestión y el seguimiento de situaciones de seguridad comunal** en
San Joaquín, desarrollada para el caso semestral de **DSY1104 – Desarrollo de Aplicaciones
Móviles** (Duoc UC, sede San Joaquín). Es un MVP académico: todos los datos son ficticios.

## ¿Qué hace?

**Vista vecino**
- Registro e inicio de sesión, con opción **"Recordarme"**.
- Reportar una incidencia (alumbrado, seguridad, espacios públicos, infraestructura u otro)
  en un formulario por pasos, con ubicación referencial, prioridad y foto opcional.
- Ver el estado de sus reportes (Recibido, En terreno, Derivado, Cerrado) y la respuesta municipal.
- Leer las noticias del barrio que publica la municipalidad.

**Vista municipal (administrador)**
- Bandeja de casos filtrada por estado.
- Cambiar el estado de un caso y dejar una observación de seguimiento.
- Creador de anuncios para los vecinos.

## Cuentas de prueba

| Rol | Correo | Contraseña |
| --- | --- | --- |
| Vecino | vecina@leguaalerta.cl | Vecina2026 |
| Municipal | municipal@leguaalerta.cl | Municipal2026 |

Para registrar una cuenta municipal nueva se pide el código **SJ2026**.

## Requisitos de la evaluación

| Requisito | Dónde se cumple |
| --- | --- |
| Formulario con validación | Login, registro, nuevo reporte, anuncios y gestión de casos (`ValidadorFormularios`) |
| Imagen | Logo vectorial `logo_legua_alerta.xml` y foto del reporte |
| Animación | Logo que "late" en la pantalla de carga y mensajes con `AnimatedVisibility` |
| Modal | Confirmar envío, detalle del reporte, gestionar caso, eliminar anuncio y cerrar sesión |
| Persistencia con Room | Usuarios (con sesión recordada), reportes y anuncios |
| Navegación entre vistas | Navigation Compose con vistas separadas para vecino y municipal |
| Recurso nativo | Cámara del teléfono para adjuntar una foto al reporte |

## Arquitectura (MVVM)

```
app/src/main/java/com/example/leguaalerta/
├── model/        Entidades de Room, DAO y base de datos
├── repository/   Acceso a los datos (usuarios, reportes, anuncios, fotos y datos demo)
├── viewmodel/    Lógica de las pantallas, validaciones y fábrica de ViewModel
└── ui/           MainActivity, pantallas, componentes, navegación y tema
```

Cada capa solo conoce a la de abajo: `ui` → `viewmodel` → `repository` → `model`.

## Tecnologías

Kotlin · Jetpack Compose (Material 3) · Room · Navigation Compose · ViewModel + StateFlow ·
compileSdk/targetSdk 37 · minSdk 24.

## Cómo ejecutar

1. Abrir la carpeta `LeguaAlerta` en Android Studio.
2. Instalar el **SDK Platform 37** desde *Tools → SDK Manager* si no está instalado.
3. Sincronizar Gradle y ejecutar en un emulador o teléfono con Android 7.0 o superior.

## Equipo

Proyecto desarrollado en equipo, con tablero de tareas en Trello y control de versiones en GitHub.
