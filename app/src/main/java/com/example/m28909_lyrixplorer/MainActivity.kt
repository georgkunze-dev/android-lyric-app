package com.example.m28909_lyrixplorer

import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.m28909_lyrixplorer.ui.theme.M28909_LyriXplorerTheme
import java.nio.charset.StandardCharsets

// Die Hauptaktivität der Anwendung, die beim Start der App aufgerufen wird
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            M28909_LyriXplorerTheme {
                // Definiert eine Oberfläche, die den gesamten Bildschirm ausfüllt und eine Hintergrundfarbe aus dem Theme übernimmt
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LyriXplorer() // Ruft die Haupt-UI-Komponente der App auf
                } // Surface
            }
        }
    } // onCreate
} // MainActivity

// Datenklasse zur Speicherung von Navigationsleisten-Elementen
data class NavigationBarData(val label: String, val id: Int)

// Komposition, die eine Liste von Navigationsleisten-Elementen zurückgibt
@Composable
private fun navigationbarItemsList(): List<NavigationBarData> {
    val liste = arrayListOf<NavigationBarData>()
    // Hinzufügen von Elementen zur Navigationsleiste mit Labels aus Ressourcen und Icons
    liste.add(NavigationBarData(stringResource(id = R.string.home), R.drawable.home_icon))
    liste.add(NavigationBarData(stringResource(id = R.string.song), R.drawable.song_icon))
    liste.add(NavigationBarData(stringResource(id = R.string.album), R.drawable.album_icon))
    liste.add(NavigationBarData(stringResource(id = R.string.search), R.drawable.search_icon))
    return liste
} // navigationbarItemsList

// Haupt-Komposition, die die LyriXplorer-App darstellt
@Composable
fun LyriXplorer() {
    val selectedIndex = remember { mutableIntStateOf(0) } // aktuell ausgewählter Index der Navigationsleiste
    val navController = rememberNavController() // Initialisierung des Navigation Controller für die Seiten-Navigation
    val context = LocalContext.current // Aktueller Kontext
    val albums = remember { mutableStateOf<List<Album>?>(null) } //Liste von Alben, die später geladen wird
    // Definition der Farben für die Navigationsleisten-Elemente
    val colors = NavigationBarItemDefaults.colors(
        Color.Black, // selected Icon Color
        Color.Green, // selected Text Color
        Color.Green, // indicatorColor
        Color.White, // unselected Icon Color
        Color.White  // unselected Text Color
    )
    // Erstellen der verschiedenen Fragment-Instanzen
    val homeFragment = remember { HomeFragment() }
    val songFragment = remember { SongFragment() }
    val albumFragment = remember { AlbumFragment() }
    val searchFragment = remember { SearchFragment() }

    // Laden der Alben im Hintergrund bei der Initialisierung der Komposition
    LaunchedEffect(Unit) {
        albums.value = SongData().getData(context)
    }

    // Box-Container für das gesamte Layout
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray))
    {
        Column {
            NavHost(navController = navController, startDestination = "home") {
                // Start-Seite
                composable(Konstanten.HOME) {
                    homeFragment.GetFragment(context, albums.value, navController)
                }
                // Song-Seite zum Anzeigen aller Songs
                composable(Konstanten.SONG) {
                    songFragment.GetAllSongsFragment(context, albums.value, navController)
                }
                // Album-Seite
                composable(Konstanten.ALBUM) {
                    albumFragment.GetFragment(context, albums.value, navController)
                }
                // Search-Seite
                composable(Konstanten.SEARCH) {
                    searchFragment.GetFragment(context, albums.value, navController)
                }
                // Song-Seite zum Anzeigen der Songs in einem Album
                composable(Konstanten.ALBUMSONGS + "{albumJson}") {backStackEntry ->
                    // Extrahieren aus der NavHost-Route und Base64-Decodieren
                    val albumJson = backStackEntry.arguments?.getString("albumJson")?.let { decodeBase64String(it) }
                    if (albumJson != null) {
                        SongFragment().GetAlbumSongs(context, albumJson, navController)
                    }
                }
                // Lyrik-Seite zum anzeigen der Songlyrics
                composable(Konstanten.LYRICS + "{songJson}/{backRoute}") { backStackEntry ->
                    // Extrahieren aus der NavHost-Route und Base64-Decodieren
                    val songJson = backStackEntry.arguments?.getString("songJson")?.let { decodeBase64String(it) }
                    val backRouteJson = backStackEntry.arguments?.getString("backRoute")?.let { decodeBase64String(it) }
                    if (songJson != null && backRouteJson != null) {
                        LyricsScreen().ShowLyricsScreen(songJson = songJson, navController, backRouteJson)
                    }
                }
            } // NavHost
        } // Column

        // Navigationsleiste am unteren Rand des Bildschirms
        NavigationBar(
            modifier = Modifier.align(alignment = Alignment.BottomCenter),
            containerColor = Color.DarkGray
            ) {
            // Erstellen der Navigationsleisten-Items und Hinzufügen eines onClick-Listeners
            navigationbarItemsList().forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = item.id), "") },
                    colors = colors,
                    label = { Text(text = item.label) },
                    selected = selectedIndex.intValue == index,
                    onClick = {
                        // Aktualisierung des Index beim Klicken und Navigation zur entsprechenden Seite
                        selectedIndex.intValue = index
                        when (selectedIndex.intValue) {
                            0 -> navController.navigate(Konstanten.HOME)
                            1 -> navController.navigate(Konstanten.SONG)
                            2 -> navController.navigate(Konstanten.ALBUM)
                            3 -> navController.navigate(Konstanten.SEARCH)
                        }
                    } // onClick
                )
            } // navigationbarItemsList
        } // NavigationBar
    } // Box
} // LyriXplorer

// Funktion zum Decodieren von BASE64-Strings, um JSON-Daten zu erhalten
fun decodeBase64String(encodedJson: String): String {
    // JSON-String BASE64-decodieren
    val albumJsonBytes = Base64.decode(encodedJson, Base64.DEFAULT)
    return String(albumJsonBytes, StandardCharsets.UTF_8)
} // decodeBase64String

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    M28909_LyriXplorerTheme {
        LyriXplorer()
    }
} // GreetingPreview