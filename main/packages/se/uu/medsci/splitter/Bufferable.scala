package se.uu.medsci.splitter

trait Bufferable[T] {

    def writeBuffer(): Unit
    
    def bufferIsEmpty(): Boolean
    
    def getBuffer(): Iterable[T]
}