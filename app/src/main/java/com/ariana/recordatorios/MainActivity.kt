package com.ariana.recordatorios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ariana.recordatorios.ui.theme.RecordatoriosAppTheme

// CONSGINA 2: modelos de datos inmutables.

enum class Categoria(val etiqueta: String) {
    ESTUDIO("Estudio"),
    PERSONAL("Personal"),
    UNIVERSIDAD("Universidad"),
    SALUD("Salud")
}

enum class Prioridad(val etiqueta: String) {
    ALTA("Alta"),
    MEDIA("Media"),
    BAJA("Baja")
}

sealed class EstadoRecordatorio {
    data object Pendiente : EstadoRecordatorio()
    data object Completado : EstadoRecordatorio()
    data object Pospuesto : EstadoRecordatorio()
}

data class Recordatorio(
    val id: Int,
    val titulo: String,
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: EstadoRecordatorio,
    val descripcion: String? = null,
    val ubicacion: String? = null
) {
    init {
        require(id > 0) {
            "El identificador debe ser mayor que cero."
        }

        require(titulo.isNotBlank()) {
            "El título no puede estar vacío."
        }

        require(titulo.length <= 100) {
            "El título no puede superar los 100 caracteres."
        }

        require(descripcion == null || descripcion.length <= 500) {
            "La descripción no puede superar los 500 caracteres."
        }
    }
}

// CONSIGNA 3: datos simulados, List, Set y Map.

val recordatorios: List<Recordatorio> = listOf(
    Recordatorio(
        id = 1,
        titulo = "Estudiar Kotlin",
        categoria = Categoria.ESTUDIO,
        prioridad = Prioridad.ALTA,
        estado = EstadoRecordatorio.Pendiente,
        descripcion = "Repasar los conceptos básicos"
    ),
    Recordatorio(
        id = 2,
        titulo = "Comprar alimentos",
        categoria = Categoria.PERSONAL,
        prioridad = Prioridad.MEDIA,
        estado = EstadoRecordatorio.Pendiente
    ),
    Recordatorio(
        id = 3,
        titulo = "Entregar actividad",
        categoria = Categoria.UNIVERSIDAD,
        prioridad = Prioridad.ALTA,
        estado = EstadoRecordatorio.Completado
    ),
    Recordatorio(
        id = 4,
        titulo = "Hacer ejercicio",
        categoria = Categoria.SALUD,
        prioridad = Prioridad.BAJA,
        estado = EstadoRecordatorio.Pendiente
    ),
    Recordatorio(
        id = 5,
        titulo = "Practicar programación",
        categoria = Categoria.ESTUDIO,
        prioridad = Prioridad.MEDIA,
        estado = EstadoRecordatorio.Pospuesto,
        descripcion = "Continuar con los ejercicios"
    )
)

// El Set evita repetir categorías.
val categoriasDisponibles: Set<Categoria> =
    recordatorios.map { it.categoria }.toSet()

// El Map asocia cada categoría con un color.
val coloresCategorias: Map<Categoria, Color> = mapOf(
    Categoria.ESTUDIO to Color(0xFFE8EAF6),
    Categoria.PERSONAL to Color(0xFFFFF3E0),
    Categoria.UNIVERSIDAD to Color(0xFFE0F2F1),
    Categoria.SALUD to Color(0xFFFCE4EC)
)

// Filtros por estado y prioridad.
fun filtrarPorEstado(
    estado: EstadoRecordatorio
): List<Recordatorio> {
    return recordatorios.filter { it.estado == estado }
}

fun filtrarPorPrioridad(
    prioridad: Prioridad
): List<Recordatorio> {
    return recordatorios.filter { it.prioridad == prioridad }
}

// CONSIGNA 4: interfaz con Jetpack Compose y Material 3.

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RecordatoriosAppTheme {
                PantallaRecordatorios()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRecordatorios() {
    val recordatoriosPendientes =
        filtrarPorEstado(EstadoRecordatorio.Pendiente)

    val recordatoriosAltaPrioridad =
        filtrarPorPrioridad(Prioridad.ALTA)

    val cantidadCategorias = categoriasDisponibles.size

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Mis recordatorios",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Organizá tus tareas",
                            fontSize = 13.sp
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tus tareas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${recordatorios.size} recordatorios registrados",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${recordatoriosPendientes.size} pendientes · " +
                        "${recordatoriosAltaPrioridad.size} de prioridad alta · " +
                        "$cantidadCategorias categorías",
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recordatorios) { recordatorio ->
                    TarjetaRecordatorio(recordatorio)
                }
            }
        }
    }
}

@Composable
fun TarjetaRecordatorio(recordatorio: Recordatorio) {
    val colorCategoria =
        coloresCategorias[recordatorio.categoria] ?: Color.LightGray

    val colorPrioridad = when (recordatorio.prioridad) {
        Prioridad.ALTA -> Color(0xFFC62828)
        Prioridad.MEDIA -> Color(0xFFEF6C00)
        Prioridad.BAJA -> Color(0xFF2E7D32)
    }

    val textoEstado = when (recordatorio.estado) {
        EstadoRecordatorio.Pendiente -> "Pendiente"
        EstadoRecordatorio.Completado -> "Completado"
        EstadoRecordatorio.Pospuesto -> "Pospuesto"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = recordatorio.titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            recordatorio.descripcion?.let { descripcion ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = descripcion,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Etiqueta(
                    texto = recordatorio.categoria.etiqueta,
                    color = colorCategoria
                )

                Etiqueta(
                    texto = "Prioridad ${recordatorio.prioridad.etiqueta}",
                    color = Color(0xFFFFF0E0),
                    textoColor = colorPrioridad
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Estado: $textoEstado",
                fontSize = 13.sp,
                color = when (recordatorio.estado) {
                    EstadoRecordatorio.Pendiente -> Color(0xFFEF6C00)
                    EstadoRecordatorio.Completado -> Color(0xFF2E7D32)
                    EstadoRecordatorio.Pospuesto -> Color.Gray
                },
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun Etiqueta(
    texto: String,
    color: Color,
    textoColor: Color = Color(0xFF333333)
) {
    Surface(
        color = color,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            color = textoColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaRecordatorios() {
    RecordatoriosAppTheme {
        PantallaRecordatorios()
    }
}