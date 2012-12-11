package se.uu.medsci.splitter.sam

import net.sf.samtools._
import org.slf4j.LoggerFactory
import se.uu.medsci.splitter.SplitterWriter
import se.uu.medsci.splitter.Bufferable

class SplitterSAMFileWriter(samFileWriter: SAMFileWriter, buffer: Iterable[SAMRecord]) extends SplitterWriter[SAMRecord] with Bufferable[SAMRecord] {
    
    def this(samFileWriter: SAMFileWriter) =
        this(samFileWriter, Iterable[SAMRecord]())

    private val logger = LoggerFactory.getLogger(classOf[SplitterSAMFileWriter]);
    
    //Accept the buffer of SAMRecords and convert it to a map with the read name as key
    private val pairsFound: scala.collection.mutable.Map[String, SAMRecord] = {     
        val map: scala.collection.mutable.Map[String, SAMRecord] = scala.collection.mutable.Map()
        for(rec <- buffer){
           map += rec.getReadName() -> rec
        }
        map
    }

    def write(rec: SAMRecord) = {
        val recordName: String = rec.getReadName()

        // The logic here is to only write a read pair together.
        if (pairsFound.contains(recordName)) {
            val pairOfRead: SAMRecord = pairsFound.remove(recordName).get
            samFileWriter.addAlignment(rec)
            samFileWriter.addAlignment(pairOfRead)
        } else {
            pairsFound += recordName -> rec
        }

    }
    
    def writeBuffer() {
        for(rec <- pairsFound.values) {
            samFileWriter.addAlignment(rec)
        }
    }
    
    
    def bufferIsEmpty(): Boolean = {
        pairsFound.isEmpty
    }
    
    def getBuffer(): Iterable[SAMRecord] = {
        pairsFound.values
    }

    def close = {
        samFileWriter.close()
    }

}