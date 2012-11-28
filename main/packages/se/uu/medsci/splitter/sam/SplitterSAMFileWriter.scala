package se.uu.medsci.splitter.sam

import net.sf.samtools._
import org.slf4j.LoggerFactory
import se.uu.medsci.splitter.Writer

class SplitterSAMFileWriter(samFileWriter: SAMFileWriter) extends Writer[SAMRecord] {

    private val logger = LoggerFactory.getLogger(classOf[SplitterSAMFileWriter]);

    def write(rec: SAMRecord) = {
        //logger.info("Writing alignment");
        samFileWriter.addAlignment(rec)
    }
    
    def close = samFileWriter.close()    

}