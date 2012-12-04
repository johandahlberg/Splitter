package se.uu.medsci.splitter

import collection.JavaConversions._
import org.slf4j.LoggerFactory
import java.io._
import net.sf.samtools._
import net.sf.picard.fastq.FastqReader
import net.sf.picard.fastq.FastqRecord
import se.uu.medsci.splitter.sam.SplitterSAMFileWriterFactory
import se.uu.medsci.splitter.fastq.SplitterFastqFileWriterFactory
import scopt.mutable

object Splitter extends App {

  private val logger = LoggerFactory.getLogger(classOf[App]);

  private val defaultNbrOfRecordsPerFile: Int = 100000;

  class Config {
    var inputFile: File = _
    var outputDir: File = _
    var listOfOutputFile: File = _
    var recordsPerOutputFile: Int = -1
  }

  val config: Config = new Config

  val parser = new scopt.mutable.OptionParser("Splitter") {
    opt("i", "input", "<file>", "Path to input file to split", { v: String => config.inputFile = new File(v) })
    opt("d", "output_dir", "<dir>", "Path to dir for output files.", { v: String => config.outputDir = new File(v) })
    opt("l", "list", "<file>", "List of output files.", { v: String => config.listOfOutputFile = new File(v) })
    intOpt("r", "records_per_file", "Number of records to output per file. 100000 is default.", { v: Int => config.recordsPerOutputFile = v })
  }

  // Parse arguments and start operations.
  if ((!args.isEmpty) && parser.parse(args) && parseArguments(config)) {
    logger.info("Starting the file splitter.")
    runFileSplitter(config.inputFile, config.outputDir, config.recordsPerOutputFile, config.listOfOutputFile)
  } else {
    // arguments are bad, usage message will have be displayed
    logger.info(parser.usage)
  }

  private def runFileSplitter(inputFile: File, outputDir: File, recordsPerFile: Int, outputFilesList: File) = {

    val fastqFileSplitter = SimpleFileSplitter.newFileSplitter(inputFile, outputDir.getAbsolutePath() + "/" + config.inputFile.getName)
    val fastqOutputFiles = fastqFileSplitter.split(recordsPerFile);    
    
    writeOutputList(outputFilesList, fastqOutputFiles)

  }

  private def parseArguments(config: Config): Boolean = {       
    if (config.inputFile == null) {
      return false
    } else if (config.recordsPerOutputFile < 1) {
      logger.info("Number of records per file was not specified. Setting it to default of: " + defaultNbrOfRecordsPerFile)
      config.recordsPerOutputFile = defaultNbrOfRecordsPerFile
    } 
    else if (config.listOfOutputFile == null) {
      logger.info("No output file for the list of output files was specified. Setting to default: " + config.inputFile.getName() + ".splitlist")
      config.listOfOutputFile = new File(config.outputDir + "/" + config.inputFile.getName() + ".splitlist")
    }
    else if (config.outputDir == null || !config.outputDir.isDirectory()) {
      logger.info("output_dir is not a directory.")
      return false
    }
    return true
  }
  
  private def writeOutputList(outputFilesList: File, list: List[File]): Unit = {   
    
    val writer = new PrintWriter(outputFilesList)
    try {
      for (inputFile <- list)
        addFile(writer, inputFile)
    } finally {
      writer.close()
    }  
  
    def addFile(writer: PrintWriter, inputFile: File) {
        writer.println(inputFile.toString)
    }  
  }
}