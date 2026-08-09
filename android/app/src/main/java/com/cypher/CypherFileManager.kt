package com.cypher

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class CypherFileManager(private val context: Context) {

    companion object {
        private const val TAG = "CypherFileManager"
    }

    fun listFiles(directory: File = context.filesDir): List<File> {
        return try {
            directory.listFiles()?.toList() ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to list files", e)
            emptyList()
        }
    }

    fun extractZip(zipFile: File, targetDirectory: File): Boolean {
        return try {
            targetDirectory.mkdirs()
            ZipInputStream(FileInputStream(zipFile)).use { zis ->
                var zipEntry: ZipEntry? = zis.nextEntry
                while (zipEntry != null) {
                    val newFile = File(targetDirectory, zipEntry.name)
                    if (zipEntry.isDirectory) {
                        newFile.mkdirs()
                    } else {
                        newFile.parentFile?.mkdirs()
                        FileOutputStream(newFile).use { fos ->
                            val buffer = ByteArray(1024)
                            var len: Int
                            while (zis.read(buffer).also { len = it } > 0) {
                                fos.write(buffer, 0, len)
                            }
                        }
                    }
                    zipEntry = zis.nextEntry
                }
                zis.closeEntry()
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "ZIP extraction failed", e)
            false
        }
    }

    fun compressToZip(filesToZip: List<File>, outputFile: File): Boolean {
        return try {
            ZipOutputStream(FileOutputStream(outputFile)).use { zos ->
                for (file in filesToZip) {
                    if (file.exists()) {
                        FileInputStream(file).use { fis ->
                            zos.putNextEntry(ZipEntry(file.name))
                            val buffer = ByteArray(1024)
                            var len: Int
                            while (fis.read(buffer).also { len = it } > 0) {
                                zos.write(buffer, 0, len)
                            }
                            zos.closeEntry()
                        }
                    }
                }
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "ZIP compression failed", e)
            false
        }
    }
}
