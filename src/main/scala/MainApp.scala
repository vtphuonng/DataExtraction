package mainapp

import scala.io.StdIn.readLine
import compressiontypeconvertor.CompressionTypeConverter

object MainApp {
  def main(args: Array[String]): Unit = {
    println("Welcome to the File Processing Application!")
    println("Please choose an option:")
    println("1. Convert files to GZIP")
    println("2. Option 2 (Placeholder)")
    println("3. Option 3 (Placeholder)")

    val choice = readLine("Enter the number of your choice: ")

    choice match {
      case "1" =>
        println("You chose to convert files to GZIP.")
        handleGzipConversion()
      case "2" =>
        println("Option 2 selected. (Feature not implemented yet)")
      case "3" =>
        println("Option 3 selected. (Feature not implemented yet)")
      case _ =>
        println("Invalid choice. Please run the program again and select a valid option.")
    }
  }

  def handleGzipConversion(): Unit = {
    val filePaths = readLine("Enter the file paths to convert, separated by commas: ").split(",").map(_.trim).toList
    val intermediateDir = readLine("Enter the path for the intermediate directory: ").trim
    val outputDir = readLine("Enter the path for the output directory: ").trim

    CompressionTypeConverter.convertToGzip(filePaths, intermediateDir, outputDir)
    println("Conversion process completed.")
  }
}
