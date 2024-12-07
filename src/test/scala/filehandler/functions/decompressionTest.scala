//package dataframehandler

import org.scalatest.funsuite.AnyFunSuite
import java.io._
import java.io.{File, FileNotFoundException, BufferedOutputStream, FileOutputStream, IOException}
import java.nio.file.{Files, Paths}
import java.util.zip.{ZipOutputStream, ZipEntry, GZIPOutputStream}
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream
import java.nio.file.StandardCopyOption
import scala.util.Try
import org.scalatest.funsuite.AnyFunSuite
import org.apache.commons.compress.archivers.tar.{TarArchiveEntry, TarArchiveInputStream, TarArchiveOutputStream}
import org.apache.commons.compress.compressors.gzip.{GzipCompressorInputStream, GzipCompressorOutputStream}
import filedecompressor.CompressionHandler

class TypeConvertorTest extends AnyFunSuite {

  test("detectCompressionType should detect zip file") {
    assert(CompressionHandler.detectCompressionType("test.zip") == "zip")
  }

  test("detectCompressionType should detect tar.gz file") {
    assert(CompressionHandler.detectCompressionType("test.tar.gz") == "tar.gz")
  }

  test("detectCompressionType should detect gz file") {
    assert(CompressionHandler.detectCompressionType("test.gz") == "gz")
  }

  test("detectCompressionType should detect bz2 file") {
    assert(CompressionHandler.detectCompressionType("test.bz2") == "bz2")
  }

  test("detectCompressionType should detect rar file") {
    assert(CompressionHandler.detectCompressionType("test.rar") == "rar")
  }

  test("detectCompressionType should return unknown for unsupported file type") {
    assert(CompressionHandler.detectCompressionType("test.txt") == "unknown")
  }

  test("decompress should throw exception for unsupported file type") {
    intercept[IllegalArgumentException] {
      CompressionHandler.decompress("test.txt", "output")
    }
  }

  test("decompressFiles should process multiple files") {
    val tempDir = Files.createTempDirectory("decompressTest")
    val filePaths = List("test.zip", "test.tar.gz", "test.bz2")
    
    // Temporarily creating dummy files for testing
    filePaths.foreach { file =>
      val tempFile = new File(tempDir.toFile, file)
      new PrintWriter(tempFile) { write("dummy content"); close() }
    }

    CompressionHandler.decompressFiles(filePaths, tempDir.toString)

    // Check if decompressed files exist in output directory
    filePaths.foreach { file =>
      assert(new File(tempDir.toFile, file).exists())
    }

    // Cleanup
    tempDir.toFile.delete()
  }

  test("decompressZip should decompress zip files correctly") {
    val tempDir = Files.createTempDirectory("decompressZipTest")
    val zipFile = new File(tempDir.toFile, "test.zip")
    
    // Create a dummy ZIP file
    val zipOut = new ZipOutputStream(new FileOutputStream(zipFile))
    zipOut.putNextEntry(new ZipEntry("test.txt"))
    zipOut.write("dummy content".getBytes())
    zipOut.closeEntry()
    zipOut.close()

    CompressionHandler.decompressZip(zipFile.getAbsolutePath, tempDir.toString)

    // Check if file is decompressed
    assert(new File(tempDir.toFile, "test.txt").exists())

    // Cleanup
    zipFile.delete()
    tempDir.toFile.delete()
  }

  test("decompressGz should decompress gzipped files correctly") {
    val tempDir = Files.createTempDirectory("decompressGzTest")
    val gzFile = new File(tempDir.toFile, "test.gz")
    
    // Create a dummy GZ file
    val gzOut = new GZIPOutputStream(new FileOutputStream(gzFile))
    gzOut.write("dummy content".getBytes())
    gzOut.close()

    CompressionHandler.decompressGz(gzFile.getAbsolutePath, tempDir.toString)

    // Check if file is decompressed
    assert(new File(tempDir.toFile, "test").exists())

    // Cleanup
    gzFile.delete()
    tempDir.toFile.delete()
  }

  test("decompressBz2 should decompress bz2 files correctly") {
    val tempDir = Files.createTempDirectory("decompressBz2Test")
    val bz2File = new File(tempDir.toFile, "test.bz2")
    
    // Create a dummy BZ2 file
    val bz2Out = new BZip2CompressorOutputStream(new FileOutputStream(bz2File))
    bz2Out.write("dummy content".getBytes())
    bz2Out.close()

    CompressionHandler.decompressBz2(bz2File.getAbsolutePath, tempDir.toString)

    // Check if file is decompressed
    assert(new File(tempDir.toFile, "test").exists())

    // Cleanup
    bz2File.delete()
    tempDir.toFile.delete()
  }


  test("decompressRar should decompress rar files correctly") {
    val tempDir = Files.createTempDirectory("decompressRarTest")
    val rarFile = new File(tempDir.toFile, "test.rar")

    try {

        val sourceRar = getClass.getResourceAsStream("/test.rar") 
        if (sourceRar == null) 
          { 
            fail("Resource test.rar not found") 
          }      
        // Copy a test RAR file from resources to the temporary directory
        //val sourceRar = getClass.getResourceAsStream("test.rar")
        Files.copy(sourceRar, rarFile.toPath, StandardCopyOption.REPLACE_EXISTING)

        // Call the method to test
        CompressionHandler.decompressRar(rarFile.getAbsolutePath, tempDir.toString)

        // Assert that the expected decompressed file exists
        assert(new File(tempDir.toFile, "test.txt").exists())
    } finally {
        // Cleanup
        def deleteRecursively(file: File): Unit = {
        if (file.isDirectory) file.listFiles().foreach(deleteRecursively)
        file.delete()
        }
        deleteRecursively(tempDir.toFile)
    }
    //rarFile.delete()
    //tempDir.toFile.delete()
    }
  

  def createTarFile(outputPath: String, files: Map[String, String]): Unit = {
    val tarOut = new TarArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(outputPath)))
    try {
      files.foreach { case (fileName, content) =>
        val entry = new TarArchiveEntry(fileName)
        val contentBytes = content.getBytes("UTF-8")
        entry.setSize(contentBytes.length)
        tarOut.putArchiveEntry(entry)
        tarOut.write(contentBytes)
        tarOut.closeArchiveEntry()
      }
    } finally {
      tarOut.close()
    }
  }

  def createTarGzFile(outputPath: String, files: Map[String, String]): Unit = {
    val tarOut = new TarArchiveOutputStream(
      new GzipCompressorOutputStream(
        new BufferedOutputStream(new FileOutputStream(outputPath))
      )
    )
    try {
      files.foreach { case (fileName, content) =>
        val entry = new TarArchiveEntry(fileName)
        val contentBytes = content.getBytes("UTF-8")
        entry.setSize(contentBytes.length)
        tarOut.putArchiveEntry(entry)
        tarOut.write(contentBytes)
        tarOut.closeArchiveEntry()
      }
    } finally {
      tarOut.close()
    }
  }

  test("Decompress a .tar file") {
    val tempDir = Files.createTempDirectory("tar_test").toFile
    val tarFile = new File(tempDir, "test.tar")
    val outputDir = new File(tempDir, "output")
    outputDir.mkdirs()

    val filesToCompress = Map(
      "file1.txt" -> "This is the content of file1.",
      "file2.txt" -> "This is the content of file2."
    )
    createTarFile(tarFile.getAbsolutePath, filesToCompress)

    CompressionHandler.decompressTar(tarFile.getAbsolutePath, outputDir.getAbsolutePath)

    // Assertions
    filesToCompress.foreach { case (fileName, content) =>
      val extractedFile = new File(outputDir, fileName)
      assert(extractedFile.exists())
      assert(new String(Files.readAllBytes(extractedFile.toPath), "UTF-8") == content)
    }
  }

  test("Decompress a .tar.gz file") {
    val tempDir = Files.createTempDirectory("tar_gz_test").toFile
    val tarGzFile = new File(tempDir, "test.tar.gz")
    val outputDir = new File(tempDir, "output")
    outputDir.mkdirs()

    val filesToCompress = Map(
      "file1.txt" -> "This is the content of file1.",
      "file2.txt" -> "This is the content of file2."
    )
    createTarGzFile(tarGzFile.getAbsolutePath, filesToCompress)

    CompressionHandler.decompressTar(tarGzFile.getAbsolutePath, outputDir.getAbsolutePath)

    // Assertions
    filesToCompress.foreach { case (fileName, content) =>
      val extractedFile = new File(outputDir, fileName)
      assert(extractedFile.exists())
      assert(new String(Files.readAllBytes(extractedFile.toPath), "UTF-8") == content)
    }
  }

  test("Decompress an empty .tar file") {
    val tempDir = Files.createTempDirectory("empty_tar_test").toFile
    val tarFile = new File(tempDir, "empty.tar")
    val outputDir = new File(tempDir, "output")
    outputDir.mkdirs()

    createTarFile(tarFile.getAbsolutePath, Map.empty)

    CompressionHandler.decompressTar(tarFile.getAbsolutePath, outputDir.getAbsolutePath)

    // Assertions
    assert(outputDir.listFiles().isEmpty)
  }

  test("Handle nonexistent .tar file") {
    val outputDir = Files.createTempDirectory("nonexistent_tar_test").toFile
    val nonExistentFile = "nonexistent.tar"

    assertThrows[FileNotFoundException] {
      CompressionHandler.decompressTar(nonExistentFile, outputDir.getAbsolutePath)
    }
  }    
}
