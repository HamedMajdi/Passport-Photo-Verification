package com.example.passportphotocomparisonthesis.NFCAndChipAuthentication.Data

import android.content.Context
import android.graphics.Bitmap
import android.nfc.tech.IsoDep
import android.util.Base64
import android.util.Log
import com.example.passportphotocomparisonthesis.Utils.Image.ImageUtil.decodeImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.sf.scuba.smartcards.CardService
import org.jmrtd.BACKeySpec
import org.jmrtd.PassportService
import org.jmrtd.lds.CardAccessFile
import org.jmrtd.lds.PACEInfo
import org.jmrtd.lds.SODFile
import org.jmrtd.lds.SecurityInfo
import org.jmrtd.lds.icao.DG1File
import org.jmrtd.lds.icao.DG2File
import org.jmrtd.lds.iso19794.FaceImageInfo
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.InputStream

class MyRepository {
    suspend fun readPassportData(isoDep: IsoDep, bacKey: BACKeySpec, context: Context): PassportData {
        return withContext(Dispatchers.IO) {
            isoDep.timeout = 10000

            val cardService = CardService.getInstance(isoDep)
            cardService.open()
            val service = PassportService(
                cardService,
                PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
                PassportService.DEFAULT_MAX_BLOCKSIZE,
                false,
                false,
            )
            service.open()
            var paceSucceeded = false
            try {
                val cardAccessFile = CardAccessFile(service.getInputStream(PassportService.EF_CARD_ACCESS))
                val securityInfoCollection = cardAccessFile.securityInfos
                for (securityInfo: SecurityInfo in securityInfoCollection) {
                    if (securityInfo is PACEInfo) {
                        service.doPACE(
                            bacKey,
                            securityInfo.objectIdentifier,
                            PACEInfo.toParameterSpec(securityInfo.parameterId),
                            null,
                        )
                        paceSucceeded = true
                    }
                }
            } catch (e: Exception) {
                Log.w("NFC", e)
            }
            service.sendSelectApplet(paceSucceeded)
            if (!paceSucceeded) {
                try {
                    service.getInputStream(PassportService.EF_COM).read()
                } catch (e: Exception) {
                    service.doBAC(bacKey)
                }
            }
            val dg1In = service.getInputStream(PassportService.EF_DG1)
            val dg1File = DG1File(dg1In)
            val dg2In = service.getInputStream(PassportService.EF_DG2)
            val dg2File = DG2File(dg2In)
            val sodIn = service.getInputStream(PassportService.EF_SOD)
            val sodFile = SODFile(sodIn)

            val allFaceImageInfo: MutableList<FaceImageInfo> = ArrayList()
            dg2File.faceInfos.forEach {
                allFaceImageInfo.addAll(it.faceImageInfos)
            }
            var imageBase64: String? = null
            var bitmap: Bitmap? = null
            if (allFaceImageInfo.isNotEmpty()) {
                val faceImageInfo = allFaceImageInfo.first()
                val imageLength = faceImageInfo.imageLength
                val dataInputStream = DataInputStream(faceImageInfo.imageInputStream)
                val buffer = ByteArray(imageLength)
                dataInputStream.readFully(buffer, 0, imageLength)
                val inputStream: InputStream = ByteArrayInputStream(buffer, 0, imageLength)

                bitmap = decodeImage(context, faceImageInfo.mimeType, inputStream)
                imageBase64 = Base64.encodeToString(buffer, Base64.DEFAULT)
            }

            PassportData(
                dg1File,
                dg2File,
                sodFile,
                imageBase64,
                bitmap,
            )
        }
    }

}

