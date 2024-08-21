package com.tecknobit.refy.ui

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.ui.graphics.Color
import com.tecknobit.refy.ui.activities.navigation.SplashScreen.Companion.localUser
import com.tecknobit.refycore.records.RefyItem
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Random
import kotlin.math.min

/**
 * Function to generate a random color for a collection
 *
 * No-any params required
 *
 * @return random color as [Color]
 */
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
 * No-any params required
 *
 * @return color as [Color]
 */
fun String.toColor(): Color {
    return Color(("ff" + removePrefix("#").lowercase()).toLong(16))
}

/**
 * Function to transform a [Color] value in the corresponding hex code
 *
 * No-any params required
 *
 * @return hex code of the color as [String]
 */
fun Color.toHex(): String {
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    return String.format("#%02X%02X%02X", red, green, blue)
}

/**
 * Function to get from a [RefyItem] list the item with the corresponding to the identifier pass
 * as parameter
 *
 * @param itemId: the item identifier
 *
 * @return the corresponding item, if exists as [T], null if not exists
 *
 * @param T: the type of the item in the list
 */
fun <T : RefyItem> List<T>.getRefyItem(
    itemId: String?
) : T? {
    if(itemId != null) {
        this.forEach { item ->
            if(item.id == itemId)
                return item
        }
    }
    return null
}

/**
 * Function to get the complete media url with the current [localUser.hostAddress] value to display
 * a media item such profile pictures or logo pictures
 *
 * @param relativeMediaUrl: the media relative url
 *
 * @return the complete media url as [String]
 */
fun getCompleteMediaItemUrl(
    relativeMediaUrl: String
) : String {
    return "${localUser.hostAddress}/$relativeMediaUrl"
}

/**
 * Function to get the complete file path of an file
 *
 * @param context: the context where the file is needed
 * @param uri: the uri of the file
 * @return the path of the file
 */
fun getFilePath(
    context: Context,
    uri: Uri
): String? {
    val returnCursor = context.contentResolver.query(uri, null, null, null, null)
    val nameIndex =  returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    returnCursor.getLong(sizeIndex).toString()
    val file = File(context.filesDir, name)
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        var read = 0
        val maxBufferSize = 1 * 1024 * 1024
        val bytesAvailable: Int = inputStream?.available() ?: 0
        val bufferSize = min(bytesAvailable, maxBufferSize)
        val buffers = ByteArray(bufferSize)
        while (inputStream?.read(buffers).also {
                if (it != null) {
                    read = it
                }
            } != -1) {
            outputStream.write(buffers, 0, read)
        }
        inputStream?.close()
        outputStream.close()
    } catch (_: Exception) {
    } finally {
        returnCursor.close()
    }
    return file.path
}