package com.example.m28909_lyrixplorer

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp

// Bildschirm zur Anzeige der Songtexte
class LyricsScreen {
    // Hauptfunktion zur Anzeige der Lyrics-Seite
    @Composable
    fun ShowLyricsScreen(songJson: String, navController: NavHostController, backRouteJson: String) {
        // Deserialisierung der JSON Strings
        val song = remember {
            try {
                Json.decodeFromString<Song>(songJson)
            } catch (e: Exception) {
                // Fehler bei Deserialisierung
                Log.e("LyricsScreen", "Error deserializing song JSON: ${e.message}")
                null
            }
        } // remember

        val backRoute = remember {
            try {
                Json.decodeFromString<String>(backRouteJson)
            } catch (e: Exception) {
                // Fehler bei Deserialisierung
                Log.e("LyricsScreen", "Error deserializing backRoute JSON: ${e.message}")
                null
            }
        } // remember

        // Wenn Song oder BackRoute null sind, wird die Komposition beendet
        if (song == null || backRoute == null) {
            return
        } else {
            // Layout für die Lyrics-Seite
            Column (
                modifier = Modifier.fillMaxSize()
            ) {
                // Reihe mit Zurück-Button und Titel
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.background(Color.DarkGray)
                ) {
                    UIBasis.GetBackIcon(navController, backRoute) // Zurückbutton
                    UIBasis.GetLyricSiteTitle() // Seitentitel
                } // Row

                // Box zur Anzeige der Song-Lyrics im WebView
                Box(modifier = Modifier.weight(1f)) {
                    GetWebView(htmlContent = song.lyricsHtml) // WebView zeigt die Lyrics
                } // Box

                // Statischer Abschnitt am unteren Bildschirmrand für den Player
                Surface(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(bottom = 80.dp)
                ) {
                    GetWebView(htmlContent = song.playerHtml) // WebView zeigt den Player
                } // Surface
            } // Column
        } // else
    } // ShowLyricsScreen

    // Funktion zur Anzeige eines WebViews mit HTML-Inhalten
    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    fun GetWebView(htmlContent: String) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                // Aktivierung von JavaScript und anderen WebView-Einstellungen
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                }
                // WebViewClient zum Handling von Seitenlade- und Fehlerereignissen
                webViewClient = object : WebViewClient() {
                    // Fehler beim Laden der Seite protokollieren
                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        Log.e("WebView", "Error: ${error?.description}")
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d("WebView", "Page loaded: $url")
                    }
                }
                // Laden der HTML-Inhalte in den WebView
                loadDataWithBaseURL("https://genius.com", htmlContent, "text/html", "UTF-8", null)
            } // WebView
        }) // AndroidView
    } // GetWebView
} // LyricsScreen