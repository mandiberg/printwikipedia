POTENTIAL REFACTORING/ABSTRACTING/CAVEATS/POTENTIAL FIXES PLEASE READ ALL OF THESE!

PDF GENERATION
	-pageheaderevent is in DIRE NEED of refactoring. it should only be as complicated as TitlesFooter.java (which is what the TOC uses)-- or better yet, both TOC and PDF should use the thing to apply page headers and page numbers.
	-there should be a globally accessible method added in order to apply a cell in which text can be placed, aligned, indented, etc. The old method was to use content bytes which are unweildly and basic. Using the itext library's higher-level text placement functions is much better and acceptable by Java and leads to PDFs that show less errors on adobe acrobat preflight and are smaller in size.
		^this placement method is used in all places where text is added to the cover, the copyright page and for the header and footer elements inside the pdfs.
	-The fontstack should be placed in to it's own class where it can be called by other parts of this app. Right now it is placed into files that need to parse any complicated text. It takes up over 100 lines each time. The fontstack can only be created for one font size at a time. It would be best to create a method that would allow you to input whatever fontsize you deem necessary OR to just generate many fontsizes right off the bat and have these font variables be callable throughout the app.
		^also investigate if there is a way to detect if arabic characters are going to be used and if they can be rendered properly. Current iteration of pwiki does not properly render arabic or hebrew characters. There is a built-in itext function that should be able to handle this.
	--helvitica fixes
		update all libraries used in this project. One main thing contributing to the helvetica errors is the wikiprocessor library is outdated and some of the newer wikipedia templates were not recognized resulting in a rendered empty piece of text which lulu and adobe did not know how to process.
		If this does not work then try seeing if Latex is a better option instead of Itext.
	--files too-big fixes
		currently files are being generated until the end of a certain entry. This became problematic when generating some of the "list of" files with tables that would go on for hundreds of pages. 
		What should happen: break on whichever page you desire and save what's left in the entry and start with that on the next book.
	--how to generate the last file -- needs to be fixed, but here is a workaround:
		I did have this working at one point (I SWEAR). for whatever reason it did not generate the last file and on the last day of the exhibition i had to find a solution on the fly. This is what I came up with:
			1. Generate the last file and take note of the page it ends on. All entries should be fairly short.
			2. Go back a few pages and then find the pkey that represents the entry that starts that particular page
			3. edit your _output file to start from that pkey with the appropriate page number and pkey that you just got. volume number doesn't matter.
			4. edit the settings.xml to generate files that are going to end on the page you believe the last few entries will also end. 
			5. generate those
			6. put the files together with an adobe program. 
			7. rename the file appropriately.
	--outputing faster
		Currently on this mac mini we can output about 3 700 page files in a minute (pdf. toc is much faster). this slows down after about 10 minutes to 2 a minute and then gets gradually slower. The graceful-restart-mac.sh file in addition to the launchctl file that runs every few minutes as documented in the README.txt in meta-and-documentation does not work as it should. It is able to close the process once and then restart it but never again after that, when it should be happening once ever 5-10 minuets. I believe this is a permissions error as the launchctl must be run as not-root and the wiki process can be run as root or regular user. Did not have time to invest in solving this.

LULU UPLOAD

	--development vs. production twitter
		comment out the tweeter script in ohlu.py when testing so you don't have unecessary tweets.

	--ftp error during upload usually stemming from network connections or just failure to uploa
		this has been attempted to be fixed by adding a method for uploading after two tries of searching for the file in the iteratefiles function within crash_ohlu.py and ohlu.py. However, sometimes this still does not work so keep an eye on it these files would fail after too many attempts at stage 6.

	--there is an error in counting files. I start at 1 instead of 0 but this corresponds to the folder generation in the round_folder function in Volume class. The program does not look in to the correct folder for the proper file.

	--add a method to the Browser class in ohlu that can mointor the page title to confirm what stage the program is on. Sometimes it gets ahead or behind itself.
