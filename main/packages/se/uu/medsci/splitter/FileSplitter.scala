package se.uu.medsci.splitter

import java.io.File

trait FileSplitter {
  
  def split(numberOfRecordsPerFile: Long): List[File]

}