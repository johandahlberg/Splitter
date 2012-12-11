package se.uu.medsci.splitter.fastq

import net.sf.samtools._
import net.sf.picard.fastq.FastqRecord
import net.sf.picard.fastq.FastqWriterFactory
import java.io.File
import se.uu.medsci.splitter.SplitterWriter
import se.uu.medsci.splitter.SplitterFileWriterFactory

class SplitterFastqFileWriterFactory extends SplitterFileWriterFactory[FastqRecord] {
      
    def createNewWriter(file: File): SplitterWriter[FastqRecord] = newFASTQFileWriter(file)
    
    def createNewWriter(file: File, buffer: Iterable[FastqRecord]): SplitterWriter[FastqRecord] = throw new Exception("No constructor with buffer has been implemented in SplitterFastqFileWriterFactory")
    
    private def newFASTQFileWriter(file: File): SplitterWriter[FastqRecord] = 
                    new SplitterFastqFileWriter(file)

}