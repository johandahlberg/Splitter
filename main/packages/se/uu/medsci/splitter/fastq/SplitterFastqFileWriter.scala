package se.uu.medsci.splitter.fastq

import net.sf.picard.fastq._
import se.uu.medsci.splitter.Writer
import java.io._

class SplitterFastqFileWriter(fastqWriter: FastqWriter) extends FastqWriter with Writer[FastqRecord] { 
  
    def write(record: FastqRecord) = fastqWriter.write(record)

    def close() = fastqWriter.close()
}

