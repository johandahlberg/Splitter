package se.uu.medsci.splitter.sam

import net.sf.samtools._
import java.io.File
import se.uu.medsci.splitter.SplitterFileWriterFactory
import se.uu.medsci.splitter.SplitterWriter

class SplitterSAMFileWriterFactory(header: SAMFileHeader, presorted: Boolean) extends SAMFileWriterFactory with SplitterFileWriterFactory[SAMRecord] {

    def createNewWriter(file: File): SplitterWriter[SAMRecord] = newSAMFileWriter(file)
    
    private def newSAMFileWriter(file: File): SplitterSAMFileWriter = 
                    new SplitterSAMFileWriter(super.makeBAMWriter(header, presorted, file))
        
}