package se.uu.medsci.splitter.sam

import net.sf.samtools._
import java.io.File
import se.uu.medsci.splitter.SplitterFileWriterFactory
import se.uu.medsci.splitter.SplitterWriter

class SplitterSAMFileWriterFactory(header: SAMFileHeader, presorted: Boolean, isPariedEnd: Boolean) extends SAMFileWriterFactory with SplitterFileWriterFactory[SAMRecord] {

    def createNewWriter(file: File): SplitterWriter[SAMRecord] = newSAMFileWriter(file)

    def createNewWriter(file: File, buffer: Iterable[SAMRecord]): SplitterWriter[SAMRecord] = newSAMFileWriter(file, buffer)

    /**
     * Create a SAMFileWriter
     */
    private def newSAMFileWriter(file: File): SplitterSAMFileWriter = {
        if (isPariedEnd)
            new SplitterPairEndSAMFileWriter(super.makeBAMWriter(header, presorted, file), file)
        else
            new SplitterSingleEndSAMFileWriter(super.makeBAMWriter(header, presorted, file), file)
    }

    /**
     * Create a SAMFileWriter with a buffer. Used for handling paired reads.
     */
    private def newSAMFileWriter(file: File, buffer: Iterable[SAMRecord]): SplitterSAMFileWriter = {
        if (isPariedEnd)
            new SplitterPairEndSAMFileWriter(super.makeBAMWriter(header, presorted, file), file, buffer)
        else
            throw new Exception("Single end SAMFileWriter is not implemented for constructor with buffer.")
    }

}