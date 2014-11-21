#!/usr/bin/env python
# -*- coding: utf-8 -*-
from selenium import webdriver 
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.keys import Keys
from PIL import Image
from pyPdf import PdfFileWriter, PdfFileReader
from reportlab.pdfgen import canvas
from StringIO import StringIO
from selenium.common.exceptions import StaleElementReferenceException
from selenium.common.exceptions import NoSuchElementException
import selenium
import os
import urllib
import time
from time import sleep
import re
import json
execfile('pass.py')

global pwd
global outFolder
global inFolder
global browser
global cFolder
pwd = os.path.abspath(os.getcwd())
outFolder= pwd+"/out"
inFolder= pwd+"/in"
cFolder = pwd+"/covers"
#open file and read what last char is. if 0 not finished so find last entry in line and then use that one
#other chars mean different things but for right now we'll do this

def immigration(volumeNum,inputFile,title,lulu_id,browser):#move file from one folder to another. give next file over. This is done so there isn't such a long read for the 6k file run through.
	print "get luluid to be put into json string and added to file."
	browser.quit()
	print "make string and append to json1.txt"
	json_s = "{'lulu_id':"+str(lulu_id)+",'volume':"+str(volumeNum)+",'name':\""+str(title)+"\"},"
	print json_s
	json_f = open("json1.txt","a")
	json_f.write(json_s)
	json_f.close()
	print "moving volume "+ volumeNum+" to clear out folder and make listing and search faster."
	for i in os.listdir(inFolder):
		if inputFile == i:
			#move that file to out and then find the next one using volumeNum
			os.rename(inFolder+"/"+inputFile, outFolder+"/"+inputFile)
			break
	nextCheck=int(volumeNum)+1#convert to int and add one to find next volume
	#make list and then use .sort() to get through all of them save it elsewhere. and keep pulling the last one out.
	for i in os.listdir(inFolder):#find the next inputFile
		if i == '.DS_Store':
			print "ignore .ds_store"
			continue
		splitFile = i.split('&&&')
		print splitFile
		nextDest = int(splitFile[0])#also change that to an int to compare with nextcheck
		print nextDest
		print nextCheck
		if nextCheck == nextDest:
			print "found it"
			print i
			travelAgent(i)
			break

def elemWait(waitTime,waitElement,browser): #this is a certain kind of wait (upload buttons and loading screens)
	try:
		WebDriverWait(browser, waitTime).until(EC.presence_of_element_located((By.XPATH, waitElement)))
		return True
	except Exception:
		return False
def execution(browser,waitElement,waitTime,ex_type):#called whenever interacting with an element
	if ex_type=="click":
		if elemWait(waitTime,waitElement,browser) == True:
			return browser.find_element_by_xpath(waitElement).click()
		else:
			print "element @ " +waitElement+ " could not be found."
	elif ex_type=="text":
		if elemWait(waitTime,waitElement,browser) == True:
			return browser.find_element_by_xpath(waitElement)
		else:
			print "element @ " +waitElement+ " could not be found."
	elif ex_type=="find":
		if elemWait(waitTime,waitElement,browser)==True:
			return True
		else:
			return False
	else:
		print "something went wrong here"
		return False
def findNext(browser,numOfPages,inc_type):
	x = 1
	while x < 500:
		isNext = "//*[@id='pageLinks']/a["+str(x)+"]"
		r_pglink = execution(browser,isNext,3,"text")
		pglink = r_pglink.text
		print pglink + " this is pglink"
		if pglink == inc_type:
			print isNext 
			print "\ni found it!!!\n"
			return isNext
		else: 
			x+=1

def iterateFiles(volumeNum, browser, encoding="utf-8"):#go through fileslist to upload pdf doc. 
	print "about to go through files"
	print "get in iframe"
	browser.switch_to_frame(browser.find_element_by_tag_name("iframe"))
	sleep(2)
	numXpath = "//*[@id='pageCount']"
	submitButton = "/html/body/div[1]/div[1]/div/table/tbody/tr/td[2]/div/div[1]/table/tbody/tr/td[1]/input"
	r_numOfPages = execution(browser,numXpath,10,"text")
	numOfPages = r_numOfPages.text
	print "there are a total of " + numOfPages + " pages."
	numOfPages = int(numOfPages)
	x=0
	i=0
	exitLoop = False
	firstTable = "//*[@id='pageLinks']/a[1]"
	if numOfPages !=1:
		xlast = findNext(browser,numOfPages,"<<")
		execution(browser,xlast,10,"click")#go to the last page becasue freshly uploaded files are there first usually.
	#/html/body/div/div/table/tbody/tr/td[2]/div/div[3]/div/form/div/div/div[2]/table/tbody/tr[2]/td[2]/span
	while x < numOfPages and exitLoop is False:
		if numOfPages != 1:
			xNext = findNext(browser,numOfPages,">")#increment backwards
		sleep(1)
		num_rows = browser.execute_script("return document.getElementById('fileListBody').getElementsByTagName('tr').length")
		num_rows = int(num_rows)
		while i < num_rows+1:
			if i == 0:
				xFile = "/html/body/div/div/table/tbody/tr/td[2]/div/div[3]/div/form/div/div/div[2]/table/tbody/tr/td[2]/span"
			else:
				xFile = "/html/body/div[1]/div[1]/div/table/tbody/tr/td[2]/div/div[3]/div[1]/form/div/div/div[2]/table/tbody/tr["+str(i)+"]/td[2]/span"
				r_isFile = execution(browser,xFile,30,"text")
				try:
					isFile = r_isFile.text
				except StaleElementReferenceException:
					print "have to make i 0 because blah blah dom blah"
					i=0#try againbecause it's probably there u no.
				print str(i) + " " + isFile
# 				# isFile = isFile.decode('utf-8', "ignore")
# 				print str(type(inputFile))
# 				inputFile = inputFile.decode('utf-8', 'ignore')
# 				print "input "+str(type(inputFile)) + " isfile " + str(type(isFile))

				# isFile = isFile.decode('utf-8')
				if str(volumeNum)==isFile[:4]:
					print "~found your file~"
					exitLoop = True
					checkBox = xFile[:-8] + "[1]/input"
					execution(browser,checkBox,3,"click")
					execution(browser,submitButton,400,"click")#click submit
					sleep(1)
					break
			i+=1
		x+=1
		i=0
		if x > numOfPages and exitLoop == False:
			x=0
			print "i have to go back to the first page and cycle through"
			try:
				execution(browser,firstTable,400,"click")
			except StaleElementReferenceException:
				print "goddammit it's not attached to dom"
				i=0
				x=0
				continue
			continue
		if x<numOfPages and exitLoop == False:
			try:
				print "gonna click xnext"
				execution(browser,xNext,300,"click")
				sleep(3)
				continue
			except StaleElementReferenceException:
				print "goddammit it's not attached to dom"
				i=0
				x=0
				continue
	#this is the checkbox which is just one td over so we chop off the end of the xfile path	
	
	browser.switch_to_default_content()#get out of iframe
	# WebDriverWait(browser, 3500).until(EC.presence_of_element_located((By.XPATH, "//*[@id='fNext' and not (@disabled)]")))
	execution(browser,"//*[@id='fNext' and not (@disabled)]",350,"click")



def existingText(tElement,changeTo):#this function deletes textarea or text input fields. For whatever reason .clear() was not working
	#tElement==the text box.
	#changeTo is the text you wish to input
	for n in range(0,20):
		tElement.send_keys("", Keys.ARROW_RIGHT)
	for n in range(0,20):
		tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys(changeTo)
	
	
def applyBarcode(browser,volumeNum):
	bcImage = browser.find_element_by_xpath("/html/body/div/div[3]/div[2]/div[2]/div/div[2]/div[4]/a/img")
	location = bcImage.location
	size = bcImage.size
	print "taking screenshot"
	browser.save_screenshot('barcode.png')
	print "cropping screen screenshot"
	im = Image.open('barcode.png')
	left = location['x']
	top = location['y']
	right = location['x'] + size['width']
	bottom = location['y'] + size['height']
	im = im.crop((left, top, right, bottom)) # defines crop points
	im.save('barcode.png') # saves new cropped image
	imgWidth = (im.size[0])*.4
	imgHeight = (im.size[1])*.4
	
	# Using ReportLab to insert image into PDF
	imgTemp = StringIO()
	imgDoc = canvas.Canvas(imgTemp)

	# Draw image on Canvas and save PDF in buffer
	imgPath = pwd+"/barcode.png"
	imgDoc.drawImage(imgPath, 303, 115, imgWidth, imgHeight)    ## at (399,760) with size 160x160
	imgDoc.save()

	# Use PyPDF to merge the image-PDF into the template
	page = PdfFileReader(file(cFolder+"/volume&&&"+volumeNum+".pdf","rb")).getPage(0)
	overlay = PdfFileReader(StringIO(imgTemp.getvalue())).getPage(0)
	page.mergePage(overlay)

	#Save the result
	output = PdfFileWriter()
	output.addPage(page)
	output.write(file(cFolder+"/bcVolume&&&"+volumeNum+".pdf","w"))
	print "reworked img success w/PIL :)"
	
	
def luluCruise(inputFile,volumeNum,title):
	print "opening new browser to automate upload of: " + title + " on Firefox"
	browser = webdriver.Firefox()
	browser.set_window_position(900,0)
	browser.set_window_size(480,320)
	browser.get("http://www.lulu.com/author/wizard/index.php?fWizard=hardcover")#change to author page
	#use firefox and go to the login page
	uid = "//*[@id='loginEmail']" #id/name for logging in
	pw = "//*[@id='loginPassword']" #password area
	logbutt = "//*[@id='loginSubmit']" #submit button
	#send keys and click login to get to the home page.
	r_uid = execution(browser,uid,15,"text")
	r_uid.send_keys(lulu_email)
	r_pw = execution(browser,pw,15,"text")
	r_pw.send_keys(lulu_pass)
	execution(browser,logbutt,25,"click") #submit by clicking
	print "successfully logged in, now get to creating the book"
	print "setting book options"
	serviceType = "//*[@id='productline_3']"
	bookSize = "//*[@id='preset_1037_73']"
	binding = "//*[@id='binding_4']"
	pgCount = "//*[@id='pagecount']"
	cont2 = "//*[@id='fNext']"
	myPage = "700"
	execution(browser,serviceType,20,"click")
	execution(browser,serviceType,20,"click")
	
	# browser.find_element_by_xpath("/html/body/div/div[3]/div[2]/div[2]/div/div/div/div/a[3]/img").click()
	# browser.find_element_by_xpath("/html/body/div/div[3]/div[2]/div[2]/div/div/div/div/a[3]/span").click()
	execution(browser,bookSize,20,"click")
	existingText(execution(browser,pgCount,10,"text"),myPage)#clear this thing and put in 700
	execution(browser,binding,10,"click")
	execution(browser,cont2,10,"click")
	print "title and author page"
	bookTitle = "//*[@id='title']"#get and put title
	fname = "//*[@id='firstName']"
	lname = "//*[@id='lastName']"
	existingText(execution(browser,bookTitle,10,"text"),unicode(title, 'utf-8'))
	existingText(execution(browser,fname,10,"text"),'Michael')
	existingText(execution(browser,lname,10,"text"),'Mandiberg')
	cont1 = browser.find_element_by_xpath("//*[@id='fNext']")
	cont1.click()
	print "get to isbn page"
	getISBN = browser.find_element_by_xpath("//*[@id='fNext']")#should be set to default by browser on isbn page to get a new one
	getISBN.click()
	print "dl pdf on this page" 
	applyBarcode(browser,volumeNum)
	
	print "barcode applied to cover pdf."

	cont3 = browser.find_element_by_xpath("//*[@id='fNext']")
	cont3.click()

	print "tada! now time for uploading."
	myFiles = "//*[@id='ui-id-2']"
	execution(browser,myFiles,10,"click")#access the other part here with the list of files that you should have FTP'd in
	iterateFiles(volumeNum, browser)
	print "file selected and found soundly"
	# try:
	# 	browser.find_element_by_xpath("//*[@id='emptyListMessage']")
	# except NoSuchElementException:
	# 	print " didn't work so go iterate again."
	# 	iterateFiles(volumeNum,browser)
	# browser.find_elements_by_class_name('documentTitle nodrag').text
	print "wait for animation and unecessary cover upload page to load..."
	execution(browser,cont2,350,"click")
	print "go to next"
	# elemWait(120,"//*[@id='fNext']",browser)

	print "want to use the old one-piece cover page'"
	# browser.find_element_by_xpath("/html/body/div[1]/div[5]/div/div[2]/a[2]").click()
	execution(browser,"/html/body/div[1]/div[5]/div/div[2]/a[2]",350, "click")
	# execution(browser,"//*[@id='OPCbtn']",350,"click")
	# elemWait(120,"//*[@id='OPCbtn']",browser) #weird loading page
	# coverUpload = browser.find_element_by_xpath("//*[@id='fOnePieceCoverFile']")#upload cover
	print "uploading cover"
	coverUpload = "//*[@id='fOnePieceCoverFile']"
	r_coverUpload = execution(browser,coverUpload,350,"text")
	r_coverUpload.send_keys(cFolder +"/"+"bcVolume&&&"+volumeNum+".pdf")
	browser.find_element_by_xpath("//*[@id='fMegaUpload']").click()
	print "wait for upload to complete..."
	execution(browser,"//*[@id='fNext' and not (@disabled)]",350,"click")
	# elemWait(330,"//*[@id='fNext']",browser)

	catSelect=browser.find_element_by_xpath("//*[@id='category']")
	catOptions = catSelect.find_elements_by_tag_name("option")
	for option in catOptions:
		if option.text=="Reference":
			print "found reference option in select"
			option.click()
			break
	print "keywords"
	keywords = browser.find_element_by_xpath("//*[@id='keywords']")
	keywords.send_keys("poetry, reference, wiki, mandiberg")
	description = browser.find_element_by_xpath("//*[@id='descr']")
	description.send_keys(unicode("Wikipedia Table of Contents is a set of 84 books that list each article in the English Wikipedia,starting with \"!\" and ending with the symbol \"Ͽ,\" with 10,785,779 titles in between. The Wikipedia Table of Contents is accompanied by Print Wikipedia, a full set of 6,228 volumes that contain all of the entries in the encyclopedia. \r\rPrint Wikipedia is a both a utilitarian visualization of one of the most extensive accumulations of human knowledge, as well as a poetic gesture towards the futility of the scale of big data: once a volume is printed it is already out of date.\r\rBuilt on possibly the largest appropriation ever made, it is also a work of absurdist poetry that draws attention to the sheer size of the book's content and the improbability of ever concretely rendering Wikipedia as a material object in tangible form.\r\rPrint Wikipedia is designed, compiled and edited through software authored by Michael Mandiberg. The code is available here: https://github.com/mandiberg/printwikipedia", 'utf-8'))
	copyrightInfo = browser.find_element_by_xpath("//*[@id='copyright']")
	copyrightInfo.send_keys("Michael Mandiberg")
	#i think this should be for mandiberg?

	lisenceSelect = browser.find_element_by_xpath("//*[@id='license']")
	liscOptions = lisenceSelect.find_elements_by_tag_name("option")
	for option in liscOptions:
		if option.text=="Creative Commons Attribution-ShareAlike 2.0":
			option.click()
			break
	editionNum = browser.find_element_by_xpath("//*[@id='edition']")
	editionNum.send_keys("01")
	publisher = browser.find_element_by_xpath("//*[@id='publisher']")
	publisher.send_keys("bad boy records")
	browser.find_element_by_xpath("//*[@id='fNext']").click()
	print "set your price page"
	# setPrice = "//*[@id='userPrice_11']"
	# myPrice="32.97"
	# r_setPrice = execution(browser,setPrice,350,"text")
	# existingText(r_setPrice,myPrice)#clear and input new price.
	browser.find_element_by_xpath("//*[@id='fNext']").click()
	print "almost there. just lemme review the order here"
	browser.find_element_by_xpath("//*[@id='fNext']").click()
	print "book " +title+ " pushed to lulu okay."
	x_lulu_id = "//*[@id='projectInformationSection']/div[2]/table/tbody/tr[1]/td"
	lulu_id = execution(browser,x_lulu_id,20,"text")
	lulu_id = lulu_id.text
	browser.find_element_by_xpath("//*[@id='fNext']").click()
	print "begin next book at vol #"+volumeNum
	immigration(volumeNum, inputFile, title, lulu_id, browser)

def travelAgent(inputFile):
	print "splitting strings, encoding for unicode to make firefox happy. sending off file: " + inputFile
	splitInput = inputFile.split('&&&')#split on the dash symbol. to make a nice buncha strings
	volumeNum = splitInput[0]#this is the volume number we are currently on. 
	title = splitInput[1]+" --- "+splitInput[2]
	inputFile=unicode(inputFile,'utf-8')
	luluCruise(inputFile,volumeNum,title)

travelAgent("0001&&&!&&&1976 Benson & Hedges Championships&&&.pdf")
# browser = webdriver.Firefox()
# immigration(0007,"0007&&&Aminabad, Qorveh&&&Arame, Maranhão&&&.pdf","Aminabad, Qorveh --- Arame, Maranhão",321,browser)