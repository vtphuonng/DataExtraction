package filecompressor

import java.io._ 
import java.nio.file._ 
import java.util.zip._ 
import org.apache.commons.compress.compressors.gzip._ 
import org.apache.commons.compress.archivers.tar._

object CompressionHandler {

  // Compress a single file into a .gz file
  def gzipCompress(sourceFilePath: String, outputFilePath: String): Unit = {
    val sourceFile = new File(sourceFilePath)
    if (!sourceFile.exists() || sourceFile.isDirectory) {
      throw new IllegalArgumentException(s"Source path $sourceFilePath is not a valid file")
    }

    val outputFile = new File(outputFilePath)
    val fos = new FileOutputStream(outputFile)
    val gzos = new GZIPOutputStream(fos)

    try {
      val fis = new FileInputStream(sourceFile)
      try {
        val buffer = new Array[Byte](1024) 
        var length = fis.read(buffer)
        while (length > 0) {
          gzos.write(buffer, 0, length)
          length = fis.read(buffer)
        }
      } finally {
        fis.close() // Ensure the input file stream is closed after processing
      }
    } finally {
      gzos.close() // Ensure the GZIPOutputStream is closed
      fos.close()  // Ensure the output file stream is closed
    }

    println(s"File $sourceFilePath has been successfully compressed to $outputFilePath")
  }
}