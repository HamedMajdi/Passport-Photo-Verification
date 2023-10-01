package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Utilities

import android.content.Context
import androidx.annotation.RawRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JsonReader {

    suspend fun readJsonFromRaw(context: Context, @RawRes fileId: Int): String {
        return withContext(Dispatchers.IO) {
            context.resources.openRawResource(fileId).bufferedReader().use { it.readText() }
        }
    }
}
