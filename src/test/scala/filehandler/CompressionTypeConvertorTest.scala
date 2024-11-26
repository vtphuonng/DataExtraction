
// import java.nio.file.{Files, Paths}
// import org.scalatest.flatspec.AnyFlatSpec
// import org.scalatest.matchers.should.Matchers
// import org.apache.commons.io.FileUtils
// import scala.util.Try
// import java.util.zip.{ZipEntry, ZipOutputStream}
// import java.io._

// import compressiontypeconvertor.CompressionTypeConverter

// class FileConverterTest extends AnyFlatSpec with Matchers {

//   "convertFilesToGzip" should "decompress and recompress files to GZIP format" in {
//     // Setup: Create temporary directories for testing
//     val tempDir = Files.createTempDirectory("fileConverterTest")
//     val intermediateDir = tempDir.resolve("intermediate").toString
//     val outputDir = tempDir.resolve("output").toString
//     Files.createDirectories(Paths.get(intermediateDir))
//     Files.createDirectories(Paths.get(outputDir))

//     // Create a temporary source file to simulate decompression
//     val sourceTextFile = new java.io.File(tempDir.toFile, "source.txt")
//     val writer = new java.io.BufferedWriter(new java.io.FileWriter(sourceTextFile))
//     writer.write("This is a test file.")
//     writer.close()

//     val sourceZipFile = new java.io.File(tempDir.toFile, "source.zip")
//     val fos = new FileOutputStream(sourceZipFile)
//     val zos = new ZipOutputStream(fos)
//     zos.putNextEntry(new ZipEntry(sourceTextFile.getName))
//     val fis = new FileInputStream(sourceTextFile) 
//     val buffer = new Array[Byte](1024)
//     var length = fis.read(buffer)
//     while (length > 0) {
//         zos.write(buffer, 0, length)
//         length = fis.read(buffer)
//     }
//     zos.closeEntry()
//     fis.close()
//     zos.close()
//     fos.close()   

//     // List of files to convert
//     val filesToConvert = List(sourceZipFile.getAbsolutePath)

//     // Call the conversion method
//     Try {
//         CompressionTypeConverter.convertToGzip(filesToConvert, intermediateDir, outputDir)
//     }.recover {
//         case e: Exception =>
//             println(s"Error during conversion: ${e.getMessage}")
//             fail(s"Conversion failed: ${e.getMessage}")
//     }
    
//     // Verify: Check if the output GZIP file exists
//     val outputGzipFile = new java.io.File(outputDir, "source.txt.gz")
//     println(s"FLAG2: $outputGzipFile")
//     outputGzipFile.exists() shouldBe true

//     Try {
//         val fis = new java.io.FileInputStream(outputGzipFile)
//         val gzis = new java.util.zip.GZIPInputStream(fis)
//         val buffer = new Array[Byte](1024)
//         val len = gzis.read(buffer)
//         println(s"FLAG2: Read ${len} bytes from the GZIP file")
//         gzis.close()
//     }.recover{
//         case e: Exception =>
//             println(s"Error reading the GZIP file: ${e.getMessage}")
//             fail(s"Reading GZIP file failed: ${e.getMessage}")
//     }

//     // Cleanup
//     FileUtils.deleteDirectory(tempDir.toFile)
//   }

//   it should "handle invalid file paths gracefully" in {
//     val filesToConvert = List("/invalid/file/path.zip")
//     val intermediateDir = "/tmp/intermediate"
//     val outputDir = "/tmp/output"

//     // Create directories
//     Files.createDirectories(Paths.get(intermediateDir))
//     Files.createDirectories(Paths.get(outputDir))

//     // Call the conversion method
//     CompressionTypeConverter.convertToGzip(filesToConvert, intermediateDir, outputDir)

//     // No exception should be thrown and no files should be created
//     val outputFiles = new java.io.File(outputDir).listFiles()
//     outputFiles shouldBe empty
//   }
// }
