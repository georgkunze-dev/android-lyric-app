package com.example.m28909_lyrixplorer

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// Fragment für die Startseite
class HomeFragment {

    // Datenklasse zur Verknüpfung eines Songs mit dem Album, aus dem der Song stammt
    data class RandomSong(val song: Song, val album: Album)

    // Funktion zur Darstellung des Home-Fragments
    @Composable
    fun GetFragment(context: Context, albums: List<Album>?, navController: NavHostController) {
        val randomSong = remember { mutableStateOf<RandomSong?>(null) } // MutableState zur Speicherung eines zufälligen Songs

        // Fehlerbehandlung für null-Alben
        if (albums.isNullOrEmpty()) {
            Log.e("isNullOrEmpty", "No albums available")
            return // Beendet die Komposition, wenn keine Alben vorhanden sind
        }

        // Zufälligen Song auswählen, wenn noch nicht gesetzt
        if (randomSong.value == null) {
            // Alle Songs aus allen Alben sammeln
            val allSongs = albums.flatMap { album -> album.songs.map { song -> RandomSong(song, album) } }
            // Zufälligen Song auswählen, falls die Liste der Songs nicht leer ist
            if (allSongs.isNotEmpty()) {
                randomSong.value = allSongs.random() // Setzen des zufälligen Songs
            }
        }

        // Zeigt den zufällig ausgewählten Song an, falls er nicht null ist
        randomSong.value?.let { (song, album) ->
            Column {
                UIBasis.GetSiteTitle("Home") // Seitentitel
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
                        .clickable { navigateToLyrics(navController, song, Konstanten.HOME) }
                ) {
                    Column (
                        modifier = Modifier.width(300.dp)
                    ) {
                        UIBasis.GetHeadlineText(text = "Your random Song!") // Überschrift anzeigen
                        UIBasis.GetImage(context = context, albumName = album.imgName, 300.dp) // Bild des Albums anzeigen
                        UIBasis.GetSongTitleText(song.title) // Titel des Songs anzeigen
                        UIBasis.GetAlbumArtistText(album.artist) // Name des Künstlers anzeigen
                    } // Column
                } // Box
            } // Column
        }
    } // GetFragment

    // Funktion zur Navigation zur Lyrics-Seite mit Jason-, Base64- und URL-kodierten Daten
    private fun navigateToLyrics(navController: NavHostController, song: Song, backRoute: String) {
        try {
            val songJson = Json.encodeToString(song)  // Song-Daten in JSON konvertieren
            val base64SongJson = Base64.encodeToString(songJson.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT) // JSON-String in Base64 kodieren
            val urlEncodedSongJson = URLEncoder.encode(base64SongJson, StandardCharsets.UTF_8.toString()) // Base64-kodierten String URL-kodieren

            val backrouteJson = Json.encodeToString(backRoute) // Navigationsdaten in JSON konvertieren
            val base64Backroute = Base64.encodeToString(backrouteJson.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT) // JSON-String in Base64 kodieren
            val urlEncodedBackroute = URLEncoder.encode(base64Backroute, StandardCharsets.UTF_8.toString()) // Base64-kodierten String URL-kodieren

            val navigationRoute = "lyrics/$urlEncodedSongJson/$urlEncodedBackroute" // Navigationsroute zur Lyrics-Seite und Backroute-Daten
            navController.navigate(navigationRoute) // Navigation zu Lyrics Seite
        } catch (e: Exception) {
            // Fehler bei der Navigation
            Log.e("SongFragment", "Error during navigation: ${e.message}")
        }
    } // navigateToLyrics
} // HomeFragment