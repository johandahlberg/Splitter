package se.uu.medsci.splitter.sam

import net.sf.samtools._
import org.slf4j.LoggerFactory
import se.uu.medsci.splitter.SplitterWriter
import se.uu.medsci.splitter.Bufferable
import java.io.File

class SplitterSingleEndSAMFileWriter(samFileWriter: SAMFileWriter, file: File) extends SplitterSAMFileWriter(samFileWriter, file) {
    
    private val logger = LoggerFactory.getLogger(classOf[SplitterSingleEndSAMFileWriter]);      

    def write(rec: SAMRecord) = {
            samFileWriter.addAlignment(rec)
    }    

}