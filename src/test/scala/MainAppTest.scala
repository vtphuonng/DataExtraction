// package mainapp

// import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
// import org.scalatest.flatspec.AnyFlatSpec
// import org.scalatest.matchers.should.Matchers
// import compressiontypeconvertor.CompressionTypeConverter
// import org.scalatest.BeforeAndAfter

// class MainAppTest extends AnyFlatSpec with Matchers with BeforeAndAfter {

//   private var originalIn: java.io.InputStream = _
//   private var originalOut: java.io.PrintStream = _

//   before {
//     originalIn = System.in
//     originalOut = System.out
//   }

//   after {
//     System.setIn(originalIn)
//     System.setOut(originalOut)
//   }

//   "MainApp" should "handle GZIP conversion option correctly" in {
//     // Simulate user input
//     val input = "1\n/tmp/testfile.txt,/tmp/testfile2.txt\n/tmp/intermediate\n/tmp/output\n"
//     System.setIn(new ByteArrayInputStream(input.getBytes))

//     // Capture console output
//     val outputStream = new ByteArrayOutputStream()
//     System.setOut(new PrintStream(outputStream))

//     // Intercept the call to CompressionTypeConverter.convertToGzip
//     val realConvertToGzip = CompressionTypeConverter.convertToGzip _
//     var mockCalled = false

//     CompressionTypeConverter.convertToGzip = (filePaths: List[String], intermediateDir: String, outputDir: String) => {
//         mockCalled = true
//         println("Mock conversion to GZIP called")
//     }
//     // Run the main method
//     MainApp.main(Array.empty)

//     // Verify output
//     val output = outputStream.toString
//     output should include("Welcome to the File Processing Application!")
//     output should include("You chose to convert files to GZIP.")
//     output should include("Conversion process completed.")
//     output should include("Mock conversion to GZIP called")

//     // Reset CompressionTypeConverter to the original
//     CompressionTypeConverter = tempConverter
//   }

//   it should "handle invalid option gracefully" in {
//     // Simulate user input
//     val input = "4\n"
//     System.setIn(new ByteArrayInputStream(input.getBytes))

//     // Capture console output
//     val outputStream = new ByteArrayOutputStream()
//     System.setOut(new PrintStream(outputStream))

//     // Run the main method
//     MainApp.main(Array.empty)

//     // Verify output
//     val output = outputStream.toString
//     output should include("Invalid choice. Please run the program again and select a valid option.")
//   }
// }
