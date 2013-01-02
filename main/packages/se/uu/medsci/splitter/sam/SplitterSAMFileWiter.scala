package se.uu.medsci.splitter.sam

import se.uu.medsci.splitter.SplitterWriter
import net.sf.samtools._
import java.io.File
import net.sf.picard.sam.FixMateInformation
import scala.collection.JavaConversions
import net.sf.picard.sam.SplitterFixMatePairInformation
import net.sf.picard.sam.SplitterFixMatePairInformation

abstract class SplitterSAMFileWriter(samFileWriter: SAMFileWriter, file: File) extends SplitterWriter[SAMRecord] {

    /**
     * Common close method for both single end and paried writer classes.
     */
    def close = {
        samFileWriter.close()
        fixMatePairInfo()
    }

    /**
     * FixMateInformation of the SAM file that the writer has been working on.
     *
     * This methods used the FixMateInformation class from net.sf.picard.sam.FixMateInformation to fix the mate info
     */
    protected def fixMatePairInfo() {
        val matePairFixer = new SplitterFixMatePairInformation(file)
        matePairFixer.run()
    }
}
