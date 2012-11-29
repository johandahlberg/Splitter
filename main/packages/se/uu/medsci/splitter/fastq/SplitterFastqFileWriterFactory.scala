package se.uu.medsci.splitter.fastq

import net.sf.samtools._
import net.sf.picard.fastq.FastqRecord
import net.sf.picard.fastq.FastqWriterFactory
import java.io.File
import se.uu.medsci.splitter.SplitterWriter
import se.uu.medsci.splitter.SplitterFileWriterFactory

class SplitterFastqFileWriterFactory extends SplitterFileWriterFactory[FastqRecord] {
      
    def createNewWriter(file: File): SplitterWriter[FastqRecord] = newFASTQFileWriter(file)
    
    private def newFASTQFileWriter(file: File): SplitterWriter[FastqRecord] = 
                    new SplitterFastqFileWriter(file)

}