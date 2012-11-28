package se.uu.medsci.splitter

import java.io.File
import java.util.Iterator


abstract class SplitFileReader[T](inputFile: File) {

    def readRecord()
    def guessNumberOfRecords(waysToSplit: Int): List[Int]
    def iterator: Iterator[T]

}