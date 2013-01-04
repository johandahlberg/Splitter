package se.uu.medsci.splitter

import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen

class TestSplitter extends FeatureSpec with GivenWhenThen with TestResources{

    feature("The user want to split a file") {
        scenario("he/she provides a fastq file") {
            given("all options are set correctly")
            // Setup options
            val args: Array[String] = Array("-i", fastqFile.getAbsolutePath(), "-r", "500", "-o", outputDir , "-l", outputDir + fastqFile.getName() + ".list")            
            
            when("the program is run")            
            Splitter.run(args)
                       
            then("the program should run without errors")

            and("return correct files")           
        }

//        scenario("he/she provides a bam file") {
//            given("all options are set correctly")
//            // Setup options
//
//            when("the program is run")
//            // Run program
//
//            then("the program should run without errors")
//
//            and("return correct files")
//
//        }
//
//        scenario("he/she provides incorrect program arguments") {
//            given("one or more options are set incorrectly")
//            // Setup options
//
//            when("the program is run")
//            // Run program
//
//            then("the program should return a usage message")
//        }
    }

}