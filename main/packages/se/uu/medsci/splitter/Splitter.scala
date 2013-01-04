package se.uu.medsci.splitter

import collection.JavaConversions._
import org.slf4j.LoggerFactory
import java.io._
import net.sf.samtools._
import net.sf.picard.fastq.FastqReader
import net.sf.picard.fastq.FastqRecord
import se.uu.medsci.splitter.sam.SplitterSAMFileWriterFactory
import se.uu.medsci.splitter.fastq.SplitterFastqFileWriterFactory
//import scopt._
import scopt.mutable.OptionParser


object Splitter extends App {

    /**
     * Variables
     */

    lazy private val logger = LoggerFactory.getLogger(classOf[App]);

    private val defaultNbrOfRecordsPerFile: Int = 100000;

    class Config {
        var inputFile: File = _
        var outputDir: File = _
        var listOfOutputFile: File = _
        var recordsPerOutputFile: Int = -1
        var isPairedEnd: Boolean = true
    }

    lazy val config: Config = new Config
    
    lazy val parser = new scopt.mutable.OptionParser("Splitter") {
        opt("i", "input", "<file>", "Path to input file to split", { v: String => config.inputFile = new File(v) })
        opt("o", "output_dir", "<dir>", "Path to dir for output files.", { v: String => config.outputDir = new File(v) })
        opt("l", "list", "<file>", "List of output files.", { v: String => config.listOfOutputFile = new File(v) })
        intOpt("r", "records_per_file", "Number of records to output per file. 100000 is default.", { v: Int => config.recordsPerOutputFile = v })
        booleanOpt("pe", "If splitting a bam file use to indicate that the data is paired. If you are splitting a file with single end data, set to false. Default is true.", { v: Boolean => config.isPairedEnd = v })
    }

    /**
     * Running the App
     */
    run(args)
    def run(args: Array[String]): List[File] = {
        if ((!args.isEmpty) && parser.parse(args) && parseArguments(config)) {
            logger.info("Starting the file splitter.")
            val outputFiles = runFileSplitter(config.inputFile, config.outputDir, config.recordsPerOutputFile, config.listOfOutputFile)
            outputFiles
        } else {
            // arguments are bad, usage message will be displayed
            logger.info(parser.usage)
            List()
        }
    }

    /**
     * Private help functions for parsing arguments
     */

    private def runFileSplitter(inputFile: File, outputDir: File, recordsPerFile: Int, outputFilesList: File): List[File] = {

        val fileSplitter = SimpleFileSplitter.newFileSplitter(inputFile, outputDir.getAbsolutePath() + "/" + config.inputFile.getName, config.isPairedEnd)
        val outputFiles = fileSplitter.split(recordsPerFile);

        writeOutputList(outputFilesList, outputFiles)
        outputFiles
    }

    private def parseArguments(config: Config): Boolean = {
        if (config.inputFile == null) {
            return false
        } else if (config.recordsPerOutputFile < 1) {
            logger.info("Number of records per file was not specified. Setting it to default of: " + defaultNbrOfRecordsPerFile)
            config.recordsPerOutputFile = defaultNbrOfRecordsPerFile
        } else if (config.outputDir == null || !config.outputDir.isDirectory() || !config.outputDir.exists()) {
            logger.info("output_dir is not a directory.")
            return false
        } else if (config.listOfOutputFile == null) {
            logger.info("No output file for the list of output files was specified. Setting to default: " + config.inputFile.getName() + ".splitlist")
            config.listOfOutputFile = new File(config.outputDir + "/" + config.inputFile.getName() + ".splitlist")
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