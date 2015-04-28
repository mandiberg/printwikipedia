#!/usr/bin/env python
# -*- coding: utf-8 -*-
from selenium import webdriver 
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.keys import Keys
from PIL import Image, ImageDraw, ImageFont
from pyPdf import PdfFileWriter, PdfFileReader
import PyPDF2 as pypdf
import ftplib
from reportlab.pdfgen import canvas
from StringIO import StringIO
from selenium.common.exceptions import StaleElementReferenceException
from selenium.common.exceptions import NoSuchElementException
import selenium
import os
import sys
import urllib
import time
from time import sleep
import re
import json


execfile("pass.py")
class Volume:
	#stores variables for the individual volume should be extended by browser.
	def roundDown(self,divisor=20):#returns a string of closest rounded down with 4 leading zeros if possible.
		fol = '{0:04d}'.format(int(self.num) - (int(self.num)%divisor))
		if(fol=="0000"):
			return "/0001"
		return "/"+fol

	def travelAgent(self,inputFile):
		print "splitting strings, encoding for unicode to make firefox happy. sending off file: " + inputFile
		splitInput = inputFile.split('&&&')#split on the dash symbol. to make a nice buncha strings
		self.num = splitInput[0]#this is the volume number we are currently on. it is a string!
		self.title = splitInput[1]+" --- "+splitInput[2]
		self.inputFile=unicode(inputFile,'utf-8')
		self.round_folder = self.roundDown()
	
class Browser:
	#for traversing the lulu interface
	def __init__(self, inputFile):
		self.stage = 0 #use stage to see where in the process you are so you know where to come back to.
		self.volume = Volume()
		self.makeVolume() #makevolume
		self.local = Local(self.volume)
		self.service_type = "//*[@id='productline_3']"
		self.book_size = "//*[@id='preset_1037_73']"
		self.binding = "//*[@id='binding_4']"
		self.pg_count = "//*[@id='pagecount']"
		self.uid = "//*[@id='loginEmail']" #id/name for logging in
		self.pw = "//*[@id='loginPassword']" #password area
		self.logbutt = "//*[@id='loginSubmit']" #submit button
		self.my_page = "700"
		self.next = "//*[@id='fNext']"
		self.book_title = "//*[@id='title']"#get and put title
		self.fname = "//*[@id='firstName']"
		self.lname = "//*[@id='lastName']"
		self.project_details = "/html/body/div[1]/div[1]/div/div/div[2]"
		self.project_id = "/html/body/div[1]/div[1]/div/div/div[2]/div[2]/table/tbody/tr[1]/td[1]"
		self.myFiles = "//*[@id='ui-id-2']"
		self.cover_upload = "//*[@id='fOnePieceCoverFile']"
		self.x_lulu_isbn = "//*[@id='projectInformationSection']/div[2]/table/tbody/tr[1]/td"
		self.x_lulu_id = "/html/body/div[1]/div[3]/div[3]/div[2]/div/div[2]/form/div[1]/div[2]/div/div[2]/div[2]/table/tbody/tr[1]/td"
		self.x_sell = "/html/body/div[1]/div[3]/div[2]/div[2]/div/div[1]/div/h3/a"
		self.sku_x = "/html/body/div[1]/div[2]/div[2]/div[1]/div[2]/div[2]/form/input[1]"
		self.bc_image = "/html/body/div/div[3]/div[2]/div[2]/div/div[2]/div[4]/a/img"
		self.folders_option = "//*[@id='labelFilter']"
		self.num_Xpath = "//*[@id='pageCount']"
		self.submit_button = "/html/body/div[1]/div[1]/div/table/tbody/tr/td[2]/div/div[1]/table/tbody/tr/td[1]/input"
		self.first_table = "//*[@id='pageLinks']/a[1]"
		self.one_file_cover = "/html/body/div[1]/div[5]/div/div[2]/a[2]"
		self.c_up_button = "//*[@id='fMegaUpload']"
		self.keywords = "//*[@id='keywords']"
		self.description = "//*[@id='descr']"
		self.copyright = "//*[@id='copyright']"
		self.license = "//*[@id='license']"
		self.edition = "//*[@id='edition']"
		self.publisher = "//*[@id='publisher']"
		self.setPrice = "//*[@id='userPrice']"
		self.x_lulu_isbn = "//*[@id='projectInformationSection']/div[2]/table/tbody/tr[1]/td"
		self.x_sell = "/html/body/div[1]/div[3]/div[2]/div[2]/div/div[1]/div/h3/a"
		self.sku_x = "/html/body/div[1]/div[2]/div[2]/div[1]/div[2]/div[2]/form/input[1]"

		self.driver = webdriver.Firefox()
		self.driver.set_window_position(0,0)
		self.driver.set_window_size(880,620)
		self.luluCruise() #start
	
	def makeVolume(self):
		self.volume.travelAgent(input_file)

	def execution(self,waitElement,waitTime,ex_type):#called whenever interacting with an element
		if ex_type=="click":
			if self.elemWait(waitTime,waitElement,ex_type) == True:
				if self.next == waitElement:
					self.stage+=1
				return self.driver.find_element_by_xpath(waitElement).click()
			else:
				print "element @ " +waitElement+ " could not be found."
		elif ex_type=="text":
			if self.elemWait(waitTime,waitElement,ex_type) == True:
				return self.driver.find_element_by_xpath(waitElement)
			else:
				print "element @ " +waitElement+ " could not be found."
		elif ex_type=="find":
			if self.elemWait(waitTime,waitElement,ex_type)==True:
				return True
			else:
				return False
		else:
			print "something went wrong here"
			return False

	def elemWait(self,waitTime,waitElement,ex_type): #this is a certain kind of wait (upload buttons and loading screens)
		try:
			WebDriverWait(self.driver, waitTime).until(EC.presence_of_element_located((By.XPATH, waitElement)))
			return True
		except:
			e = sys.exc_info()[0]
			print str(e)
			return False

	def existingText(self,tElement,changeTo):#this function deletes textarea or text input fields. For whatever reason .clear() was not working
		#tElement==the text box.
		#changeTo is the text you wish to input
		for n in range(0,20):
			tElement.send_keys("", Keys.ARROW_RIGHT)
		for n in range(0,20):
			tElement.send_keys("", Keys.BACK_SPACE)
		tElement.send_keys(changeTo)
	def applyBarcode(self):
		# xbc = self.execution(self.bc_image,20,"")
		# location = xbc.location
		bcImage = self.driver.find_element_by_xpath("/html/body/div/div[3]/div[2]/div[2]/div/div[2]/div[4]/a/img")
		location = bcImage.location
		size = bcImage.size
		print "taking screenshot"
		self.driver.save_screenshot('barcode.png')
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
		print "draw up new page and place barcode on there"
		imgPath = self.local.pwd+"/barcode.png"
		imgDoc.drawImage(imgPath, 303, 115, imgWidth, imgHeight)    ## at (303,115) with size 160x160
		imgDoc.save()

		# Use PyPDF to merge the image-PDF into the template
		page = PdfFileReader(file(self.local.cFolder+"/volume&&&"+self.volume.num+".pdf","rb")).getPage(0)
		overlay = PdfFileReader(StringIO(imgTemp.getvalue())).getPage(0)
		page.mergePage(overlay)
		output = PdfFileWriter()
		output.addPage(page)
		output.write(file(self.local.cFolder+"/bcVolume&&&"+self.volume.num+".pdf","w"))
		print "cover is up"

		#MAKE FUNCTION HERE
		im = Image.open('barcode.png')
		im.save('barcode.png') # saves new cropped image
		imgWidth = (im.size[0])*.4
		imgHeight = (im.size[1])*.4

		# Using ReportLab to insert image into PDF
		imgTemp = StringIO()
		imgDoc = canvas.Canvas(imgTemp)

		# Draw image on Canvas and save PDF in buffer
		print "seek to page and place barcode before main"
		imgPath = "barcode.png"
		imgDoc.drawImage(imgPath, 20, 40, imgWidth, imgHeight)    ## at (303,115) with size 160x160
		imgDoc.save()

		# Use PyPDF to merge the image-PDF into the template
		original = pypdf.PdfFileReader(file(self.local.inFolder+self.volume.round_folder+"/pre"+self.volume.num+".pdf","rb"))
		page = original.getPage(1)
		overlay = pypdf.PdfFileReader(StringIO(imgTemp.getvalue())).getPage(0)
		page.mergePage(overlay)
		# add all pages to a writer
		writer = pypdf.PdfFileWriter()
		for i in range(original.getNumPages()):
		    page = original.getPage(i)
		    writer.addPage(page)
		with open(self.local.inFolder+self.volume.round_folder+"/mod"+self.volume.num+".pdf", "wb") as outFile:
			writer.write(outFile)
	def findNext(self,numOfPages,inc_type):
		x = 1
		while x < 500:
			isNext = "//*[@id='pageLinks']/a["+str(x)+"]"
			r_pglink = self.execution(isNext,3,"text")
			pglink = r_pglink.text
			print pglink + " this is pglink"
			if pglink == inc_type:
				print isNext 
				print "\ni found it!!!\n"
				return isNext
			else: 
				x+=1
	def iterateFiles(self,mod,encoding="utf-8"):
		print "about to go through files"
		print "get in iframe"
		self.driver.switch_to_frame(self.driver.find_element_by_tag_name("iframe"))
		self.execution(self.folders_option,200,"click")#clickin the folders
		sleep(2)
		n = 1
		while(1):#this is dangerous and should probably be changed....
			t_folder = self.execution("/html/body/div[1]/div[1]/div/table/tbody/tr/td[1]/div/div[2]/ul[2]/li["+str(n)+"]/a",10,"text")
			print t_folder.text + " this is text of folder! n is at " + str(n) + " folder_name" + self.volume.round_folder
			if(self.volume.round_folder.strip("/") == t_folder.text):
				self.execution("/html/body/div[1]/div[1]/div/table/tbody/tr/td[1]/div/div[2]/ul[2]/li["+str(n)+"]/a",30,"click")
				sleep(2)
				break
			else:
				n+=1
		r_numOfPages = self.execution(self.num_Xpath,50,"text")
		print r_numOfPages
		numOfPages = r_numOfPages.text
		print "there are a total of " + numOfPages + " pages."
		numOfPages = int(numOfPages)
		x=0
		i=0
		exitLoop = False
		if numOfPages !=1:
			xlast = self.findNext(numOfPages,"<<")
			self.execution(xlast,10,"click")#go to the last page becasue freshly uploaded files are there first usually.
		#/html/body/div/div/table/tbody/tr/td[2]/div/div[3]/div/form/div/div/div[2]/table/tbody/tr[2]/td[2]/span
		while x < numOfPages and exitLoop is False:
			if numOfPages != 1:
				xNext = self.findNext(numOfPages,">")#increment backwards
			sleep(1)
			num_rows = self.driver.execute_script("return document.getElementById('fileListBody').getElementsByTagName('tr').length")
			num_rows = int(num_rows)
			while i < num_rows+1:
				if i == 0:
					xFile = "/html/body/div/div/table/tbody/tr/td[2]/div/div[3]/div/form/div/div/div[2]/table/tbody/tr/td[2]/span"
				else:
					xFile = "/html/body/div[1]/div[1]/div/table/tbody/tr/td[2]/div/div[3]/div[1]/form/div/div/div[2]/table/tbody/tr["+str(i)+"]/td[2]/span"
					r_isFile = self.execution(xFile,30,"text")
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
					if mod is True:
						if "mod"+self.volume.num==isFile[:7]:
							print "~found your file~"
							exitLoop = True
							checkBox = xFile[:-8] + "[1]/input"
							self.execution(checkBox,3,"click")
							self.execution(self.submit_button,400,"click")#click submit
							sleep(1)
							break
					else:
						if self.volume.num==isFile[:4]:
							print "~found your file~"
							exitLoop = True
							checkBox = xFile[:-8] + "[1]/input"
							self.execution(checkBox,3,"click")
							self.execution(self.submit_button,400,"click")#click submit
							sleep(1)
							break
				i+=1
			x+=1
			i=0
			if x > numOfPages and exitLoop == False:
				x=0
				print "i have to go back to the first page and cycle through"
				try:
					self.execution(self.first_table,400,"click")
				except StaleElementReferenceException:
					print "goddammit it's not attached to dom"
					i=0
					x=0
					continue
				continue
			if x<numOfPages and exitLoop == False:
				try:
					print "gonna click xnext"
					self.execution(xNext,300,"click")
					sleep(3)
					continue
				except StaleElementReferenceException:
					print "goddammit it's not attached to dom"
					i=0
					x=0
					continue
		#this is the checkbox which is just one td over so we chop off the end of the xfile path	
		
		self.driver.switch_to_default_content()#get out of iframe
		# WebDriverWait(browser, 3500).until(EC.presence_of_element_located((By.XPATH, "//*[@id='fNext' and not (@disabled)]")))
		# self.execution("//*[@id='fNext' and not (@disabled)]",350,"click")

	def luluCruise(self):
		print "opening new browser to automate upload of: " + self.volume.title + " on Firefox"
		self.driver.get("http://www.lulu.com/author/wizard/index.php?fWizard=hardcover")#change to author page
		r_uid = self.execution(self.uid,150,"text")
		r_uid.send_keys(lulu_email)
		r_pw = self.execution(self.pw,150,"text")
		r_pw.send_keys(lulu_pass)
		self.execution(self.logbutt,250,"click") #submit by clicking
		print "successfully logged in, now get to creating the book"
		print "setting book options"
		self.execution(self.service_type,20,"click")
		self.execution(self.book_size,20,"click")
		self.existingText(self.execution(self.pg_count,10,"text"),self.my_page)#clear this thing and put in 700
		self.execution(self.binding,10,"click")
		self.execution(self.next,10,"click")
		print "title and author page"
		self.existingText(self.execution(self.book_title,10,"text"),unicode(self.volume.title, 'utf-8'))
		self.existingText(self.execution(self.fname,10,"text"),'Michael')
		self.existingText(self.execution(self.lname,10,"text"),'Mandiberg')
		print "snag the fcid"
		self.driver.execute_script('document.getElementById("projectDetailsContent").style.display="block"')
		self.volume.fcid = self.execution(self.project_id,30,"text").text
		print self.volume.fcid
		self.execution(self.next,10,"click")
		print "get to isbn page"
		self.execution(self.next,10,"click")
		print "dl pdf on this page" 
		self.applyBarcode()
		print "ftp modified pre-image on up"
		self.local.ftpIt()
		self.execution(self.next,10,"click")
		self.execution(self.myFiles,30,"click")
		self.iterateFiles(True)#mod = true so attach the modded pre-file before the larger internals
		print "file selected soundly"
		sleep(2)
		self.execution(self.myFiles,30,"click")
		self.iterateFiles(False)
		print "both files selected and placed soundly, phew!"
		self.execution(self.next,350,"click")
		print "wait for animation of unecessary cover upload page to load..."
		self.execution(self.next,350,"click")
		print "go to next"
		self.execution(self.one_file_cover,350, "click")
		print "uploading cover"
		r_cover_upload = self.execution(self.cover_upload,350,"text")
		r_cover_upload.send_keys(self.local.cFolder +"/"+"bcVolume&&&"+self.volume.num+".pdf")
		self.execution(self.c_up_button,350,"click")
		print "wait for upload to complete..."
		self.execution("//*[@id='fNext' and not (@disabled)]",350,"click")
		catSelect=self.driver.find_element_by_xpath("//*[@id='category']")
		catOptions = catSelect.find_elements_by_tag_name("option")
		for option in catOptions:
			if option.text=="Reference":
				print "found reference option in select"
				option.click()
				break
		print "keywords"
		r_keywords = self.execution(self.keywords,150,"text")
		r_keywords.send_keys("poetry, reference, wiki, mandiberg")
		r_desc = self.execution(self.description,100,"text")
		r_desc.send_keys(unicode("Wikipedia Table of Contents is a set of 84 books that list each article in the English Wikipedia,starting with \"!\" and ending with the symbol \"Ͽ,\" with 10,785,779 titles in between. The Wikipedia Table of Contents is accompanied by Print Wikipedia, a full set of 6,228 volumes that contain all of the entries in the encyclopedia. \r\rPrint Wikipedia is a both a utilitarian visualization of one of the most extensive accumulations of human knowledge, as well as a poetic gesture towards the futility of the scale of big data: once a volume is printed it is already out of date.\r\rBuilt on possibly the largest appropriation ever made, it is also a work of absurdist poetry that draws attention to the sheer size of the book's content and the improbability of ever concretely rendering Wikipedia as a material object in tangible form.\r\rPrint Wikipedia is designed, compiled and edited through software authored by Michael Mandiberg. The code is available here: https://github.com/mandiberg/printwikipedia", 'utf-8'))

		r_copy = self.execution(self.copyright,30,"text")
		r_copy.send_keys("Wikipedia Contributors")
		lisc_select =self.execution(self.license,30,"text")
		liscOptions = lisc_select.find_elements_by_tag_name("option")
		for option in liscOptions:
			if option.text=="Creative Commons Attribution-ShareAlike 2.0":
				option.click()
				break
		r_edition =self.execution(self.edition,30,"text")
		r_edition.send_keys('01')
		r_publisher = self.execution(self.publisher,30,"text")
		r_publisher.send_keys("Michael Mandiberg")
		self.execution(self.next,350,"click")
		r_price = self.execution(self.setPrice,30,"text")
		self.existingText(self.execution(self.setPrice,20,"text"),'50')
		self.execution(self.next,350,"click")
		print "almost there. just lemme review the order here"
		self.execution(self.next,350,"click")
		print "book " +self.volume.title+ " pushed to lulu okay."
		self.execution(self.x_lulu_isbn,20,"text")
		lulu_isbn = execution(browser,x_lulu_isbn,20,"text")
		self.volume.isbn = lulu_isbn.text
		self.execution(self.next,350,"click")
		print "go to the sale page to grab the SKU code."
		self.execution(self.x_sell,20,"click")
		self.volume.sku = self.execution(self.sku_x,30,"text").get_attribute('value')
		print sku +  " sku"
		#^^straight up sku code in a hidden input
		# x_buy_url = "/html/body/div/div/div/table/tbody/tr/td[1]/div/form/input[5]"
		#^^straight up url to the thing
		print "begin next book at vol #"+self.volume.num
		self.local.immigration(self.volume)


	
class Local:
	#for doing things in the local 
	def __init__(self, volume):
		self.volume = volume
		def getFolders():#sets these vars.
			self.pwd = os.path.abspath(os.getcwd())
			self.outFolder= self.pwd+"/out"
			self.inFolder= self.pwd+"/in"
			self.cFolder = self.pwd+"/covers"
		getFolders()
	def immigration(self,volume):#move file from one folder to another. give next file over. This is done so there isn't such a long read for the 6k file run through.
		# print "get luluid to be put into json string and added to file."
		# browser.quit()
		print "make string and append to json1.txt"
		json_s = "{'lulu_id':"+str(volume.fcid)+",'sku':"+str(volume.sku)+",'volume':"+str(volume.num)+",'name':\""+str(volume.title)+"\"},"
		print json_s
		json_f = open("json4Ben2.txt","a")
		json_f.write(json_s)
		json_f.close()
		print "moving volume "+ volume.num + " to clear out folder and make listing and search faster."
		for i in os.listdir(self.inFolder+"/"+volume.round_folder):
			if self.inputFile == i:
				#move that file to out and then find the next one using volumeNum
				os.rename(self.inFolder+"/"+volume.round_folder+"/"+volume.inputFile, self.outFolder+"/"+volume.inputFile)
				break
		nextCheck=int(volume.num)+1#convert to int and add one to find next volume
		#make list and then use .sort() to get through all of them save it elsewhere. and keep pulling the last one out.
		print self
		for i in os.listdir(self.inFolder+"/"+volume.round_folder):#find the next inputFile
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
				Browser(i)
				break
	def ftpIt(self):
		try:
			session = ftplib.FTP(ftp_host,lulu_email,lulu_pass)
			session.cwd(self.volume.round_folder)
			print self.inFolder+self.volume.round_folder+'/mod'+self.volume.num+'.pdf'
			file = open(self.inFolder+self.volume.round_folder+'/mod'+self.volume.num+'.pdf','rb') # file to send
			session.storbinary('STOR mod'+self.volume.num+".pdf", file) # send the file
			file.close() # close file and FTP
			session.quit()
		except Exception, e:
			print str(e)
			if session:
				session.quit()
			self.ftpIt()

b = Browser(input_file)