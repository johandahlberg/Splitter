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
class SimpleFileSplitter[T](inputFileIteratorHandle: Iterable[T], writerFactor: SplitterFileWriterFactory[T], outputFileNamePrefix: String, outputFileNamePostFix: String) extends FileSplitter {

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
        val outputBaseName: String = outputFileNamePrefix.replace(outputFileNamePostFix, "")
        // Function to build a file name with the correct index appended.
        def getFilenameWithIndex = new File(outputBaseName + "." + index + outputFileNamePostFix);
        var outputFile: File = getFilenameWithIndex
        var listOfOutputFiles: List[File] = List(outputFile);

        // Initializing a file writer
        var fileWriter: SplitterWriter[T] = writerFactor.createNewWriter(outputFile);

        // Iterator over all records in the input file and write to the output file
        // while the file size is under the desired size.
        val iterator = inputFileIteratorHandle.iterator;
        for (record <- iterator) {
            try {
                recordsProcessed = recordsProcessed + 1;

                // Write to the output file using abstract record reader.
                //val record: T = iterator.next();
                fileWriter.write(record);

                // If the desired number of records have been passed to the file writer,
                // create a new output file and file writer
                if (recordsProcessed == numberOfRecordsPerFile * index && iterator.hasNext) {

                    index = index + 1;
                    outputFile = getFilenameWithIndex

                    // If the write has a buffer, make sure this is passed along to the next writer.
                    fileWriter =
                        if (fileWriter.isInstanceOf[Bufferable[T]])
                            checkBufferAndCloseFileWriter(fileWriter.asInstanceOf[SplitterWriter[T] with Bufferable[T]], outputFile)
                        else {
                            fileWriter.close();
                            writerFactor.createNewWriter(outputFile);
                        }

                    listOfOutputFiles = outputFile :: listOfOutputFiles;
                }
            }
        }

        // If the file writer has a buffer - make sure that it is flushed before exiting.
        if (fileWriter.isInstanceOf[Bufferable[T]]) {
            fileWriter.asInstanceOf[Bufferable[T]].writeBuffer()
        }

        fileWriter.close()

        logger.debug("Finished processing. Processed a total of " + recordsProcessed + " records processed. And created " + index + " new files.");

        // Return the list of output files.
        listOfOutputFiles
    }

    private def checkBufferAndCloseFileWriter(fileWriter: SplitterWriter[T] with Bufferable[T], outputFile: File): SplitterWriter[T] = {

        // Check if the fileWriter is bufferable
        // If so pass the buffer to the next writer
        // and return a appropriate file writer.
        if (fileWriter.bufferIsEmpty) {
            fileWriter.close();
            return writerFactor.createNewWriter(outputFile);
        } else {
            val buffer = fileWriter.getBuffer
            fileWriter.close();
            return writerFactor.createNewWriter(outputFile, buffer);
        }
    }
}

object SimpleFileSplitter {
    /**
     * Factory method trying to determine the file type of the input file dynamically and returning a appropriate FileSplitter
     */
    def newFileSplitter(inputFile: File, outputFilesPrefix: String, isPairedEnd: Boolean): FileSplitter =
        {

            val nameOfTheFile = inputFile.getName()

            val splitter =
                if (nameOfTheFile.matches(""".*\.fastq\.gz""")) {
                    val iterableFastqFileReader: Iterable[FastqRecord] = new FastqReader(inputFile)
                    val fastqWriterFastqFactory: SplitterFileWriterFactory[FastqRecord] = new SplitterFastqFileWriterFactory();
                    new SimpleFileSplitter[FastqRecord](iterableFastqFileReader, fastqWriterFastqFactory, outputFilesPrefix, ".fastq.gz")
                } else if ((nameOfTheFile.matches(""".*\.bam"""))) {
                    val iterableFileReader: SAMFileReader = new SAMFileReader(inputFile);
                    val header: SAMFileHeader = iterableFileReader.getFileHeader();
                    val writerFactory: SplitterFileWriterFactory[SAMRecord] = new SplitterSAMFileWriterFactory(header, false, isPairedEnd);
                    new SimpleFileSplitter[SAMRecord](iterableFileReader, writerFactory, outputFilesPrefix, ".bam");
                } else
                    throw new SplitterException("Did not recognize file type of input file: " + inputFile)

            splitter
        }
}