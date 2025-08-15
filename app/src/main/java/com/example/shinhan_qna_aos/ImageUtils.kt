package com.example.shinhan_qna_aos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

//class ImageUtils {
//    companion object {
//        suspend fun compressImage(context: Context, imageUri: Uri, maxFileSizeMB: Int): File? {
//            return withContext(Dispatchers.IO) {
//                try {
//                    val inputStream = context.contentResolver.openInputStream(imageUri)
//                        ?: return@withContext null
//                    val originalBitmap = BitmapFactory.decodeStream(inputStream)
//                    inputStream.close()
//                    if (originalBitmap == null) return@withContext null
//
//                    val compressedFile =
//                        File(context.cacheDir, "compressed_image_${System.currentTimeMillis()}.jpg")
//                    var quality = 100
//                    var fileSizeKB: Long
//
//                    do {
//                        if (compressedFile.exists()) compressedFile.delete()
//                        FileOutputStream(compressedFile).use { outputStream ->
//                            val compressed = originalBitmap.compress(
//                                Bitmap.CompressFormat.JPEG,
//                                quality,
//                                outputStream
//                            )
//                            if (!compressed) return@withContext null
//                            outputStream.flush()
//                        }
//                        fileSizeKB = compressedFile.length() / 1024
//                        quality -= 5
//                    } while (fileSizeKB > maxFileSizeMB * 1024 && quality > 5)
//
//                    compressedFile
//                } catch (e: Exception) {
//                    null
//                }
//            }
//        }
//    }
//}