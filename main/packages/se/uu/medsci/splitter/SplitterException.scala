package se.uu.medsci.splitter

case class SplitterException(msg: String = "There was an error of unkown type.") extends Exception(msg)