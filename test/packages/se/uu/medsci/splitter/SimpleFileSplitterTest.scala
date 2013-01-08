package se.uu.medsci.splitter

import org.scalatest.FlatSpec
import java.io.File
import net.sf.picard.fastq.FastqRecord
import net.sf.samtools.SAMRecord

class SimpleFileSplitterTest extends FlatSpec with TestResources{

    "A SimpleFileSplitter" should "return the correct type of file splitter if it gets a fastq file" in {

        val actual = SimpleFileSplitter.newFileSplitter(fastqFile, outputDir, true)
        assert(actual.isInstanceOf[SimpleFileSplitter[FastqRecord]])
    }

    it should "return the correct type of file splitter if it gets a bam file" in {
        val actual = SimpleFileSplitter.newFileSplitter(bamFile, outputDir, true)
        assert(actual.isInstanceOf[SimpleFileSplitter[SAMRecord]])
    }

    it should "throw a understandable exception if the file type does not match" in {
        val unrecognizedFile = File.createTempFile("testningfastq", ".unkown.file.ending")
        intercept[SplitterException] {
            SimpleFileSplitter.newFileSplitter(unrecognizedFile, outputDir, true)
        }
    }

    it should "split a fastq file to new files with exactly as many per file as specified by the user" in {
        val splitter = SimpleFileSplitter.newFileSplitter(fastqFile, outputDir, true)
        val actual = splitter.split(500)

        // 2 files should be create from the example file.
        assert(actual.size === 2)

        val linesInFirstFile = wordCountZcatCommand(actual(0))
        val linesInSecondFile = wordCountZcatCommand(actual(1))
        assert(linesInFirstFile === 2000)
        assert(linesInSecondFile === 2000)
    }
    
    it should "split a bam file to new files with approximatly as many per file as specified by the user" in {
        val splitter = SimpleFileSplitter.newFileSplitter(bamFile, outputDir, true)
        val actual = splitter.split(10)

        // 4 files should be create from the example file.
        assert(actual.size === 4)

        // TODO Test this in a better way. But this should cover most cases, as the test file contains both paired end and
        // single end reads.        
        val linesInFirstFile = wordCountSamtoolsCommand(actual(0))
        val linesInSecondFile = wordCountSamtoolsCommand(actual(1))
        val linesInThridFile = wordCountSamtoolsCommand(actual(2))
        val linesInFourthFile = wordCountSamtoolsCommand(actual(3))
        assert(linesInFirstFile === 5)
        assert(linesInSecondFile === 10)
        assert(linesInThridFile === 10)
        assert(linesInFourthFile === 8)
    }
    
    /**
     * Help function used for checking the number of lines in a file,
     * calling using the "wc -l" system command to do so.
     */
    def wordCountZcatCommand(file: File): Int = {
        import scala.sys.process._
        val zcat = "zcat " + file.getAbsolutePath()
        val wc = "wc -l"
        (zcat #| wc !!).asInstanceOf[String].stripLineEnd.toInt
    }

     /**
     * Help function used for checking the number of lines in a file,
     * calling using the "samtools view" native command to do so.
     * Please note that samtools has to be installed for this to work.
     */
     def wordCountSamtoolsCommand(file: File): Int = {
        import scala.sys.process._
        val samView = "samtools view " + file.getAbsolutePath()
        val wc = "wc -l"
        (samView #| wc !!).asInstanceOf[String].stripLineEnd.toInt
    }

}