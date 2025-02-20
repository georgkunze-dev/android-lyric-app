package com.example.m28909_lyrixplorer

import android.content.Context
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.IOException

// Klasse zur Verarbeitung von Songdaten
class SongData {
    // Funktion zum Laden und Dekodieren der JSON-Daten in eine Liste von Alben
    fun getData(context: Context): List<Album>? {
        // JSON-Datei aus dem Asset-Verzeichnis laden
        val jsonString = loadJsonFromAsset(context, "songs.json")

        // Decodierung des JSON-String
        return jsonString?.let {
            try {
                val json = Json { ignoreUnknownKeys = true }
                json.decodeFromString<List<Album>>(jsonString)
            } catch (ex: Exception) {
                // Fehler bei der JSON-Dekodierung
                ex.printStackTrace()
                null
            }
        }
    } // getData

    // Funktion zum Laden der JSON-Datei aus dem Asset-Verzeichnis
    private fun loadJsonFromAsset(context: Context, fileName: String): String? {
        return try {
            // Öffnet die Datei und liest sie in einen String ein
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            // Fehler beim Lesen der Datei
            ex.printStackTrace()
            null
        }
    } // loadJsonFromAsset
} // SongData