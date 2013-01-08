package se.uu.medsci.splitter

import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen
import java.io.File

class SplitterTest extends FeatureSpec with GivenWhenThen with TestResources {

    feature("The user want to split a file") {
        scenario("he/she provides a fastq file") {
            given("all options are set correctly")
            val args: Array[String] = Array("-i", fastqFile.getAbsolutePath(), "-r", "500", "-o", outputDir, "-l", outputDir + fastqFile.getName() + ".list")

            when("the program is run")
            val outputFiles = Splitter.run(args)

            then("the program should run without errors")

            and("return correct files")
            val fileToMd5Map = Map[String, String]("exampleFastq.1.fastq.gz" -> "6c108ef2bb8f9e6b81fd57d8a874e5f6",
                "exampleFastq.2.fastq.gz" -> "7dd44848e6243e685cfa979441fa016d")
            assertFilesHaveCorrectMd5s(outputFiles, fileToMd5Map)

        }

        scenario("he/she provides a bam file") {
            given("all options are set correctly")
            // Setup options
            val args: Array[String] = Array("-i", bamFile.getAbsolutePath(), "-r", "10", "-o", outputDir, "-l", outputDir + bamFile.getName() + ".list")

            when("the program is run")
            val outputFiles = Splitter.run(args)

            then("the program should run without errors")

            and("return correct files")
            val fileToMd5Map = Map[String, String]("exampleBAM.1.bam" -> "da4b2835c56235c81ebcfa206866b5c2",
                "exampleBAM.2.bam" -> "d888ead223aa45709c506ebd2ff3a1a8",
                "exampleBAM.3.bam" -> "343c2a5953fa4c44e1855ae17609a68f",
                "exampleBAM.4.bam" -> "b76e0b746d3897319ffc842d6876064c")
            assertFilesHaveCorrectMd5s(outputFiles, fileToMd5Map)

        }

        scenario("he/she provides incorrect program arguments") {
            given("one or more options are set incorrectly")
            // Setup options
            val args: Array[String] = Array()

            when("the program is run")
            val outputFiles = Splitter.run(args)

            then("the program should return a usage message")
            // TODO Write the actual usage message test.
        }
    }

}