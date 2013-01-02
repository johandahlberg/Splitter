package net.sf.picard.sam

import java.io.File
import org.slf4j.LoggerFactory

class SplitterFixMatePairInformation(file: File) extends FixMateInformation {

    
    private val logger = LoggerFactory.getLogger(classOf[SplitterFixMatePairInformation]);

    
    val inputFileList = new java.util.LinkedList[File]()
    inputFileList.add(file)
    this.INPUT = inputFileList
    val tmpDir = new java.util.LinkedList[File]()
    tmpDir.add(new File(System.getProperty("java.io.tmpdir")))
    this.TMP_DIR = tmpDir
    

    def run() {
        logger.info("Fixing the mate pair info using Picard FixMateInformation.")
        super.doWork()
    }

}