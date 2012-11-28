package se.uu.medsci.splitter

trait Writer[T] {
        
    def write(record: T)
    
    def close()
    
}