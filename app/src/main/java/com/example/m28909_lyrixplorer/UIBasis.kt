package com.example.m28909_lyrixplorer

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

// Utility-Klasse für UI-Komponenten
class UIBasis {

    companion object {

        // Funktion zur Anzeige eines vertikalen Abstands
        @Composable
        fun GetSpacerHeight(height: Double = 10.0) {
            Spacer(modifier = Modifier.height(height.dp))
        } // GetSpacerHeight

        // Funktion zur Anzeige eines horizontalen Abstands
        @Composable
        fun GetSpacerWidth(width: Double = 10.0) {
            Spacer(modifier = Modifier.width(width.dp))
        } // GetSpacerWidth

        // Funktion zur Anzeige des Seitentitels
        @Composable
        fun GetSiteTitle(text: String) {
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.DarkGray)
                    .padding(0.dp, 10.dp)
            )
        } // GetSiteTitle

        // Funktion zur Anzeige des Titels auf der Lyrics-Seite
        @Composable
        fun GetLyricSiteTitle() {
            Text(
                text = "Lyrics",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.DarkGray)
                    .padding(start = 0.dp, top = 10.dp, end = 40.dp, bottom = 10.dp)
            )
        } // GetLyrikSiteTitle

        // Funktion zur Anzeige einer Überschrift
        @Composable
        fun GetHeadlineText(text: String) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        } // GetHeadlineText

        // Funktion zur Anzeige eines Zurück-Icons mit Navigation
        @Composable
        fun GetBackIcon(navController: NavHostController, navRoute: String) {
            Icon(
                painter = painterResource(id = R.drawable.back_icon),
                contentDescription = "Back Icon",
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 10.dp)
                    .clickable {
                        navController.navigate(navRoute)
                    }
            )
        } // GetBackIcon

        // Funktion zur Anzeige eines Such-Icons
        @Composable
        fun GetSearchIcon() {
            Icon(
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = "Search Icon",
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 10.dp, end = 10.dp)
            )
        } // GetSearchIcon

        // Funktion zur Anzeige eines Bildes basierend auf dem Albumnamen
        @SuppressLint("DiscouragedApi")
        @Composable
        fun GetImage(context: Context, albumName: String, size: Dp) {
            // Ressourcen-ID basierend auf dem Albumnamen erhalten
            val imageResourceId = context.resources.getIdentifier(
                albumName.lowercase(), "drawable", context.packageName
            )

            Image(
                painter = painterResource(id = imageResourceId),
                contentDescription = "My Image",
                modifier = Modifier.size(size)
            )
        } // GetImage

        // Funktion zur Anzeige des Songtitels
        @Composable
        fun GetSongTitleText(text: String) {
            Text(
                text = text,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
        } // GetSongTitleText

        // Funktion zur Anzeige des Album-Künstlernamens
        @Composable
        fun GetAlbumArtistText(text: String) {
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.fillMaxWidth()
            )
        } // GetAlbumArtistText

        // Funktion zur Anzeige eines Textfelds für die Suche
        @Composable
        fun GetTextField(
            caption: String,
            textValue: MutableState<String>,
            keyboardType: KeyboardType,
            readonly: Boolean = false
        ) {
            TextField(
                value = textValue.value.toString(),
                onValueChange = {
                    textValue.value = it
                },
                // enabled = false,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,  // FontWeight.W800,
                    //fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Monospace,
                    //letterSpacing = 0.5.em,
                    background = MaterialTheme.colorScheme.background,
                    //textDecoration = TextDecoration.Underline
                ),
                singleLine = true,
                readOnly = readonly,
                label = {
                    Text(
                        text = caption,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier.fillMaxWidth()
            ) // TextField
        } // GetTextField
    }
} // UIBasis