package se.uu.medsci.splitter

trait SplitterWriter[T] {
        
    def write(record: T)   
    
    def close()
    
}