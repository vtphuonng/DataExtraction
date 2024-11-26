// package filecompressor

// import java.io._
// import java.nio.file._
// import java.util.zip._
// import org.scalatest._
// import org.scalatest.flatspec.AnyFlatSpec
// import org.scalatest.matchers.should.Matchers

// class CompressionHandlerTest extends AnyFlatSpec with Matchers {

//   // Test case for successful compression
//   "gzipCompress" should "compress a file into a .gz file" in {
//     // Setup: Create a temporary file with some content
//     val tempDir = Files.createTempDirectory("gzipCompressTest")
//     val sourceFile = new File(tempDir.toFile, "source.txt")
//     val writer = new BufferedWriter(new FileWriter(sourceFile))
//     writer.write("This is a test file to be compressed.")
//     writer.close()

//     // Output path for the compressed file
//     val outputFilePath = new File(tempDir.toFile, "source.txt.gz").getAbsolutePath

//     // Call the method
//     CompressionHandler.gzipCompress(sourceFile.getAbsolutePath, outputFilePath)

//     // Verify: Check if the .gz file exists
//     val outputFile = new File(outputFilePath)
//     outputFile.exists() shouldBe true

//     // Verify: Check that the file is actually a GZIP file by inspecting its header
//     val fis = new FileInputStream(outputFile)
//     val magicBytes = new Array[Byte](2)
//     fis.read(magicBytes)
//     fis.close()

//     magicBytes shouldBe Array[Byte](31, -117) // GZIP magic header

//     // Cleanup
//     sourceFile.delete()
//     outputFile.delete()
//     tempDir.toFile.delete()
//   }

//   // Test case for invalid source file (not a valid file)
//   it should "throw IllegalArgumentException when the source path is not a valid file" in {
//     val invalidPath = "/invalid/file.txt"
//     val outputFilePath = "/output/file.gz"

//     // Expecting an exception when calling gzipCompress
//     an[IllegalArgumentException] should be thrownBy {
//       CompressionHandler.gzipCompress(invalidPath, outputFilePath)
//     }
//   }

//   // Test case for invalid directory as source
//   it should "throw IllegalArgumentException when the source path is a directory" in {
//     val sourceDir = Files.createTempDirectory("gzipCompressTestDir").toFile
//     val outputFilePath = "/output/file.gz"

//     // Expecting an exception when calling gzipCompress with a directory
//     an[IllegalArgumentException] should be thrownBy {
//       CompressionHandler.gzipCompress(sourceDir.getAbsolutePath, outputFilePath)
//     }

//     // Cleanup
//     sourceDir.delete()
//   }
// }
