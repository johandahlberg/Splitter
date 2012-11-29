Splitter
--------

There is often a need to split files into smaller chuncks when working in bioinformatics. The headache you get from trying to parse a 150 Gb bam file in reasonable time might make you feel like you had to many [Skull Splitters](http://beeradvocate.com/beer/profile/118/402) last night. This is where Splitter comes in, it will split those nasty files for you to allow for simple scatter/gatter-parallelism, allowing you to get the job done on time.

Usage
-----

	Usage: Splitter [options] 

	  -i <file> | --input <file>
	        Path to input file to split
	  -d <dir> | --output_dir <dir>
	        Path to dir for output files.
	  -r <value> | --records_per_file <value>
	        Number of records to output per file. 100000 is default.

TODOs
-----
* Implement tests
* Add support for more formats, e.g. vcf

License
-------
MIT License.
