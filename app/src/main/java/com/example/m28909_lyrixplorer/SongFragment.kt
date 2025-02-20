package com.example.m28909_lyrixplorer

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Locale

// Fragment für die Darstellung der Songs
class SongFragment {

    // Funktion zur Anzeige der Songs eines bestimmten Albums
    @Composable
    fun GetAlbumSongs(context: Context, albumJson: String, navController: NavHostController) {
        // JSON-Decodierung des Albums
        val album = try {
            Json.decodeFromString<Album>(albumJson)
        } catch (e: Exception) {
            // Fehler beim Dekodieren anzeigen
            Log.e("SongFragment", "Error loading album")
            return
        }

        // Layout für die Anzeige der Album-Songs
        Column {
            // Titelzeile mit einem Rückkehr-Icon und Titel
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.DarkGray)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                UIBasis.GetBackIcon(navController, Konstanten.ALBUM) // Zurück-Button
                UIBasis.GetSiteTitle("Songs in the Album") // Seiten-Titel
            }

            // Überprüft, ob das Album Songs enthält
            if (album.songs.isEmpty()) {
                Log.e("SongFragment", "No Songs in the Album")
            } else {
                // Auflistung der Songs in einer LazyColumn
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(bottom = 80.dp)
                ) {
                    // Eigene Reihe für jeden Song im Album
                    items(album.songs) { song ->
                        GetSongRow(album = album, song = song, context = context, navController, Konstanten.ALBUM)
                    }
                } // LazyColumn
            } // else
        } // Column
    } // GetAlbumSongs

    // Funktion zur Anzeige aller Songs aus allen Alben
    @Composable
    fun GetAllSongsFragment(context: Context, albums: List<Album>?, navController: NavHostController) {
        // Liste von Songs aus allen Alben sortiert nach dem Songtitel
        val allAlbums = remember(albums) {
            albums?.flatMap { album ->
                album.songs.map { song -> album to song }
            }?.sortedBy { it.second.title.lowercase(Locale.ROOT) } // Sortierung der Songs nach Titel
        } // remember

        // Layout für die Anzeige aller Songs
        Column {
            UIBasis.GetSiteTitle("Songs") // Seiten-Titel

            if (allAlbums.isNullOrEmpty()) {
                Log.e("SongFragment", "No Songs available")
            } else {
                // Auflistung der Songs in einer LazyColumn
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(bottom = 80.dp)
                ) {
                    // Eigene Reihe für jeden Song in allen Alben
                    items(allAlbums) { (album, song) ->
                        GetSongRow(album = album, song = song, context = context, navController, Konstanten.SONG)
                    }
                } // LazyColumn
            } // else
        } // Column
    } // GetAllSongsFragment

    // Funktion zur Anzeige der Suchergebnisse
    @Composable
    fun GetFragmentForSearchResults(context: Context, searchResults: List<Pair<Album, Song>>?, navController: NavHostController) {
        // Wenn keine Suchergebnisse vorhanden sind, wird die Funktion beendet
        if (searchResults.isNullOrEmpty()) {
            return
        } else {
            // Auflistung der Songs die dem Suchergebniss entsprechen
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(bottom = 80.dp)
            ) {
                items(searchResults) { (album, song) ->
                    GetSongRow(album = album, song = song, context = context, navController, Konstanten.SEARCH)
                }
            } // LazyColumn
        } // else
    } // GetFragmentForSearchResults

    // Funktion zur Darstellung einer einzelnen Song-Reihe in der Liste
    @Composable
    fun GetSongRow(album: Album, song: Song, context: Context, navController: NavHostController, backRoute: String) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.LightGray)
                .clickable {
                    navigateToLyrics(navController, song, backRoute) // Navigation zur Lyrics-Seite
                }
        ) {
            UIBasis.GetImage(context, album.imgName, 50.dp) // Zeigt das Album-Bild an
            UIBasis.GetSpacerWidth()
            // Zeigt den Song-Titel und den Album-Künstler in einer Spalte an
            Column(modifier = Modifier.weight(1f)) {
                UIBasis.GetSongTitleText(song.title) // Song-Titel
                UIBasis.GetAlbumArtistText(album.artist) // Künstler
            } // Column
        } // Row
    } // GetSongRow

    // Funktion zur Navigation zur Lyrics-Seite mit Jason-, Base64- und URL-kodierten Daten
    private fun navigateToLyrics(navController: NavHostController, song: Song, backRoute: String) {
        try {
            val songJson = Json.encodeToString(song) // Song-Daten in JSON konvertieren
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
} // SongFragment