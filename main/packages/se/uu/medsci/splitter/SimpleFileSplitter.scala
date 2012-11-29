package se.uu.medsci.splitter

import java.io.File
import org.slf4j.LoggerFactory

import se.uu.medsci.splitter.fastq._
import se.uu.medsci.splitter.sam._
import net.sf.samtools._
import net.sf.picard.fastq._

import collection.JavaConversions._

/**
 * Generalized file splitter
 * T is a record class, for example a SAMRecord which can be written to a file by
 * a writer of class K.
 */
class SimpleFileSplitter[T](inputFileIteratorHandle: Iterable[T], writerFactor: SplitterFileWriterFactory[T], outputFileNamePrefix: String) extends FileSplitter {

  val logger = LoggerFactory.getLogger(classOf[SimpleFileSplitter[T]]);

  /**
   * Split the the file for the input interator to files of size of desiredFileSize,
   * outputting new files with file names as specified by outputFileNamePrefix
   */
  def split(numberOfRecordsPerFile: Long): List[File] = {
    logger.debug("Splitting");
    // Counters    
    var index: Int = 1;
    var recordsProcessed = 0;

    // Files and file lists
    var outputFile: File = new File(outputFileNamePrefix + "." + index);
    var listOfOutputFiles: List[File] = List(outputFile);

    // Initializing a file writer
    var fileWriter: SplitterWriter[T] = writerFactor.createNewWriter(outputFile);

    // Iterator over all records in the input file and write to the output file
    // while the file size is under the desired size.
    val iterator = inputFileIteratorHandle.iterator;
    while (iterator.hasNext) {
      try {
        recordsProcessed = recordsProcessed + 1;

        // Write to the output file using abstract record reader.
        val record: T = iterator.next();
        fileWriter.write(record);

        if (recordsProcessed == numberOfRecordsPerFile * index) {
          fileWriter.close();
          index = index + 1;
          outputFile = new File(outputFileNamePrefix + "." + index);
          fileWriter = writerFactor.createNewWriter(outputFile);
          listOfOutputFiles = outputFile :: listOfOutputFiles;
        }
      }
    }

    fileWriter.close();

    logger.debug("Finished processing. Processed a total of " + recordsProcessed + " records processed. And created " + index + " new files.");

    // Return the list of output files.
    listOfOutputFiles
  }
}

object SimpleFileSplitter {
  /**
   * Factory method trying to determine the file type of the input file dynamically and returning a appropriate FileSplitter
   */
  def newFileSplitter(inputFile: File, outputFilesPrefix: String): FileSplitter =
    {

      println(outputFilesPrefix)

      val nameOfTheFile = inputFile.getName()

      val splitter =
        if (nameOfTheFile.matches(""".*\.fastq\.gz""")) {
          val iterableFastqFileReader: Iterable[FastqRecord] = new FastqReader(inputFile)
          val fastqWriterFastqFactory: SplitterFileWriterFactory[FastqRecord] = new SplitterFastqFileWriterFactory();
          new SimpleFileSplitter[FastqRecord](iterableFastqFileReader, fastqWriterFastqFactory, outputFilesPrefix)
        } else if ((nameOfTheFile.matches(""".*\.bam"""))) {
          val iterableFileReader: SAMFileReader = new SAMFileReader(inputFile);
          val header: SAMFileHeader = iterableFileReader.getFileHeader();
          val writerFactory: SplitterFileWriterFactory[SAMRecord] = new SplitterSAMFileWriterFactory(header, false);
          new SimpleFileSplitter[SAMRecord](iterableFileReader, writerFactory, outputFilesPrefix);
        } else
          throw new Exception("Did not recognize file type of input file: " + inputFile)

      splitter
    }
}