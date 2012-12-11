package se.uu.medsci.splitter

import java.io.File

trait SplitterFileWriterFactory[T] {
    
    def createNewWriter(file: File): SplitterWriter[T]
    def createNewWriter(file: File, buffer: Iterable[T]): SplitterWriter[T]
    
}