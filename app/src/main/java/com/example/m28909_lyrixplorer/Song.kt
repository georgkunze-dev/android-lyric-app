package com.example.m28909_lyrixplorer

import kotlinx.serialization.Serializable

// Datenklasse für einen Song
@Serializable
data class Song(
    val id: Int,        // Eindeutige ID des Songs
    val title: String,  // Titel des Songs
    val lyricsHtml: String,  // HTML-Code für die Songlyrics
    val playerHtml: String   // HTML-Teil für den Player
) // Song