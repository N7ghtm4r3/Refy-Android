package com.tecknobit.refy.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecknobit.refycore.records.LinksCollection
import java.util.Random

fun generateRandomColor() : Color {
    val random = Random()
    return Color(
        red = random.nextFloat(),
        green = random.nextFloat(),
        blue = random.nextFloat(),
        alpha = 1f
    )
}

/**
 * Function to get the color from its hex code
 *
 * @return color as [Color]
 */
fun String.toColor(): Color {
    return Color(("ff" + removePrefix("#").lowercase()).toLong(16))
}

fun Color.toHex(): String {
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    return String.format("#%02X%02X%02X", red, green, blue)
}

fun List<LinksCollection>.getLinksCollection(
    collectionId: String?
) : LinksCollection? {
    if(collectionId != null) {
        this.forEach { collection ->
            if(collection.id == collectionId)
                return collection
        }
    }
    return null
}