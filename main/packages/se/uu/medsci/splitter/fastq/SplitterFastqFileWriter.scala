package se.uu.medsci.splitter.fastq

import net.sf.picard.fastq._
import se.uu.medsci.splitter.SplitterWriter
import java.io._
import java.util.zip.GZIPOutputStream

class SplitterFastqFileWriter(outputFile: File) extends SplitterWriter[FastqRecord] {

    val output = new FileOutputStream(outputFile)
    val writer: Writer = new OutputStreamWriter(new GZIPOutputStream(output), "UTF-8")

    def write(record: FastqRecord) = {

        //TODO Make sure that the outputs are appended with new lines.
        writer.write(FastqConstants.SEQUENCE_HEADER);
        printlnHelper(record.getReadHeader());
        printlnHelper(record.getReadString());
        writer.write(FastqConstants.QUALITY_HEADER);
        printlnHelper(if (record.getBaseQualityHeader() == null) "" else record.getBaseQualityHeader());
        printlnHelper(record.getBaseQualityString());
    }

    private def printlnHelper(s: String) = {
        writer.write(s + "\n");
    }
    
    def close() = {
        writer.close()
        output.close()
    }
}

