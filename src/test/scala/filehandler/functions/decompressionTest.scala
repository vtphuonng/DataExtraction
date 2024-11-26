// //package dataframehandler

// import org.scalatest.funsuite.AnyFunSuite
// import java.io._
// import java.nio.file.{Files, Paths}
// import java.util.zip.{ZipOutputStream, ZipEntry, GZIPOutputStream}
// import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream

// import scala.util.Try

// import filedecompressor.CompressionHandler

// class TypeConvertorTest extends AnyFunSuite {

//   test("detectCompressionType should detect zip file") {
//     assert(CompressionHandler.detectCompressionType("test.zip") == "zip")
//   }

//   test("detectCompressionType should detect tar.gz file") {
//     assert(CompressionHandler.detectCompressionType("test.tar.gz") == "tar.gz")
//   }

//   test("detectCompressionType should detect gz file") {
//     assert(CompressionHandler.detectCompressionType("test.gz") == "gz")
//   }

//   test("detectCompressionType should detect bz2 file") {
//     assert(CompressionHandler.detectCompressionType("test.bz2") == "bz2")
//   }

//   test("detectCompressionType should return unknown for unsupported file type") {
//     assert(CompressionHandler.detectCompressionType("test.txt") == "unknown")
//   }

//   test("decompress should throw exception for unsupported file type") {
//     intercept[IllegalArgumentException] {
//       CompressionHandler.decompress("test.txt", "output")
//     }
//   }

//   test("decompressFiles should process multiple files") {
//     val tempDir = Files.createTempDirectory("decompressTest")
//     val filePaths = List("test.zip", "test.tar.gz", "test.bz2")
    
//     // Temporarily creating dummy files for testing
//     filePaths.foreach { file =>
//       val tempFile = new File(tempDir.toFile, file)
//       new PrintWriter(tempFile) { write("dummy content"); close() }
//     }

//     CompressionHandler.decompressFiles(filePaths, tempDir.toString)

//     // Check if decompressed files exist in output directory
//     filePaths.foreach { file =>
//       assert(new File(tempDir.toFile, file).exists())
//     }

//     // Cleanup
//     tempDir.toFile.delete()
//   }

//   test("decompressZip should decompress zip files correctly") {
//     val tempDir = Files.createTempDirectory("decompressZipTest")
//     val zipFile = new File(tempDir.toFile, "test.zip")
    
//     // Create a dummy ZIP file
//     val zipOut = new ZipOutputStream(new FileOutputStream(zipFile))
//     zipOut.putNextEntry(new ZipEntry("test.txt"))
//     zipOut.write("dummy content".getBytes())
//     zipOut.closeEntry()
//     zipOut.close()

//     CompressionHandler.decompressZip(zipFile.getAbsolutePath, tempDir.toString)

//     // Check if file is decompressed
//     assert(new File(tempDir.toFile, "test.txt").exists())

//     // Cleanup
//     zipFile.delete()
//     tempDir.toFile.delete()
//   }

//   test("decompressGz should decompress gzipped files correctly") {
//     val tempDir = Files.createTempDirectory("decompressGzTest")
//     val gzFile = new File(tempDir.toFile, "test.gz")
    
//     // Create a dummy GZ file
//     val gzOut = new GZIPOutputStream(new FileOutputStream(gzFile))
//     gzOut.write("dummy content".getBytes())
//     gzOut.close()

//     CompressionHandler.decompressGz(gzFile.getAbsolutePath, tempDir.toString)

//     // Check if file is decompressed
//     assert(new File(tempDir.toFile, "test").exists())

//     // Cleanup
//     gzFile.delete()
//     tempDir.toFile.delete()
//   }

//   test("decompressBz2 should decompress bz2 files correctly") {
//     val tempDir = Files.createTempDirectory("decompressBz2Test")
//     val bz2File = new File(tempDir.toFile, "test.bz2")
    
//     // Create a dummy BZ2 file
//     val bz2Out = new BZip2CompressorOutputStream(new FileOutputStream(bz2File))
//     bz2Out.write("dummy content".getBytes())
//     bz2Out.close()

//     CompressionHandler.decompressBz2(bz2File.getAbsolutePath, tempDir.toString)

//     // Check if file is decompressed
//     assert(new File(tempDir.toFile, "test").exists())

//     // Cleanup
//     bz2File.delete()
//     tempDir.toFile.delete()
//   }

// }
