package com.example.passportphotocomparisonthesis.NFCAndChipAuthentication.Data

import android.graphics.Bitmap
import org.jmrtd.lds.SODFile
import org.jmrtd.lds.icao.DG1File
import org.jmrtd.lds.icao.DG2File

data class PassportData(
    val dg1File: DG1File,
    val dg2File: DG2File,
    val sodFile: SODFile,
    val imageBase64: String?,
    val bitmap: Bitmap?
)