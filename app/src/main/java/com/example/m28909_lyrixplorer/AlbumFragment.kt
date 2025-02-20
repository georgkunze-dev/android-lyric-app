package com.example.m28909_lyrixplorer

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// Fragment zur Darstellung der Alben
class AlbumFragment {

    // Funktion zur Anzeige der Album-Liste
    @Composable
    fun GetFragment(context: Context, albums: List<Album>?, navController: NavHostController) {
        Column {
            UIBasis.GetSiteTitle("Albums") // Seitentitel

            // Überprüfung, ob Alben vorhanden sind
            if (albums.isNullOrEmpty()) {
                // Falls keine Alben verfügbar sind
                Log.e("AlbumFragment", "No albums available")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    // Eigene Reihe für jedes Album
                    items(albums) { album ->
                        AlbumRow(album = album, context = context, navController = navController)
                    }
                } // LazyColumn
            } // else
        } // Column
    } // GetFragment

    // Funktion zur Darstellung einer einzelnen Album-Reihe
    @Composable
    fun AlbumRow(album: Album, context: Context, navController: NavHostController) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.LightGray)
                .clickable {navigateToAlbumSongs(navController, album)},
            verticalAlignment = Alignment.CenterVertically
        ) {
            UIBasis.GetImage(context = context, albumName = album.imgName, 100.dp) // Album-Bild
            UIBasis.GetSpacerWidth()
            // Album- und Künstlername
            Column(modifier = Modifier.weight(1f)) {
                UIBasis.GetSongTitleText(album.album)
                UIBasis.GetAlbumArtistText(album.artist)
            } // Column
        } // Row
    } // AlbumRow

    // Funktion zur Navigation zur Album-Songs-Seite mit Jason-, Base64- und URL-kodierten Daten
    private fun navigateToAlbumSongs(navController: NavHostController, album: Album) {
        try {
            val albumJson = Json.encodeToString(album) // Album-Daten in JSON konvertieren
            val base64AlbumJson = Base64.encodeToString(albumJson.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT) // JSON-String in Base64 kodieren
            val urlEncodedAlbumJson = URLEncoder.encode(base64AlbumJson, StandardCharsets.UTF_8.toString()) // Base64-kodierten String URL-kodieren

            val navigationRoute = "albumSongs/$urlEncodedAlbumJson" // Navigationsroute zur Lyrics-Seite und Backroute-Daten
            navController.navigate(navigationRoute) // Navigation zu Album-Songs-Seite
        } catch (e: Exception) {
            // Fehler beim Serialisieren oder Navigieren
            Log.e("AlbumFragment", "Error during navigation: ${e.message}")
        }
    } // navigateToAlbumSongs
} // AlbumFragment