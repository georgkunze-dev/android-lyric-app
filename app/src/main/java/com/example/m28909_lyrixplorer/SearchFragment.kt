package com.example.m28909_lyrixplorer

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController

// Fragment zur Darstellung der Songsuche
class SearchFragment {

    // Funktion zur Anzeige des Such-UI und der Ergebnisse
    @Composable
    fun GetFragment(context: Context, albums: List<Album>?, navController: NavHostController) {
        val searchQuery = remember { mutableStateOf("") } // MutableState zum Speichern der Suchanfrage

        // Filtern der Songs, wenn Alben verfügbar sind
        val filteredSongs = remember(searchQuery.value, albums) {
            if (albums.isNullOrEmpty()) {
                null
            } else {
                /* Durchsuchen aller Alben nach Songs, deren Titel die Suchanfrage enthält
                   (Groß-/Kleinschreibung wird ignoriert) */
                albums.flatMap { album ->
                    album.songs.filter { song ->
                        song.title.contains(searchQuery.value, ignoreCase = true)
                    }.map { song -> album to song }
                }
            }
        } // remember

        // Layout für die Suchseite
        Column(modifier = Modifier.fillMaxSize()) {
            UIBasis.GetSiteTitle("Search") // Seitentitel
            // Reihe für das Such-Icon und -Feld
            Row(
                modifier = Modifier.background(Color.DarkGray),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UIBasis.GetSearchIcon()// Such-Icon
                // Suchfeld
                UIBasis.GetTextField(
                    caption = "Search Song...",
                    textValue = searchQuery,
                    keyboardType = KeyboardType.Text
                )
            } // Row
            UIBasis.GetSpacerHeight()
            // Anzeigen der Songs nach der Suche
            SongFragment().GetFragmentForSearchResults(context = context, searchResults = filteredSongs, navController)
        } // Column
    } // GetFragment
} // SearchFragment