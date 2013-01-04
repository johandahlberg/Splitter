package se.uu.medsci.splitter.fastq

import java.io.File

import org.scalatest.FlatSpec

import net.sf.picard.fastq.FastqRecord
import se.uu.medsci.splitter.SplitterException
import se.uu.medsci.splitter.SplitterWriter

class TestSplitterFastqFileWriterFactory extends FlatSpec {

    "A SplitterFastqFileWriterFactory" should "return a FastqFileWriter" in {
        val factory = new SplitterFastqFileWriterFactory        
        val file = File.createTempFile("testningfastq", ".fastq.gz")

        // The actual test
        val writer = factory.createNewWriter(file)
        assert(writer.isInstanceOf[SplitterWriter[FastqRecord]])
    }
    
    it should "throw an exception if it gets a FastqRecord Iterator \"buffer\" as an argument" in {
        val factory = new SplitterFastqFileWriterFactory
        val file = File.createTempFile("testningfastq", ".fastq.gz")
        val buffer = List(new FastqRecord("IRIS:7:1:17:394#0/1", "GTCAGGACAAGAAAGACAANTCCAATTNACATTATG", "IRIS:7:1:17:394#0/1", "aaabaa`]baaaaa_aab]D^^`b`aYDW]abaa`^"))

        // The actual test
        intercept[SplitterException]{
            val writer = factory.createNewWriter(file, buffer)
        }
    }

}