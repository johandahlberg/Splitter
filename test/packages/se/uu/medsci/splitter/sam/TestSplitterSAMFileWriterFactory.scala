package se.uu.medsci.splitter.sam

import java.io.File

import org.scalatest.Finders
import org.scalatest.FlatSpec

import net.sf.samtools.SAMFileHeader
import net.sf.samtools.SAMRecord
import se.uu.medsci.splitter.SplitterWriter
import se.uu.medsci.splitter.SplitterException

class TestSplitterSAMFileWriterFactory extends FlatSpec {

    val header = new SAMFileHeader()
    val presorted = true

    "A SplitterSAMFileWriterFactory" should "return a SpliterSAMFileWriter" in {
        val isPaired = true
        val factory = new SplitterSAMFileWriterFactory(header, presorted, isPaired)
        val file = File.createTempFile("testningbam", ".bam")

        // The actual test
        val writer = factory.createNewWriter(file)

        assert(writer.isInstanceOf[SplitterWriter[SAMRecord]])
    }

    it should "return a SpliterPairEndSAMFileWriter if isPaired is true" in {
        val isPaired = true
        val factory = new SplitterSAMFileWriterFactory(header, presorted, isPaired)
        val file = File.createTempFile("testningbam", ".bam")

        // The actual test
        val writer = factory.createNewWriter(file)

        assert(writer.isInstanceOf[SplitterPairEndSAMFileWriter])
    }

    it should "return a SpliterSingleEndSAMFileWriter if isPaired is false" in {
        val isPaired = false
        val factory = new SplitterSAMFileWriterFactory(header, presorted, isPaired)
        val file = File.createTempFile("testningbam", ".bam")

        // The actual test
        val writer = factory.createNewWriter(file)

        assert(writer.isInstanceOf[SplitterSingleEndSAMFileWriter])
    }

    it should "handle being passed a buffer if isPaired is true" in {
        val isPaired = true
        val factory = new SplitterSAMFileWriterFactory(header, presorted, isPaired)
        val file = File.createTempFile("testningbam", ".bam")

        val buffer = List(new SAMRecord(header))

        // The actual test
        val writer = factory.createNewWriter(file, buffer)

        assert(writer.isInstanceOf[SplitterPairEndSAMFileWriter])
    }

    it should "throw an exception if being passed a buffer if isPaired is false" in {
        val isPaired = false
        val factory = new SplitterSAMFileWriterFactory(header, presorted, isPaired)
        val file = File.createTempFile("testningbam", ".bam")

        val buffer = List(new SAMRecord(header))

        // The actual test
        intercept[SplitterException]{
            val writer = factory.createNewWriter(file, buffer)
        }        
    }

}