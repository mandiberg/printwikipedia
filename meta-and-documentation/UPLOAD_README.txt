===notes to consider for next iteration of the printwikipedia project and during upload===

*****PRE UPLOAD*******

	--general notes and possible improvements for ohlu.py and other upload files.
		GENERAL:
			DO NOT GET COMFORTABLE WITH CONSISTENCY. your expectations for how this code will run will change every day as the network changes and you will find more bugs.
			sometimes working through javascript is easier than using selenium.
			probably by the time you do this again the publishing process on lulu will be slightly different.		
			the files:
				ohlu.py : first one that should be run. goes through all the files in order.
				crash_ohlu.py : should be run as cleanup. get a list of the failed files to try again with. these should be put in an array in the file crash_list.py
				crash_too_big.py : was made because of helvetica errors in the covers. redistill the covers using adobe distiller with the lulu.joboptions file. this will 		start you right from the upload cover page and will go from there. use crash_list in the same way for failed helvetica cover files.
				whereFile.js : aids in iterating through files faster during the iteration page of the pdf creation. This should be integrated into the lulu_hax.js on 			greasemonkey
				lulu_hax.js : ignores alerts and other things that will break the code. skips lulu's suggested coverwizard. sometimes was troubling to do via selenium.
				potentially_useful_utilities/ : folder that contains some potentially helpful files for moving things around, organizing files and comparing your completed 	json to your error files


			general technical:
				-you may want to remove some of the sleep(x) from the file. we were dealing with specific constraints and learned where the slowdowns were and what to look for.
				-ohlu.py could maybe remove the other iteratefiles function
				-implement the evalPageTitle function.  
				-!fix the check2 javascript function! right now it is commented out. for some reason i began getting an error saying that it could not run the file.
				-Add the functions from whereFile.js into the greasemonkey script so that they are automatically available and will take up less code.

			--user accounts
				make sure to use a development account for all testing and development. Switch to a fresh user with empty projects and empty FTP for actual upload.

			--keep each user to about 1000 files
				*nameofuser+<<anynumber>>@url.com would be the way to organize users. the +# still makes the email show up at nameofuser@url.com 

		STEPS:
			1. set up payment details in lulu in projects. there is a link on the side navigation on the left of the page.
			2. each user should have about 1000 files inside. Make users as necessary (e.g. someemail+1@blah.com, someemail+2@blah.com, etc.)

***DURING UPLOAD*******:
	
	-- if it crashes:
		*hopefully it crashed gracefully and is able to continue on to the next file. If this happens, you will still see output being generated in the terminal. The last file should be noted in your crasherrfiles.txt file and you should reference that to see why it failed.
		*if you do not see output on the terminal and instead see the shell prompt (ie: computername:folder user# (<-- varies depending on what system. this is for mac)) then follow the following steps to restart:
		
			1. figure out what file failed. you can do this by:
				a) scrolling up in the terminal until you see the name of the last file that was being uploaded. It would appear in a string like this:  
					"splitting strings, encoding for unicode to make firefox happy. sending off file: 5949&&&Rural Municipality of Terrell No. 101&&&Rush Street (album)&&&.pdf"
					5949&&&Rural Municipality of Terrell No. 101&&&Rush Street (album)&&&.pdf <-- this is the name of the file!
				b) check the crasherrfiles and the json success file and see what the last number (the higher of the two) was that either failed or completed. The number AFTER that one is likely the one that crashed the program. Go to the terminal where the program was running and type: 
					ls in/folderitshouldbein/volnumberyouwant* 
				this should print out the filename you want.
			2. take that file name and put it in to the pass.py file for input_file as a string so it looks like: input_file = "1234&&&thefilename&&&secondtitle&&&.pdf"
			3. run python ohlu.py (or whatever program you're on) in the terminal.

	--  monitor cron.py for twitter in the potentially useful folder.
	-- if you are away from the computer doing the upload. You can connect via TeamViewer to make quick changes or restart. https://www.teamviewer.com
	-- monitor your crasherrfiles to see if there are consistant types of files that are breaking or if there's a certain point in the process where they are failing.
	-- connection issues
		Alter your router in your network in order to give the computer that you are running the upload on priority over other devices. This will be specific for each router so search for your router and how to set bandwidth priority. Also it is a very good idea to turn off other devices that would be connected to the router.
		/etc/hosts file ignore metrics + ad sites (metrics.lulu.com, ad.google.com, etc.) by setting the address to 127.0.0.1 (localhost) to avoid hanging on those page loads


*****POST UPLOAD*****:

	parse your crasherrfiles and compare it to your successful json to see if a particular file actually failed or if you started the process over and it worked the second time after recording it's failure.
		-open the file that contains the list of failures in sublimetext or somethingthat allows you to do some regex searching.
		-turn on regex search and type into the find ^\d\d\d\d this searches for the beginning of the string followed by 4 digits-- the volume number.
			*if you want to search for a specific kind of error use this regex: ^\d\d\d\d.+failed too many at stage: 0 ---- changing the string after .+ to whatever error you wish to find. put another .+\n AFTER the string to get to the end of that particular line with the error you want.
		-in sublimetext you can then select "find all", cut , open a new document, paste.
		-regex search again for \n and click "find all"
		-press the comma button then a space. add brackets to either end and now you have a python array to parse.
			*you may need to remove leading zeroes! this can be done with a bit more regex. I just did a search for \s[0]* and removed all zeroes that were preceded by a whitespace, because supposedly the ending zeroes would have a comma after them.

	Final check:
		1. get lulu to send the csv of the available books on all users used
		2. compare to your json see that all volumes are uploaded and available.
			If they aren't: 
				-get list of files
				-put into crash_list and run crash_ohlu against them in a new user (e.g. printwikipedia+8) to be sure there will be no conflicts.
				-check new json again against the lulu csv/xls file and repeat the few steps as necessary
				-if you've lost all hope, upload manually. use "js for additional details page.js" to make it a bit easier.
		3. use json to create SVGs
		4. svg images hosted on aws






