Splitter
--------

There is often a need to split files into smaller chuncks when working in bioinformatics. The headache you get from trying to parse a 150 Gb bam file in reasonable time might make you feel like you had to many [Skull Splitters](http://beeradvocate.com/beer/profile/118/402) last night. This is where Splitter comes in, it will split those nasty files for you to allow for simple scatter/gatter-parallelism, allowing you to get the job done on time.

Usage
-----
`java -jar <path to Splitter.jar> <options>`


	Usage: Splitter [options] 

	  -i <file> | --input <file>
	        Path to input file to split
	  -d <dir> | --output_dir <dir>
	        Path to dir for output files.
	  -l <file> | --list <file>
	        List of output files.
	  -r <value> | --records_per_file <value>
	        Number of records to output per file. 100000 is default.
	  --pe <value>
	        If splitting a bam file use to indicate that the data is paired. If you are splitting a file with single end data, set to false. Default is true.


Building
--------
Build using `ant`

Testing
-------
Build and run tests using `ant test`

TODOs
-----
* Implement tests
	- Some basic tests are implemented, but more wouldn't hurt
* Add support for more formats, e.g. vcf

Credits
-------
Project setup from using the [java-scala-ivy-template](https://github.com/zsellera/java-scala-ivy-template).
Command line arguments are parsed using [scopt](https://github.com/scopt/scopt).
Uses [Picard](http://picard.sourceforge.net/) for a lot of the file parsing.

License
-------
MIT License.
