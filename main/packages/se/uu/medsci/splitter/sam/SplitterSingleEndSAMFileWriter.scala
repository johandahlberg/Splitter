package se.uu.medsci.splitter.sam

import net.sf.samtools._
import org.slf4j.LoggerFactory
import se.uu.medsci.splitter.SplitterWriter
import se.uu.medsci.splitter.Bufferable

class SplitterSingleEndSAMFileWriter(samFileWriter: SAMFileWriter) extends SplitterSAMFileWriter {
    
    private val logger = LoggerFactory.getLogger(classOf[SplitterSingleEndSAMFileWriter]);      

    def write(rec: SAMRecord) = {
            samFileWriter.addAlignment(rec)
    }
    
    def close = {
        samFileWriter.close()
    }

}