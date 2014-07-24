#!/usr/bin/env python
# -*- coding: utf-8 -*-
import selenium
from selenium import webdriver 
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains
from PIL import Image
from pyPdf import PdfFileWriter, PdfFileReader
from reportlab.pdfgen import canvas

import os
import urllib
import time
import re
from StringIO import StringIO



global pwd
global outFolder
global inFolder
global output_cp #output cover pdf
global browser

pwd = os.path.abspath(os.getcwd())
outFolder= pwd+"/out"
inFolder= "/Users/wiki/repos/printwikipedia/dist/temp"
print pwd
print inFolder + " this is infolder" 
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
		splitFile = i.split('&&&')
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
	tElement.send_keys("", Keys.ARROW_RIGHT)
	tElement.send_keys("", Keys.ARROW_RIGHT)
	tElement.send_keys("", Keys.ARROW_RIGHT)
	tElement.send_keys("", Keys.ARROW_RIGHT)
	tElement.send_keys("", Keys.ARROW_RIGHT)
	tElement.send_keys("", Keys.ARROW_RIGHT)
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
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys("", Keys.BACK_SPACE)
	tElement.send_keys(changeTo)

def placeBarcode(volumeNum):
	print "begin placing barcode on cover pdf"
	#for adding the barcode to the pdf cover.
	# Using ReportLab to insert image into PDF
	imgTemp = StringIO()
	imgDoc = canvas.Canvas(imgTemp)

	# Draw image on Canvas and save PDF in buffer
	imgPath = "/Users/wiki/repos/printwikipedia/curl/barcode.png"
	imgDoc.drawImage(imgPath, 175, 80, 190, 80)    ## at (175,80) with size 190x80
	imgDoc.save()
	volumeNum = str(volumeNum)
	input_cp = "/Users/wiki/repos/printwikipedia/dist/covers/volume"+volumeNum+".pdf"
	output_cp = "/Users/wiki/repos/printwikipedia/dist/covers/bar_volume"+volumeNum+".pdf"
	
	# Use PyPDF to merge the image-PDF into the template
	page = PdfFileReader(file(input_cp,"rb")).getPage(0)
	overlay = PdfFileReader(StringIO(imgTemp.getvalue())).getPage(0)
	page.mergePage(overlay)
	print "merging..."

	#Save the result
	output = PdfFileWriter()
	output.addPage(page)
	output.write(file(output_cp,"w"))
	print "saving and returning output file location for the new cover"
	return output_cp

def luluCruise(inputFile,volumeNum,title):
	print "begin program:"
	print "opening browser for a cool lulu cruise"

	print title
	browser = webdriver.Firefox()
	#browser = webdriver.Chrome(pwd+"/chromedriver")
	browser.get('https://www.lulu.com/account/sign-in#login')
	#use firefox and go to the login page
	uid = browser.find_element_by_xpath("//*[@id='loginEmail']") #id/name for logging in
	pw = browser.find_element_by_xpath("//*[@id='loginPassword']") #password area
	logbutt = browser.find_element_by_xpath("//*[@id='loginSubmit']") #submit button
	#send keys and click login to get to the home page.
	uid.send_keys('bacn@mandiberg.com')
	pw.send_keys('20DollarBi11')
	logbutt.click() #submit by clicking (return doesn't work for some reason)
	browser.get("http://www.lulu.com/author/wizard/index.php?fWizard=hardcover")#change to author page
	print "options"
	
	serviceType = browser.find_element_by_xpath("//*[@id='productline_3']")
	hover = ActionChains(browser).move_to_element(serviceType)
	hover.perform()
	WebDriverWait(browser, 10000)
	bookSize = browser.find_element_by_xpath("//*[@id='preset_1037_73']")
	print "one"
	hover.perform()
	WebDriverWait(browser, 10000)
	bookSize.click()
	browser.find_element_by_xpath("/html/body/div/div[3]/div[2]/div[2]/div/div/div/div/a[3]/img").click()
	browser.find_element_by_xpath("/html/body/div/div[3]/div[2]/div[2]/div/div/div/div/a[3]/span").click()
	hover.perform()
	WebDriverWait(browser, 10000)
	print "two"
	hover.perform()
	binding = browser.find_element_by_xpath("//*[@id='binding_4']")
	hover.perform()
	
	pgCount = browser.find_element_by_xpath("//*[@id='pagecount']")
	print "three"
	#bookSize = browser.find_element_by_xpath("//*[@id='trimSizeOption_2']")
	# color = browser.find_element_by_xpath("//*[@id='inkColorOption_2']")
	#paper = browser.find_element_by_xpath("//*[@id='paperCoatingOption_1']")
	cont2 = browser.find_element_by_xpath("//*[@id='fNext']")
	myPage = "700"
	print 'almost there'
	binding.click()
	existingText(pgCount,myPage)#clear this thing and put in 700
	
	cont2.click()
    
	print "book stuff now!"
	
	bookTitle = browser.find_element_by_xpath("//*[@id='title']")#get title
	existingText(bookTitle, title)
	# bookTitle.send_keys(title)#put title
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
	browser.save_screenshot('/Users/wiki/repos/printwikipedia/curl/barcode.png')
	print "cropping screen screenshot"
	im = Image.open('/Users/wiki/repos/printwikipedia/curl/barcode.png')
	left = 451
	top = 539
	right = left + 380
	bottom = top + 160
	im = im.crop((left, top, right, bottom)) # defines crop points
	im.save('/Users/wiki/repos/printwikipedia/curl/barcode.png') # saves new cropped image
	print "all done it's in barcode.png"
	
	output_cp = placeBarcode(volumeNum)

	cont3 = browser.find_element_by_xpath("//*[@id='fNext']")
	cont3.click()

	print "tada now time for uploading."
	uploadFile = inFolder +"/"+inputFile+".pdf"

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
	print "need some human input here!"
	print "please upload cover: volume"+volumeNum+".pdf"
	iterator = 0
	#for iterator in range(0,8):
	#	print('\a')
	coverUpload.send_keys(output_cp) #using output_cp
	coverUp = browser.find_element_by_xpath("//*[@id='fMegaUpload']")
	coverUp.click()
 	elemWait(550,"//*[@id='fNext']",browser)
 	print "wait for upload to complete"
	
	# browser.get("http://www.lulu.com/author/index.php")
	#browser.find_element_by_xpath("/html/body/div[2]/div[2]/div/div[2]/div[2]/div[2]/table/tbody/tr/td[4]/a").click()
	#going back to that one file again through the profile...
	#going to try file upload again.
	#coverUpload = browser.find_element_by_xpath("//*[@id='fOnePieceCoverFile']")#upload cover
	#print "uploading cover"
	#coverUpload.send_keys(pwd +"../covers/"+"volume"+volumeNum+".pdf")
	#coverUp = browser.find_element_by_xpath("//*[@id='fMegaUpload']")
	#coverUp.click()
	#print "wait for upload to complete"
	elemWait(950,"//*[@id='category']",browser)
	catSelect=browser.find_element_by_xpath("//*[@id='category']")
	catOptions = catSelect.find_elements_by_tag_name("option")
	WebDriverWait(browser, 10000)
	for option in catOptions:
		if "Reference" in option.text:
			print "found reference option in select"
			option.click()
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
	print lisenceSelect
	liscOptions = lisenceSelect.find_elements_by_tag_name("option")
	for option in liscOptions:
		print option.text
		if "Creative Commons Attribution-ShareAlike 2.0" in option.text:
			option.click()
			break
		print "no"
	editionNum = browser.find_element_by_xpath("//*[@id='edition']")
	editionNum.send_keys("01")
	publisher = browser.find_element_by_xpath("//*[@id='publisher']")
	publisher.send_keys("bad boy records")
	browser.find_element_by_xpath("//*[@id='fNext']").click()
	print "set your price page"
	setPrice = browser.find_element_by_xpath("//*[@id='userPrice']")
	myPrice="44.97"
	existingText(setPrice,myPrice)#clear and input new price.
	browser.find_element_by_xpath("//*[@id='fNext']").click()
	print "almost there. just lemme review the order here"
	print "yep ok everything looks good. i'm a computer~"
	browser.find_element_by_xpath("//*[@id='fNext']").click()
	print "book " +title+ " pushed to lulu okay."
	print "begin next book at vol #"+volumeNum
	immigration(volumeNum, inputFile, browser)

def travelAgent(inputFile):
	splitInput = inputFile.split('&&&')#split on the dash symbol. to make a nice buncha strings
	volumeNum = splitInput[1]#this is the volume number we are currently on. 
	title = splitInput[2]+" --- "+splitInput[3]
	title=unicode(title, 'utf-8')#encode that real nice for lulu
	luluCruise(inputFile,volumeNum,title)


travelAgent("tocVol&&&1&&&!&&&1986 Buenos Aires Grand Prix (tennis)&&&")