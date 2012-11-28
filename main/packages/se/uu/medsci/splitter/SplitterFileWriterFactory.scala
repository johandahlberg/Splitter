package se.uu.medsci.splitter

import java.io.File

trait SplitterFileWriterFactory[T] {
    
    def createNewWriter(file: File): Writer[T]
    
}