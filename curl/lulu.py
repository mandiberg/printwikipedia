#!/usr/bin/env python
# -*- coding: utf-8 -*-
from selenium import webdriver 
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.keys import Keys
from PIL import Image

import selenium
import os
import urllib
import time
import re
execfile('pass.py')

global pwd
global outFolder
global inFolder
global browser
pwd = os.path.abspath(os.getcwd())
outFolder= pwd+"/out"
inFolder= pwd+"/in"
print pwd
#open file and read what last char is. if 0 not finished so find last entry in line and then use that one
#other chars mean different things but for right now we'll do this

def immigration(volumeNum,inputFile,browser):
	browser.quit()
	print "immigration office. moving volume "+ volumeNum
	for i in os.listdir(inFolder):
		if inputFile == i:
			#move that file to out and then find the next one using volumeNum
			os.rename(inFolder+"/"+inputFile, outFolder+"/"+inputFile)
			break
	nextCheck=int(volumeNum)+1#convert to int and add one to find next volume
	#make list and then use .sort() to get through all of them save it elsewhere. and keep pulling the last one out.
	for i in os.listdir(inFolder):#find the next inputFile
		splitFile = i.split('-')
		print splitFile
		nextDest = int(splitFile[1])#also change that to an int to compare with nextcheck
		print nextDest
		print nextCheck
		if nextCheck == nextDest:
			print "found it"
			print i
			travelAgent(i)
			break

def elemWait(waitTime,waitElement,browser): #this is a certain kind of wait (uploads and flash loading screens lulu likes)
	#waitTime is the amount of time it will take to load this thing
	#waitElement is the element that it is waiting for to be available after upload/pageload/whatever
	try:
		print "about to look for element"
		def find(driver):
			e = browser.find_element_by_xpath(waitElement)
			if (e.get_attribute("disabled")=='true'):
				return False
			return e
		element = WebDriverWait(browser, waitTime).until(find)
		print "still looking for element: "+waitElement+" ?"
	finally: print 'yowp'
	print "found it. moving on to the next part."
	browser.find_element_by_xpath(waitElement).click() #click next when you can

def existingText(tElement,changeTo):#this function deletes textarea or text input fields. For whatever reason .clear() was not working
	#tElement==the text box.
	#changeTo is the text you wish to input
	for n in range(0,20):
		tElement.send_keys("", Keys.ARROW_RIGHT)
	for n in range(0,20):
		tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys(changeTo)

def iterateFiles(inputFile, browser):
	print "about to go through files"
	print "get in iframe"
	browser.switch_to_frame(browser.find_element_by_tag_name("iframe"))
	numOfPages = browser.find_element_by_xpath("/html/body/div/div/table/tbody/tr/td[2]/div/div/div/div/span[2]").text
	x=0
	#row of table with title
	#/html/body/div/div/table/tbody/tr/td[2]/div/div[3]/div/form/div/div/div[2]/table/tbody/tr[2]/td[2]/span
	while x<numOfPages:
		for i in range(1,25):
			if i == 1:
				xFile = "/html/body/div/div/table/tbody/tr/td[2]/div/div[3]/div/form/div/div/div[2]/table/tbody/tr/td[2]/span"
				print "it one"
			else:
				xFile = "/html/body/div/div/table/tbody/tr/td[2]/div/div[3]/div/form/div/div/div[2]/table/tbody/tr["+str(i)+"]/td[2]/span"
			isFile = browser.find_element_by_xpath(xFile)
			print isFile
			print isFile.text
			if isFile == inputFile:
				print "i found him jon \n\n\n"
				checkBox = xFile[:-8]
				browser.find_element_by_xpath(checkBox).click()
				break
			i+=1
		
		x+=1
		xNext = "/html/body/div/div/table/tbody/tr/td[2]/div/div/div/div/div/a[8]"
		browser.find_element_by_xpath(xNext).click()
	browser.find_element_by_xpath("/html/body/div/div/table/tbody/tr/td[2]/div/div/table/tbody/tr/td/input").click()
	browser.switch_to_default_content()	

def luluCruise(inputFile,volumeNum,title):
	print "begin program:"
	print "opening browser for a cool lulu cruise"

	print title
	browser = webdriver.Firefox()
	browser.get("http://www.lulu.com/author/wizard/index.php?fWizard=hardcover")#change to author page
	#use firefox and go to the login page
	uid = browser.find_element_by_xpath("//*[@id='loginEmail']") #id/name for logging in
	pw = browser.find_element_by_xpath("//*[@id='loginPassword']") #password area
# 	logbutt = browser.find_element_by_xpath("//*[@id='loginSubmit']") #submit button
	#send keys and click login to get to the home page.
	uid.send_keys(lulu_email) 
	pw.send_keys(lulu_pass)
	elemWait(120,"//*[@id='loginSubmit']",browser)

	print "successfully logged in, now get to the book"
	print "options"
	elemWait(200,"//*[@id='productline_3']",browser)
	
	pgCount = browser.find_element_by_xpath("//*[@id='pagecount']")
	cont2 = browser.find_element_by_xpath("//*[@id='fNext']")
	myPage = "700"
	print "i click"
	browser.find_element_by_xpath("/html/body/div/div[3]/div[2]/div[2]/div/div/div/div/a[3]/img").click()	
	existingText(pgCount,myPage)#clear this thing and put in 700
	binding = browser.find_element_by_xpath("//*[@id='binding_4']")
	binding.click()

	cont2.click()
	print "book stuff now!"
	bookTitle = browser.find_element_by_xpath("//*[@id='title']")#get title
	existingText(bookTitle,title)
	cont1 = browser.find_element_by_xpath("//*[@id='fNext']")
	cont1.click()
	print "get to isbn page"
	getISBN = browser.find_element_by_xpath("//*[@id='fNext']")#should be set to default by browser on isbn page to get a new one
	getISBN.click()
	print "dl pdf on this page"
	# pdfLink = browser.find_element_by_xpath("//*[@id='BarcodeImage']").get_attribute('href')
	bcImage = browser.find_element_by_xpath("/html/body/div/div[3]/div[2]/div[2]/div/div[2]/div[4]/a/img")
	location = bcImage.location
	size = bcImage.size
	print "taking screenshot"
	browser.save_screenshot('barcode.png')
	print "croping screen screenshot"
	im = Image.open('barcode.png')
	left = location['x']
	top = location['y']
	right = location['x'] + size['width']
	bottom = location['y'] + size['height']
	im = im.crop((left, top, right, bottom)) # defines crop points
	im.save('barcode.png') # saves new cropped image
	print "all done it's in barcode.png"

	cont3 = browser.find_element_by_xpath("//*[@id='fNext']")
	cont3.click()

	print "tada now time for uploading."
	browser.find_element_by_xpath("//*[@id='ui-id-2']").click()
	#go through rows and count 25 looking for a certain title of the file given to you from the ls in the beginning.
	#if more than 25 go to next page and search there.
	iterateFiles(inputFile, browser)
	
	# uploadFile = inFolder +"/"+inputFile
# 	print uploadFile
# 	tempf = unicode(uploadFile,'utf-8')
# 	print tempf
# 	browser.find_element_by_xpath("//*[@id='uploadField']").send_keys(unicode(uploadFile, 'utf-8'))
# 	#injecting javascript to put in full path here. for some reason sending keys to that specific spot is not working.
# # 	browser.execute_script('document.forms[0][5].files[0]["mozFullPath"] ='+uploadFile+' ;')
# 	cont4 = browser.find_element_by_xpath("//*[@id='fMegaUpload']") #click this and upload
# 	cont4.click()
# 	print "wait for upload"
# 	uploadButt = browser.find_element_by_xpath("//*[@id='fNext']").get_attribute('class')
# 	print uploadButt
	#while this button is still unclickable keep waiting. but also check to see if there are errors.

	elemWait(350, "//*[@id='fNext']",browser)

	print "onto the next step which is just going to confirm but it does this stupid loading animation so you have to do another try function that just waits for a minute"
	elemWait(120,"//*[@id='fNext']",browser)

	print "want to use the old 'wizard'"
	browser.find_element_by_xpath("/html/body/div/div[5]/div/div[2]/a").click()
	elemWait(120,"//*[@id='OPCbtn']",browser) #weird loading page
	coverUpload = browser.find_element_by_xpath("//*[@id='fOnePieceCoverFile']")#upload cover
	print "uploading cover"
	coverUpload.send_keys(pwd +"/"+"newCover.jpg")
	browser.find_element_by_xpath("//*[@id='fMegaUpload']").click()
	print "wait for upload to complete"
	elemWait(330,"//*[@id='fNext']",browser)

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
	description.send_keys("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx must be 50 chars long")
	#automatically set to english
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
	elemWait(123,"//*[@id='userPrice_11']",browser)
	setPrice = browser.find_element_by_xpath("//*[@id='userPrice_11']")
	myPrice="32.97"
	existingText(setPrice,myPrice)#clear and input new price.
	browser.find_element_by_xpath("//*[@id='fNext']").click()
	print "almost there. just lemme review the order here"
	print "yep ok everything looks good. i'm a computer~"
	browser.find_element_by_xpath("//*[@id='fNext']").click()
	print "book " +title+ " pushed to lulu okay."
	print "begin next book at vol #"+volumeNum
	immigration(volumeNum, inputFile, browser)

def travelAgent(inputFile):
	splitInput = inputFile.split('-')#split on the dash symbol. to make a nice buncha strings
	volumeNum = splitInput[1]#this is the volume number we are currently on. 
	title = splitInput[2]+" --- "+splitInput[3]
	title=unicode(title, 'utf-8')#encode that real nice for lulu
	luluCruise(inputFile,volumeNum,title)


travelAgent("Vol-00002-bb-™_symbol-10215110-4374729.pdf")
