package se.uu.medsci.splitter

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import org.scalatest.Assertions._

/**
 *
 * Trait for storing paths to test resources and some utility methods
 */
trait TestResources {
    val outputDir = System.getProperty("java.io.tmpdir") + "/"
    val fastqFile = new File("resources/exampleFastq.fastq.gz")
    val bamFile = new File("resources/exampleBAM.bam")

    /**
     * Check that the files in the files list and the files file names in the fileToMd5Map are
     * the same. Used for checking that a program return correct output files.
     */
    def assertFilesHaveCorrectMd5s(files: List[File], fileToMd5Map: Map[String, String]) = {
        val allFilesAndMd5sCorrect = files.forall(file => {
            fileToMd5Map.contains(file.getName()) == true &&
                calculateMd5Sum(file).equals(fileToMd5Map(file.getName))
        })

        assert(fileToMd5Map.size === files.size)
        assert(allFilesAndMd5sCorrect === true)
    }

    /**
     * Utility function for calculating md5 checksum for a file and returning it as
     * a hex string.
     */
    def calculateMd5Sum(file: File): String = {

        val inputStream = new FileInputStream(file)
        val md5Digest = MessageDigest.getInstance("MD5")
        val buffer = new Array[Byte](8192)

        // Read the input stream and feed it to the MessageDigest algorithm
        // via a recursive approach
        def getDigest(inputStream: InputStream, digest: MessageDigest): BigInteger = {

            val read = inputStream.read(buffer)
            // End of file reached
            if (read == -1)
                new BigInteger(1, digest.digest())
            else {
                // Unless end of file is reached update the digest
                // and keep reading
                digest.update(buffer, 0, read)
                getDigest(inputStream, digest)
            }
        }
        getDigest(inputStream, md5Digest).toString(16)
    }
}