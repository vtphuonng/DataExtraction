package compressiontypeconvertor

import java.nio.file.{Files, Paths, Path}
import scala.util.Try

import filedecompressor.{CompressionHandler => DecompressionHandler}
import filecompressor.{CompressionHandler => CompressorHandler}

object CompressionTypeConverter {
  def convertToGzip(filePaths: List[String], intermediateDir: String, outputDir: String): Unit = {
    Files.createDirectories(Paths.get(intermediateDir))    
    Files.createDirectories(Paths.get(outputDir))
    
    filePaths.foreach( filePath => 
      Try {
        println(s"Processing: $filePath")
        val decompressedDirPath = Paths.get(intermediateDir, Paths.get(filePath)
                                    .getFileName.toString()
                                    .replaceAll("\\.[^.]+$", "")
                                    )

        println(s"FLAG3: $filePath && ${decompressedDirPath.toString()}")
        val decompressedDir = decompressedDirPath.toFile.getAbsolutePath
        
        DecompressionHandler.decompress(filePath, decompressedDir.toString()) 

        val decompressedFile = decompressedDirPath.toFile
        if (decompressedFile.isDirectory) {
          decompressedFile.listFiles().foreach { file =>
            val outputFilePath = Paths.get(outputDir, file.getName + ".gz").toString
            CompressorHandler.gzipCompress(file.getAbsolutePath, outputFilePath)
            println(s"Successfully converted to GZIP: $outputFilePath")
          }
        } else {
          val outputFilePath = Paths.get(outputDir, decompressedFile.getName + ".gz").toString
          CompressorHandler.gzipCompress(decompressedFile.getAbsolutePath, outputFilePath)
          println(s"Successfully converted to GZIP: $outputFilePath")
        }
      }.recover {
        case e: Exception => 
          println(s"Failed to process $filePath: ${e.getMessage}")
      }
    )
  }
}