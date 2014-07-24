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
	tElement.send_keys("", Keys.ARROW_RIGHT)
	tElement.send_keys("", Keys.ARROW_RIGHT)
	tElement.send_keys("", Keys.ARROW_RIGHT)
	tElement.send_keys("", Keys.ARROW_RIGHT)
	tElement.send_keys("", Keys.ARROW_RIGHT)
	tElement.send_keys("", Keys.ARROW_RIGHT)
	tElement.send_keys("", Keys.ARROW_RIGHT)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys(changeTo)

def luluCruise(inputFile,volumeNum,title):
	print "begin program:"
	print "opening browser for a cool lulu cruise"

	print title
	browser = webdriver.Firefox()
	browser.get("http://www.lulu.com/author/wizard/index.php?fWizard=hardcover")#change to author page
	#use firefox and go to the login page
	uid = browser.find_element_by_xpath("//*[@id='loginEmail']") #id/name for logging in
	pw = browser.find_element_by_xpath("//*[@id='loginPassword']") #password area
	logbutt = browser.find_element_by_xpath("//*[@id='loginSubmit']") #submit button
	#send keys and click login to get to the home page.
	uid.send_keys(lulu_email) 
	pw.send_keys(lulu_pass)
	logbutt.click() #submit by clicking (return doesn't work for some reason)

	print "successfully logged in, now get to the book"
	print "options"
	serviceType = browser.find_element_by_xpath("//*[@id='productline_3']")
	print "im a size"
	bookSize = browser.find_element_by_xpath("//*[@id='preset_1037_73']")
	binding = browser.find_element_by_xpath("//*[@id='binding_4']")
	pgCount = browser.find_element_by_xpath("//*[@id='pagecount']")
	#bookSize = browser.find_element_by_xpath("//*[@id='trimSizeOption_2']")
	# color = browser.find_element_by_xpath("//*[@id='inkColorOption_2']")
	#paper = browser.find_element_by_xpath("//*[@id='paperCoatingOption_1']")
	cont2 = browser.find_element_by_xpath("//*[@id='fNext']")
	myPage = "700"
	ActionChains(browser).move_to_element(serviceType).perform()
	serviceType.click()
	browser.find_element_by_xpath("/html/body/div/div[3]/div[2]/div[2]/div/div/div/div/a[3]/img").click()
	browser.find_element_by_xpath("/html/body/div/div[3]/div[2]/div[2]/div/div/div/div/a[3]/span").click()
	serviceType.click()
	bookSize.click()
	existingText(pgCount,myPage)#clear this thing and put in 700
	binding.click()
	
	# color.click()
	cont2.click()
	print "book stuff now!"
	bookTitle = browser.find_element_by_xpath("//*[@id='title']")#get title
	bookTitle.send_keys(title)#put title
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
	uploadFile = inFolder +"/"+inputFile
	print uploadFile
	browser.find_element_by_xpath("//*[@id='uploadField']").send_keys(unicode(uploadFile, 'utf-8'))
	cont4 = browser.find_element_by_xpath("//*[@id='fMegaUpload']") #click this and upload
	cont4.click()
	print "wait for upload"
	uploadButt = browser.find_element_by_xpath("//*[@id='fNext']").get_attribute('class')
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
	#here for language it would be funny to say it's in javanese
	#because of java
	#right?
	#riight?
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
