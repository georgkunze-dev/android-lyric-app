package com.example.m28909_lyrixplorer

import kotlinx.serialization.Serializable

// Datenklasse für ein Album
@Serializable
data class Album(
    val album: String,    // Name des Albums
    val artist: String,   // Name des Künstlers
    val imgName: String,  // Name des Bildes, das mit dem Album verbunden ist
    val songs: List<Song> // Liste von Songs, die zum Album gehören
) // Album