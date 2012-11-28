package se.uu.medsci.splitter.fastq

import net.sf.samtools._
import net.sf.picard.fastq.FastqRecord
import net.sf.picard.fastq.FastqWriterFactory
import java.io.File
import se.uu.medsci.splitter.Writer
import se.uu.medsci.splitter.SplitterFileWriterFactory

class SplitterFastqFileWriterFactory extends FastqWriterFactory with SplitterFileWriterFactory[FastqRecord] {
      
    def createNewWriter(file: File): Writer[FastqRecord] = newFASTQFileWriter(file)
    
    private def newFASTQFileWriter(file: File): Writer[FastqRecord] = 
                    new SplitterFastqFileWriter(super.newWriter(file))

}