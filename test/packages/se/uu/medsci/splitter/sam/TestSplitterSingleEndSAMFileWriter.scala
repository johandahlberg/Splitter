package se.uu.medsci.splitter.sam

import java.io.File

import collection.JavaConversions.asScalaIterator

import org.scalatest.FlatSpec

import net.sf.samtools.SAMFileHeader.SortOrder
import net.sf.samtools.SAMFileWriterFactory
import net.sf.samtools.SAMRecordSetBuilder

/**
 * TODO
 * 
 * Add some tests actually looking at the output file, making sure that the correct reads were written.
 */

class TestSplitterSingleEndSAMFileWriter extends FlatSpec {

    val file = File.createTempFile("testingsam", ".bam")
    val samRecordFactory = new SAMRecordSetBuilder(true, SortOrder.coordinate)

    val header = samRecordFactory.getHeader()
    val samFileWriterFactory = new SAMFileWriterFactory
    val samFileWriter = samFileWriterFactory.makeBAMWriter(header, true, file)
    val splitterWriter = new SplitterSingleEndSAMFileWriter(samFileWriter, file)

    "A SplitterSingleEndSAMFileWriter" should "write to a file." in {

        samRecordFactory.addFrag("read1", 1, 1, true)
        samRecordFactory.addFrag("read2", 1, 2, true)
        val records = samRecordFactory.getRecords()
        val iterator = records.iterator()

        // The actual test
        for (rec <- iterator) {
            splitterWriter.write(rec)
        }
    }

    it should "close the file when finished." in {

        // The actual test
        splitterWriter.close()

    }
}