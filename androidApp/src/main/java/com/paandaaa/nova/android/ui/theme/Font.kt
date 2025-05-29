package com.paandaaa.nova.android.ui.theme


import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.GoogleFont.Provider
import com.paandaaa.nova.android.R

private val poppinsFont = GoogleFont("Poppins")

private val fontProvider = Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val Poppins = FontFamily(
    Font(googleFont = poppinsFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = poppinsFont, fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = poppinsFont, fontProvider = fontProvider, weight = FontWeight.Bold),
)

