package com.example.lab12_maps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen() {
    val arequipa = LatLng(-16.4040102, -71.559611)
    val lima = LatLng(-12.0464, -77.0428)

    // Ejercicio 1: tipo de mapa
    var mapType by remember { mutableStateOf(MapType.NORMAL) }
    // Polilínea clickeable
    var rutaSeleccionada by remember { mutableStateOf(false) }

    val locations = listOf(
        LatLng(-16.433415, -71.5442652),  // JLByR
        LatLng(-16.4205151, -71.4945209), // Paucarpata
        LatLng(-16.3524187, -71.5675994)  // Zamacola
    )
    val plazaDeArmas = listOf(
        LatLng(-16.398866, -71.536961), LatLng(-16.398744, -71.536529),
        LatLng(-16.399178, -71.536289), LatLng(-16.399299, -71.536721)
    )
    val parqueLambramani = listOf(
        LatLng(-16.422704, -71.530830), LatLng(-16.422920, -71.531340),
        LatLng(-16.423264, -71.531110), LatLng(-16.423050, -71.530600)
    )
    val mallAventura = listOf(
        LatLng(-16.432292, -71.509145), LatLng(-16.432757, -71.509626),
        LatLng(-16.433013, -71.509310), LatLng(-16.432566, -71.508853)
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(arequipa, 12f)
    }

    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(LatLng(-16.2520984, -71.6836503), 12f),
            durationMs = 3000
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = mapType)
        ) {
            Marker(
                state = rememberMarkerState(position = arequipa),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                title = "Arequipa, Perú"
            )

            locations.forEach { location ->
                Marker(
                    state = rememberMarkerState(position = location),
                    title = "Ubicación",
                    snippet = "Punto de interés"
                )
            }

            listOf(plazaDeArmas, parqueLambramani, mallAventura).forEach { points ->
                Polygon(
                    points = points,
                    strokeColor = Color.Red,
                    fillColor = Color.Blue.copy(alpha = 0.4f),
                    strokeWidth = 5f
                )
            }

            // Polilínea 1: punteada con extremos redondeados
            Polyline(
                points = locations,
                color = Color(0xFF1B5E20),
                width = 10f,
                pattern = listOf(Dash(30f), Gap(20f)),
                startCap = RoundCap(),
                endCap = RoundCap(),
                jointType = JointType.ROUND
            )

            // Polilínea 2: geodésica Arequipa - Lima
            Polyline(
                points = listOf(arequipa, lima),
                color = Color.Red,
                width = 6f,
                geodesic = true
            )

            // Polilínea 3: clickeable, cambia de color
            Polyline(
                points = listOf(plazaDeArmas[0], parqueLambramani[0], mallAventura[0]),
                color = if (rutaSeleccionada) Color.Green else Color.Magenta,
                width = 12f,
                clickable = true,
                onClick = { rutaSeleccionada = !rutaSeleccionada }
            )
        }

        // Botones para cambiar el tipo de mapa
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(onClick = { mapType = MapType.NORMAL }) { Text("Normal") }
            Button(onClick = { mapType = MapType.SATELLITE }) { Text("Satélite") }
            Button(onClick = { mapType = MapType.HYBRID }) { Text("Híbrido") }
            Button(onClick = { mapType = MapType.TERRAIN }) { Text("Relieve") }
        }
    }
}