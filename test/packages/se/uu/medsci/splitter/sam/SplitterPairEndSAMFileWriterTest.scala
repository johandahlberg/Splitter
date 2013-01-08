package se.uu.medsci.splitter.sam

import java.io.File

import scala.collection.JavaConversions.collectionAsScalaIterable

import org.scalatest.BeforeAndAfter
import org.scalatest.Finders
import org.scalatest.FlatSpec

import net.sf.samtools.SAMFileHeader.SortOrder
import net.sf.samtools.SAMFileWriterFactory
import net.sf.samtools.SAMRecord
import net.sf.samtools.SAMRecordSetBuilder

class SplitterPairEndSAMFileWriterTest extends FlatSpec with BeforeAndAfter {

    val file = File.createTempFile("testingsam", ".bam")
    var samRecordFactory = new SAMRecordSetBuilder(true, SortOrder.coordinate)

    val header = samRecordFactory.getHeader()
    val samFileWriterFactory = new SAMFileWriterFactory
    val samFileWriter = samFileWriterFactory.makeBAMWriter(header, false, file)

    var splitterWriter = new SplitterPairEndSAMFileWriter(samFileWriter, file, List())  
    var splitterWriterWithBuffer = new SplitterPairEndSAMFileWriter(samFileWriter, file, List())
    var records = samRecordFactory.getRecords()
    
    before {
        // Initiate a clean SplitterPairEndSAMFileWriter for each test
        splitterWriter = new SplitterPairEndSAMFileWriter(samFileWriter, file, List())

        // Initiate a clean splitter writer with a buffer for each test
        samRecordFactory.addFrag("read1", 1, 1, true)
        samRecordFactory.addFrag("read2", 1, 2, true)
        val records = samRecordFactory.getRecords()
        splitterWriterWithBuffer = new SplitterPairEndSAMFileWriter(samFileWriter, file, records)

        // Initiate a clean SamRecordFactory for each test
        samRecordFactory = new SAMRecordSetBuilder(true, SortOrder.coordinate)

    }

    "A SplitterPairEndSAMFileWriter" should "write to paired end reads to the file" in {

        samRecordFactory.addPair("pair1", 1, 10, 20)
        samRecordFactory.addPair("pair2", 2, 10, 20)
        val mixedReadsPairedAndUnpaired = samRecordFactory.getRecords()

        for (rec <- mixedReadsPairedAndUnpaired) {
            splitterWriter.write(rec)
        }

        assert(splitterWriter.bufferIsEmpty === true)
    }

    it should "not write single end reads to the file, but they should be added to the buffer instead" in {

        samRecordFactory.addPair("pair1", 1, 10, 20)
        samRecordFactory.addPair("pair2", 2, 10, 20)
        val readNames = List("read1", "read2")
        samRecordFactory.addFrag(readNames(0), 1, 1, true)
        samRecordFactory.addFrag(readNames(1), 1, 2, true)
        val mixedReadsPairedAndUnpaired = samRecordFactory.getRecords()

        for (rec <- mixedReadsPairedAndUnpaired) {
            splitterWriter.write(rec)
        }
        assert(!splitterWriter.bufferIsEmpty)
        val buffer = splitterWriter.getBuffer
        assert(readNames.forall(expectedReadname => buffer.exists(actualRecord => actualRecord.getReadName().equals(expectedReadname))))
        assert(buffer.size === readNames.size)
    }

    it should "write the buffer" in {
        splitterWriterWithBuffer.writeBuffer()
        assert(splitterWriterWithBuffer.bufferIsEmpty)
    }

    it should "close after finishing writing" in {
        splitterWriter.close()
    }

    it should "check if the buffer is empty" in {
        assert(splitterWriter.bufferIsEmpty())
        assert(!splitterWriterWithBuffer.bufferIsEmpty)
    }

    it should "get the buffer" in {
        //TODO Implement a better test that checking that the read names are equal.
        // At the moment there seems to be some trouble with the equals method in SamRecord
        // which causes direct comparison of the records to be troublesome.
        val actual: List[String] = splitterWriterWithBuffer.getBuffer.toList.map(f => f.getReadName())
        val expected: List[String] = records.toList.map(f => f.getReadName())
        expected.foreach(expected => assert(actual.contains(expected)))
    }
}