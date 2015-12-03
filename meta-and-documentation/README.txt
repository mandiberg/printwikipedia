Hello future Michael or anyone else interested in printing Wikipedia!

This will remind or teach you how to get printwiki up and running.

The first thing you will need to do is to create a new directory where you want to
store the printwiki project and all of its components. (It isn't absolutely necessary 
that everypart of the project is in the same directory but it makes things easier to 
manage.)

**************************************************************************************
Setting up the database

This first thing you will want to start doing is setting up the database, as it will 
take a long time to write all of the article entries into the database and you can use 
this time to get other parts of the project ready to go.

--------------------------------------------------------------------------------------
Tools
First we need a few things in order to be able to host and use the database

1. MySQL 
	This can be downloaded from: http://dev.mysql.com/downloads/mysql/
2. MySQL Workbench 
	This can be downloaded from: http://dev.mysql.com/downloads/tools/workbench/5.2.html
3. The Wikipedia database .bz2 file. Put this in the folder where you will run the 
project from.
	You can get this from : 
4. The "mwdumper.jar" file from the GitHub repository. Copy this into the same folder 
you put the bz2 file into in step 3.
5. The "structure.sql" file from the "meta-and-documentation" folder in the GitHub 
repository, once again move this to your project folder.
6. 40+ GB of free space to store the database in (As time goes on and Wikipedia grows 
this number will increase)

You now have everything you need in order to get the database set up
--------------------------------------------------------------------------------------
Now that we the necessary tools we can set the database up!
--------------------------------------------------------------------------------------
Increasing buffer size
	Open my.cnf -- on linux machines it is in /etc/my.cnf on Mac it is in /usr/local/mysql/support-files/
	uncomment the line that reads "innodb_buffer_pool_size" and make it about =70% of your total RAM
	I.E. if you have 16GB of RAM, innodb_buffer_pool_size=11G
	Restart mysql (http://coolestguidesontheplanet.com/start-stop-mysql-from-the-command-line-terminal-osx-linux/)
--------------------------------------------------------------------------------------
Setting up a schema
	Open MySQL Workbench.
	Double Click on the existing Local Instance. 
	In the top left corner click the "create new schema button" (The symbol is the 2 
	golden pucks stacked ontop of eachother)
	Give the schema a name and SWITCH COLLATION TO "utf8 - default collation", then 
	click "Apply" in the bottom left corner
--------------------------------------------------------------------------------------
Structuring the schema
	Open the terminal or command prompt
	Navigate to the folder you stored the "structure.sql" file.
	Type "mysql -u root" to login to the mysql client
	Next type "USE yourschemaname" to switch to your newly made schema, you should see 
	a message saying "Database changed" in your terminal window
	Finally type "\. structure.sql" this will apply the needed structure to your schema 
	in order for it to hold the printwikipedia database
	
	Alternately, do it via a query in MySQLWorkbench
	Type "USE thenameyougaveyourschema;"
	Then paste in the contents of structure.sql
--------------------------------------------------------------------------------------
Building the Database
	**************
	NOTE : You must have structured the schema before doing this, if you do not this 
	process will do NOTHING
	**************
	Now we just need to fill the schema with all the data.
	In the terminal window navigate to the folder where you have the "mwdumper.jar" and 
	bz2 file stored (This can be the folder you are already in)
	Type "java -jar mwdumper.jar --format=sql:1.5 yourbz2filename.bz2 --filter=latest --
	filter=notalk | mysql -u root -p --default-character-set=utf8 yourschemaname" into 
	the terminal (replace yourbz2filename and yourschemaname with the correct names)
	If everything is working correctly the terminal will start to print messages like 
	"11,000 pages (367.432/sec), 11,000 revs (367.432/sec)"
	This will take some time, likely 10+ hours, if you feel like it you may set up 
	other areas of the project, however there are still steps you must do to set up the 
	database after this process finishes.
--------------------------------------------------------------------------------------
Speeding up the Database
	Now you have all the information necessary to start creating outputs, unfortunately 
	if you try running it right now it will be horrendeously slow, inorder to fix this 
	we will be indexing the database. 
	Open the terminal and navigate to the folder you stored "newmaster.sql" in.
	Type "mysql -u root" and login to the mysql client
	Next type "\.newmaster.sql".
	This will create a new table in the database and move the articles into it ordered by
	article number and thus speeds up the database. The query also removes most of problem articles we 
	were running into.
	This process takes a long time so don't be alarmed if you have to wait a while.
	Congratulations your Database is ready to go!
--------------------------------------------------------------------------------------
Working with the test Database snippet
	For testing, we have made a smaller snippet of the finished DB to work on.
	DL the 70K entry database snippet here:
	http://theredproject.com/foryou/misc/newmaster%20dump--%20pkey%2070000.sql
	Dump this into the schema via command line or MySQLWorkbench
	Don't forget to "USE thenameyougaveyourschema;"
	**this is the sped up database**

--------------------------------------------------------------------------------------
Using a wiki dump
	This page provides some of the past dumps of wikipedia http://dumps.wikimedia.org/enwiki/
	The latest and largest dump is located at this address: http://dumps.wikimedia.org/enwiki/enwiki-latest-pages-articles.xml.bz2
	The best way to download this is by using nohup and wget to make sure it goes through (these are very large files!)
	like so: nohup wget -bcq <<http address of your file>> 
--------------------------------------------------------------------------------------

**************************************************************************************
Database for Table of Contents
--------------------------------------------------------------------------------------

The Table of Contents database is much smaller and easier. It is really just a list of entries. Go here: http://dumps.wikimedia.org/enwiki/latest/ and download this file: enwiki-latest-all-titles-in-ns0.gz. Unzip it (it should be called enwiki-latest-all-titles-in-ns0) and put it in the "output" directory (see below if it doesn't exist yet.


**************************************************************************************
Setting up the project to run
--------------------------------------------------------------------------------------
Housekeeping
There is a list of things you will need to have in your project directory inorder to be able to successfully run printwiki:
1. The "fonts" directory: 
	This is found in your GitHub repository. Simply copy it to the directory where you 
	will be running printwiki from.
2. The "logs" directory:
	This is also in the GitHub repository. Once again copy it.
3. The "output" directory:
	This stores all of the output pdfs. In the directory where you are running 
	printwiki make a new folder labelled "output".
4. The settings file: 
	In the meta-and-documentation folder where this README is stored there is a file 
	labelled settings.xml. Copy this file into the directory you are running printwiki 
	from.
--------------------------------------------------------------------------------------
Building the wikitopdf.jar
	The "wikitopdf.jar" file is the file that actually converts the database into pdfs. 
	You will need to build it from the NetBeans project in your GitHub repository. 
	Start NetBeans and open the "printwikipedia" NetBeans Project in your GitHub 
	repository.
	It will come up with a window about Project Problems

Resolving Problems
	Click "Resolve Problems…" then select "scala-library-2.9.1-1.jar" from the "Project
	Problmes:" list, and click "Resolve…"
	In the popup window navigate to the "lib" folder in the GitHub "printwikipedia"
	repository and find the "scala-library-2.9.1-1.jar" file. Select it and click 
	"Choose". A green check should replace the yellow triangle beside 
	"scala-library-2.9.1-1.jar" in the Project Problems window.
	Next select "akka-actor-2.0.jar" from the conflicts window and once again navigate 
	to the "lib" folder in your GitHub (it may open here by default this time)
	"printwikipedia" repository and select the "akka-actor-2.0.jar" file. 
	Select "junit" and click on "Resolve." Click yes to install that library.
	You will not be able to resolve the "JAX-WS-ENDORSED" error in this interface. C
	Click "Close" to exit from the Project Problems window.
	
JAX-WS-ENDORSED
	Netbeans will create the JAX-WS-ENDORSED library when a new web service is created. Create a new throwaway project which you can call something like "JAX-delete-me".  
Create a new file via File > New File, choose Web Service > Web Service Client. On the next screen enter the following into the WDSL URL field: http://www.w3.org/2001/04/wsws-proceedings/uche/wsdl.html That should create the required libraries. (more info here: http://stackoverflow.com/questions/6207190/how-do-i-reference-libraries-in-netbeans)

Building	
	Now simply click the build button in the upper left portion of NetBeans (sybolized 
	by a hammer). It should display "BUILD SUCCESSFUL" in green letters in the bottom 
	of the window.
	This will have created a file named "wikitopdf.jar" in the folder labelled "dist" 
	in the "printwikipedia" repository in your GitHub repositories folder.
	Find and open the folder called "dist" and copy the "wikitopdf.jar" file into your 
	project directory (the same one where you copied "fonts" "logs" "output" etc.)
	You now have everything you need to start running printwikipedia, all that is left 
	to do is set up the settings.
--------------------------------------------------------------------------------------
Settings
	To control the behavior of "wikitopdf.jar" you can manipulate the "settings.xml" 
	file.
	Right click "settings.xml" and open it in the text editor of your choice. 
	This first thing we will need to do is get put in the settings for the database. 
	If you are hosting the database on the same computer that you are running printwiki 
	on then under <db-host> write 127.0.0.1.
	If you are hosting the database on a seperate computer enter the IP address of that 
	computer under <db-host>.
	Next fill in the <db-port> with 3306 (default port for mysql) 
	Under <db-name> enter the name of the schema you are using to hold the database
	Under <db-user> type "root", and leave <db-pass> blank
	
	The next 5 settins all control the behaviour of "wikitopdf.jar"
	<article-bunch> controls how many articles you will query from the database at a 
	time. For speeds sake it is a good idea to always query much more than you will 
	need as the time needed to find the articles is much larger than the time needed to 
	move them.
	<page-file-limit> determines the minimum pdf pages that will be written to a single 
	pdf document. 
	<start-page> determins what article number you will begin creating pdfs from
	<time-limit> controls how long the "wikitopdf.jar" will run before gracefully 
	shutting down (more on this later).
	<thread-limit> controls the number of different threads you will be using if you 
	are using the threaded version (at time of writing the threaded version was not 
	working, this may change in the future).
	
	The settings we originally ran on were : 
	<article-bunch>
		5000
	</article-bunch>
	<page-file-limit>
		670
	</page-file-limit>
	<start-page>
		6704441
	</start-page>
	<time-limit>
		1620
	</time-limit>
--------------------------------------------------------------------------------------
**************************************************************************************
Running Manually
	You will likely want to run "wikitopdf.jar" manually a few times inorder to ensure 
	that everything is working and to familiarize yourself with the outputs and settins.
	To do so open a terminal or command prompt window. 
	Navigate to the directory with "wikitopdf.jar" in it.
	Type "java -jar wikitopdf.jar" to start the program running on a single-treaded 
	processor
	There are a series of different arguments you can use to specify different actions 
	the "wikitopdf.jar" can do
	1. "java -jar wikitopdf.jar pdf" the defualt settins, exactly the same as above.
	2. "java -jar wikitopdf.jar threaded" runs it using a multi-threaded processor (at 
	the time of writing this is not working)
	3. "java -jar wikitopdf.jar toc" creates the table of contents volumes
	4. "java -jar wikitopdf.jar pagenumbers" does nothing as of yet
	5. "java -jar wikitopdf.jar covers" creates the coverpages for the outputs in the 
	output file. 
		*****before outputting covers check to make sure that each file is sequential and there are no files with two of the same volume number (ie. the amount of volumes matches the total number of pdfs in the output/temp folders not including the _output.pdf file). This has happened before!
	Have some fun experimenting with the different settings to familiarize yourself 
	with how the progam works, and make sure things are working correctly
--------------------------------------------------------------------------------------
Possible Problems:
	
	1. The "wikitopdf.jar" uses large amounts of dynamic memory. On some machines this will result in frequent crashing. In order to fix this add the "Xmx1024m" argument to your java command: "java -Xmx1024m -jar wikitopdf.jar ..."
	**************
	NOTE : If this is an issue edit the .bat files that you will use for auto running the program to include the "Xmx1024m" argument as well.
	**************
	
	2. When trying to run mwdumper.jar if an error appears saying: "ERROR 1366 (HY000) at line 54: Incorrect string value: '\xF0\x90\xA1\x80\x0A|...' for column 'old_text' at row 2" it will not add the mediawiki data to the database because the sql connection has just closed on that error. This error means that there is a piece of string data that is too large for the mysql character set in the text table.
	To fix this drop your schema and start over again and edit the structure.sql file from line 757 to 764 to:
		DROP TABLE IF EXISTS `text`;
		/*!40101 SET @saved_cs_client     = @@character_set_client */;
		/*!40101 SET character_set_client = utf8mb4 */;
		CREATE TABLE `text` (
		  `old_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
		  `old_text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
		  `old_flags` tinyblob NOT NULL,
		  PRIMARY KEY (`old_id`)
		) ENGINE=InnoDB AUTO_INCREMENT=349963463 DEFAULT CHARSET=utf8 MAX_ROWS=10000000 AVG_ROW_LENGTH=10240;
		/*!40101 SET character_set_client = @saved_cs_client */;
	run the structure.sql code against your schema and then in your regular terminal
	run: "java -jar -Xmx6024m mwdumper.jar --format=sql:1.5 enwiki-late-pages-articles.xml.bz2 --filter=latest --filter=notalk | mysql -u root --default-character-set=utf8mb4 NAMEOFYOURSCHEMA" without quotes.

	3. While MWDumper is running you may get a mysql error that pops up. It can be anything from invalid characters to something in your database structure not being large enough... This can either be because we did not set up the structure.sql file properly or someone who was editing wikipedia put something in there that messes up the dump. One way of telling that you got this error is if you do a count of ids for the page table (SELECT COUNT(page_id) from page;) it will be a number ending in three zeroes. This is probably incorrect as you have a 1 in 1000 chance of landing on 1000 exactly. Scroll up in the terminal window where mwdumper was running until you see a mysql error or something other than (for example) 
	7,445,000 pages (49.169/sec), 7,445,000 revs (49.169/sec)
	Your order of operations to fix the problem should be as follows:
		a) Identify the line of code that has the error. google it. read stackoverflow about why you got that error. if it says that a column in your table was too small for the data make that column bigger if possible.
		b) If you think it's something wrong with the dump-- a bad character, malformed data, etc. contact someone in the mailinglist for the dumps https://lists.wikimedia.org/mailman/listinfo/xmldatadumps-l (it's fun to give back) and see if they can help you.
		c) If you can't figure out what's wrong then you might have to use a different dump or wait until next month to get new data.
	If you've identified the problem via option a and have fixed your mysql database or if you know where the issue is via option b in the xml file and want to delete that piece of data you can then take the rest of the dump and append it to wherever mwdumper took you successfully.
		-Use mysql to get the last entry in your database: SELECT * FROM page ORDER BY page_id desc limit 1;
		-bunzip your bz2 dump that you're using which had the error.
		*****It's not recommended to do ^this^ if you are using a very large dump (one of the 11GB+). It will take absolutely forever to bunzip and then do the following steps. If possible get a smaller one and find out if where you failed is within that. This is good if you know you were close to the end of the dump. Otherwise i would recommend you use option c.
		-use grep -an "YOUR LAST TITLE" FILENAME and make note of the line number that you get for a result that looks like 
			<title>YOURLASTTITLE</title>
		-use sed to trim back about 300 lines to be safe and output your file to something you can open in a text editor.
			sed -n '251540000,$'p YOURFILENAME > NEWFILENAME
		-open that new file in a text editor look for your entry within <title></title> scroll up until you see <page> and delete everything before that.
		-add this xml to the top 
			<mediawiki xmlns="http://www.mediawiki.org/xml/export-0.10/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.mediawiki.org/xml/export-0.10/ http://www.mediawiki.org/xml/export-0.10.xsd" version="0.10" xml:lang="en">
			  <siteinfo>
			    <sitename>Wikipedia</sitename>
			    <dbname>enwiki</dbname>
			    <base>http://en.wikipedia.org/wiki/Main_Page</base>
			    <generator>MediaWiki 1.25wmf23</generator>
			    <case>first-letter</case>
			    <namespaces>
			      <namespace key="-2" case="first-letter">Media</namespace>
			      <namespace key="-1" case="first-letter">Special</namespace>
			      <namespace key="0" case="first-letter" />
			      <namespace key="1" case="first-letter">Talk</namespace>
			      <namespace key="2" case="first-letter">User</namespace>
			      <namespace key="3" case="first-letter">User talk</namespace>
			      <namespace key="4" case="first-letter">Wikipedia</namespace>
			      <namespace key="5" case="first-letter">Wikipedia talk</namespace>
			      <namespace key="6" case="first-letter">File</namespace>
			      <namespace key="7" case="first-letter">File talk</namespace>
			      <namespace key="8" case="first-letter">MediaWiki</namespace>
			      <namespace key="9" case="first-letter">MediaWiki talk</namespace>
			      <namespace key="10" case="first-letter">Template</namespace>
			      <namespace key="11" case="first-letter">Template talk</namespace>
			      <namespace key="12" case="first-letter">Help</namespace>
			      <namespace key="13" case="first-letter">Help talk</namespace>
			      <namespace key="14" case="first-letter">Category</namespace>
			      <namespace key="15" case="first-letter">Category talk</namespace>
			      <namespace key="100" case="first-letter">Portal</namespace>
			      <namespace key="101" case="first-letter">Portal talk</namespace>
			      <namespace key="108" case="first-letter">Book</namespace>
			      <namespace key="109" case="first-letter">Book talk</namespace>
			      <namespace key="118" case="first-letter">Draft</namespace>
			      <namespace key="119" case="first-letter">Draft talk</namespace>
			      <namespace key="446" case="first-letter">Education Program</namespace>
			      <namespace key="447" case="first-letter">Education Program talk</namespace>
			      <namespace key="710" case="first-letter">TimedText</namespace>
			      <namespace key="711" case="first-letter">TimedText talk</namespace>
			      <namespace key="828" case="first-letter">Module</namespace>
			      <namespace key="829" case="first-letter">Module talk</namespace>
			      <namespace key="2600" case="first-letter">Topic</namespace>
			    </namespaces>
			  </siteinfo>	
		-run this command: bzip2 NEWEDITEDFILE
		-create a new schema with a new name using the steps above
		-run mwdumper on your newly bzip'd file into the schema you just created.
		-once it's done use mysqldump to create a sql file 
			 mysqldump -n -t -u YOURNAME --password=pw NEWSCHEMA > DUMPEDFILE.sql
		-then run that into your original dump
			mysql -u YOURNAME -p -D ORIGINALSCHEMA < DUMPEDFILE.sql
		-pat yourself on the back

**************************************************************************************
Setting up Graceful Restarts
	"wikitopdf.jar" has built in restart capabilities that allow it to start up from 
	where it left off after it crashes or exits.
	**************
	NOTE: This restart process is based on the pdf files in the "output" folder, if you 
	want it to restart properly DO NOT delete the finised volumes in the output folder.
	**************
	At the time of writing there was a memory leak in the "wikitopdf.jar" file that 
	would cause it to slow down as it was running. In order to get around this we built 
	in a graceful restart system that would cause wikitopdf.jar to exit after a time 
	based on <time-limit> and then start up shortly after using your OS task manager 
	system.
	This next section will explain how to set up the restart process so that 
	"wikitopdf.jar" will run in a reasonable amount of time.
	**************
	NOTE: This process should only be completed AFTER the database has finished building
	**************
--------------------------------------------------------------------------------------
Using Launch Agent on Macs for Single Threaded Version

    Pulled from here: http://www.thesafemac.com/scheduling-recurring-tasks/

	If you are running the project on a Mac, you'll use a plist and a LaunchAgent. 
    To start, save a text file with this content:
        #!/bin/bash
        cd /Users/wiki/repos/printwikipedia/dist
        java -jar -Xmx1900m "wikitopdf.jar" pdf
    as:
        graceful-restart-mac.sh
    This file can be saved anywhere; we put it in printwikipedia/meta-and-documentation.
    Note that you may need to update the path on line 2.
    
    Next, type the following into Terminal, changing the path to point to wherever you
    saved graceful-restart-mac.sh:
        chmod a+x /Users/wiki/repos/printwikipedia/meta-and-documentation/graceful-restart-mac.sh
    
    Next,save this text:
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN"
        "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
        <plist version="1.0">
        <dict>
        <key>Label</key>
        <string>net.reedcorner.test</string>
        <key>ProgramArguments</key>
        <array>
          <string>/Users/wiki/repos/printwikipedia/meta-and-documentation/graceful-restart-mac.sh</string>
        </array>
        <key>StartInterval</key>
        <integer>3600</integer>
        </dict>
        </plist>
    to anywhere as: 
        net.reedcorner.test.plist
    Note that the string in the array needs to be the path to graceful-restart-mac.sh
     
    Next, choose Go to Folder from the Finder’s Go menu and enter:
        ~/Library/LaunchAgents
    If this directory does not exist, create it.
    Move the net.reedcorner.test.plist file that you created earlier into that folder.
    
    Next, type the following into the Terminal:
        launchctl load -w ~/Library/LaunchAgents/net.reedcorner.test.plist
    You can unload this file with:
        launchctl unload -w ~/Library/LaunchAgents/net.reedcorner.test.plist
--------------------------------------------------------------------------------------
Using Task Scheduler on PCs for Single Threaded Version
	If you are running the project on a PC you will want to use the program "Task 
	Manager".
	Included in the GitHub repository under the folder meta-and-documentation, there is 
	a file called "graceful-restart-single.bat". Move this file into the directory you 
	are running "wikitopdf.jar" from. 
	Now open "Task Scheduler" (click the windows icon in the bottom left of the screen 
	and type "Task Scheduler" to find it.
	In the Task Scheduler window on the right side click the "Create Task..." option
	Give your Task a name, then click on the triggers tab in the top of the Create Task 
	window.
	Click the "New..." button in the bottom of the Triggers menu 
	In the "New Trigger" window select the "One time" option on the top left, and pick 
	a Start time that is a few minutes a head of the current time.
	Under "Advanced settings" check off the "Repeat task every:" box and set the scroll 
	down option to 30 minutes if you are using 1620 as <time-limit>. If you are using a 
	different <time-limit> adjust accordingly. 
	Next click "OK"
	Now you are back in the "Create Task" window, at the top click the tab labelled 
	"Actions"
	In the "Actions" tab click "New..." at the bottom of the window.
	In the "New Action" window that opens blick the "Browse..." button the the right.
	Navigate to where you stored "graveful-restart-single.bat", and select it. 
	Next Click "Ok" to exit from the "New Action" window, then "Ok" to exit from the 
	"Create Task" window.
	To check that your task was correctly created click on the "Task Scheduler Library" 
	option on the right side of the "Task Scheduler" window. 
	In the middle of the screen you should see a list of tasks that your computer will 
	preform. As you scroll through the list you should see your newly created task. 
--------------------------------------------------------------------------------------
TO DO : Add documentation to explain how to set up graceful restart on other OS's and 
using multi-threading if implemented later.
**************************************************************************************
Congradulations printwikipedia is ready to start running on your machine. If 
everything went properly it should begin running at the time you selected in the 
graceful restart process. 
You can check that it is running because a terminal window/command prompt will open 
and display a message such as:
" C:\Users\FMCollab\Documents\GitHub\printwikipedia\meta-and-documentation>cd c:\
Users\FMCollab\Documents\PrintWiki

c:\Users\FMCollab\Documents\PrintWiki>java -jar wikitopdf.jar  1>fulloutput.txt"
and soon you should see pdfs being created in the "output" folder.
The whole process will take quite some time depending on the speed of the machine you 
are running on. You can expect atleast 2 days running time to complete the entire 
process.  
