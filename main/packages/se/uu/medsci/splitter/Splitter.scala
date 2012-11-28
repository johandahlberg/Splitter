package se.uu.medsci.splitter

import collection.JavaConversions._
import org.slf4j.LoggerFactory
import java.io._
import net.sf.samtools._
import net.sf.picard.fastq.FastqReader
import net.sf.picard.fastq.FastqRecord
import se.uu.medsci.splitter.sam.SplitterSAMFileWriterFactory
import se.uu.medsci.splitter.fastq.SplitterFastqFileWriterFactory

object Splitter extends App {

  private val logger = LoggerFactory.getLogger(classOf[App]);

  logger.info("Hello world splitter!");

  val inputBamFile = new File("resources/exampleBAM.bam");
  val inputFastQFile = new File("resources/exampleFastq.fastq.gz");

  logger.info("Splitting fastq")
  val fastqFileSplitter = SimpleFileSplitter.newFileSplitter(inputFastQFile, "fastq")
  val fastqOutputFiles = fastqFileSplitter.split(10);
  
  logger.info("Splitting bam")
  val bamFileSplitter = SimpleFileSplitter.newFileSplitter(inputBamFile, "bam")
  val bamOutputFiles = bamFileSplitter.split(10);
  logger.info("The output files were: " + bamOutputFiles)

}