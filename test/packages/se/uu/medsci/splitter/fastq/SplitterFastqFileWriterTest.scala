package se.uu.medsci.splitter.fastq

import org.scalatest.FlatSpec
import java.io.File
import net.sf.picard.fastq._
import collection.JavaConversions._

class SplitterFastqFileWriterTest extends FlatSpec{

    // Setting up for the tests
    val file = File.createTempFile("testningfastq", ".fastq.gz")
    val writer = new SplitterFastqFileWriter(file)
    val record = new FastqRecord("IRIS:7:1:17:394#0/1", "GTCAGGACAAGAAAGACAANTCCAATTNACATTATG", "IRIS:7:1:17:394#0/1", "aaabaa`]baaaaa_aab]D^^`b`aYDW]abaa`^")

    "A SplitterFastqFileWriter" should "write a record to a file" in {

        // The actual test
        writer.write(record)
        // The writer has to be closed before the output can be checked.        
        writer.close()
        
        val reader = new FastqReader(file)
        val iterator = reader.iterator()
        var counter = 0;
        for (rec <- iterator) {
            counter = counter + 1
            assert(record.getBaseQualityHeader() === rec.getBaseQualityHeader())
            assert(record.getBaseQualityString() === rec.getBaseQualityString())
            assert(record.getReadHeader() === rec.getReadHeader())
            assert(record.getReadString() === rec.getReadString())
        }
        assert(counter === 1)
    }

    it should "close the file after finished writing" in {
        // The actual test
        writer.close()
        
        // If the stream is closed a IOExecption will be thrown.
        intercept[java.io.IOException] {
            writer.write(record)
        }
    }

}