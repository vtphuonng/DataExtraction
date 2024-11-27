package filedecompressor

import java.io.{BufferedInputStream, BufferedOutputStream, File, FileInputStream, FileOutputStream}
import java.util.zip.{GZIPInputStream, ZipEntry, ZipInputStream}
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream
import java.nio.file.{Files, Paths}
import scala.util.Try
import java.io.IOException
import org.apache.commons.compress.archivers.tar.{TarArchiveEntry, TarArchiveInputStream}
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import com.github.junrar.Archive
import com.github.junrar.rarfile.FileHeader

object CompressionHandler {

  // Function to detect compression type based on the file extension
  def detectCompressionType(filePath: String): String = {
    if (filePath.endsWith(".zip")) "zip"
    else if (filePath.endsWith(".tar.gz") || filePath.endsWith(".tgz")) "tar.gz"
    else if (filePath.endsWith(".tar")) "tar"
    else if (filePath.endsWith(".gz")) "gz"
    else if (filePath.endsWith(".bz2")) "bz2"
    else if (filePath.endsWith(".rar")) "rar"
    else "unknown"
  }

  // Function to decompress a file based on its compression type
  def decompress(filePath: String, outputDir: String): Unit = {
    val compressionType = detectCompressionType(filePath)

    compressionType match {
      case "zip" => decompressZip(filePath, outputDir)
      case "tar.gz" | "tar" => decompressTar(filePath, outputDir)
      case "gz" => decompressGz(filePath, outputDir)
      case "bz2" => decompressBz2(filePath, outputDir)
      case "rar" => decompress(filePath, outputDir)
      case _ => throw new IllegalArgumentException(s"Unsupported file type for $filePath")
    }
  }

  // Function to decompress a list of files
  def decompressFiles(filePaths: List[String], outputDir: String): Unit = {
    Files.createDirectories(Paths.get(outputDir)) // Ensure the output directory exists
    filePaths.foreach { filePath =>
      Try {
        println(s"Decompressing: $filePath")
        decompress(filePath, outputDir)
        println(s"Successfully decompressed: $filePath")
      }.recover {
        case e: Exception => println(s"Failed to decompress $filePath: ${e.getMessage}")
      }
    }
  }

  // Decompress a ZIP file
  def decompressZip(filePath: String, outputDir: String): Unit = {
    val zipFile = new ZipInputStream(new BufferedInputStream(new FileInputStream(filePath)))
    try {
      var entry: ZipEntry = zipFile.getNextEntry
      while (entry != null) {
        val outputFile = new File(outputDir, entry.getName)
        if (entry.isDirectory) {
          outputFile.mkdirs()
        } else {
          outputFile.getParentFile.mkdirs() // Ensure parent directory exists
          val outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))
          try {
            val buffer = new Array[Byte](1024)
            var len = zipFile.read(buffer)    // Declare len after initializing the buffer
            while (len != -1) {
              outputStream.write(buffer, 0, len)
              len = zipFile.read(buffer)
            }
          } catch{
            case e: IOException => 
              throw new IOException(s"Error writing to file $outputFile: ${e.getMessage}", e)
          }
          finally {
            outputStream.close()
          }
        }
        zipFile.closeEntry()
        entry = zipFile.getNextEntry
      }
    } 
    catch {
      case e: IOException =>
        throw new IOException(s"Error reading ZIP file $filePath: ${e.getMessage}", e)
    }
    finally {
      zipFile.close()
    }
  }

  // Decompress a TAR file
  def decompressTar(filePath: String, outputDir: String): Unit = {
    // val fileStream = new FileInputStream(filePath)
    // val tarStream =  if (filePath.endsWith(".gz") || filePath.endsWith(".tgz"))
    // {
    //   new TarArchiveInputStream(
    //     new 
    //   )
    // }
    println("not supported")
  }

  // Decompress a GZ file
  def decompressGz(filePath: String, outputDir: String): Unit = {
    val outputFile = new File(outputDir, new File(filePath).getName.replace(".gz", ""))
    val inputStream = new GZIPInputStream(new BufferedInputStream(new FileInputStream(filePath)))
    val outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))
    try {
      val buffer = new Array[Byte](1024)
      var len = inputStream.read(buffer)
      //varStream.read(buffer)
      while (len != -1) {
        outputStream.write(buffer, 0, len)
        len = inputStream.read(buffer)
      }
    } finally {
      inputStream.close()
      outputStream.close()
    }
  }

  // Decompress a BZ2 file
  def decompressBz2(filePath: String, outputDir: String): Unit = {
    val outputFile = new File(outputDir, new File(filePath).getName.replace(".bz2", ""))
    val inStream = new org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream(
      new BufferedInputStream(new FileInputStream(filePath))
    )
    val outStream = new BufferedOutputStream(new FileOutputStream(outputFile))
    try {
      val buffer = new Array[Byte](1024)  // Correcialization with size
      var len = inStream.read(buffer)    // Declare len after initializing the buffer
      while (len != -1) {
        outStream.write(buffer, 0, len)
        len = inStream.read(buffer)
      }
    } finally {
      inStream.close()
      outStream.close()
    }
  }


  def decompressRar(filePath: String, outputDir: String): Unit = {
    val rarFile = new Archive(new File(filePath))
    try {
      val headers = rarFile.getFileHeaders
      headers.forEach { header =>
        val outputFile = new File(outputDir, header.getFileNameString.trim)
        if (header.isDirectory) {
          outputFile.mkdirs()
        } else {
          outputFile.getParentFile.mkdirs()
          val outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))
          try {
            rarFile.extractFile(header, outputStream)
          } finally {
            outputStream.close()
          }
        }
      }
    } finally {
      rarFile.close()
    }
  }

}
