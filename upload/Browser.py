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

from cgi import escape
from reportlab.pdfgen import canvas
from StringIO import StringIO
from selenium.common.exceptions import StaleElementReferenceException
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import UnexpectedAlertPresentException
from selenium.webdriver.firefox.webdriver import FirefoxProfile
# from browsermobproxy import Server
import selenium

import sys
import urllib
import time
from time import sleep
import re
import json
# import smtplib
from datetime import datetime
# from email.mime.text import MIMEText
import tweepy
import math

from selenium.webdriver.common.action_chains import ActionChains

# execfile("pass.py") #need to run this first to get credentials for this upload

sys.setrecursionlimit(4800) #try not to crash please~



        
class Browser:
        #for traversing the lulu interface
        def __init__(self, IPass, Volume, Local):
                

                self.const = IPass

                if self.const.make_log is True: #send output to logfile.
                    log_file = open("log.txt","w")
                    sys.stdout = log_file
                # print "input file: " + input_file
                
                self.stage = 0 #use stage to see where in the process you are so you know where to come back to.
                self.whoops = 0 #number of mistakes on this particular's entry's upload. you can change number of mistakes allowed in webFailure function.
                self.volume = Volume
                self.volume.travelAgent(self.const.input_file) #makevolume
                self.local = Local
                self.t_start = datetime.now()
                self.service_type = "//*[@id='productline_3']"
                self.service_type2 = "//*[@id='productline_3']/img"
                self.book_size = "//*[@id='preset_1040_0']"
                self.book_size2 = "//*[@id='preset_1040_0']/img"
                self.titlexpath = "/html/body/div[1]/div[3]/div[2]/div[1]/div[1]/h2"
                self.binding = "//*[@id='binding_4']"
                self.pg_count = "//*[@id='pagecount']"
                self.uid = "//*[@id='loginEmail']" #id/name for logging in
                self.pw = "//*[@id='loginPassword']" #password area
                self.logbutt = "//*[@id='loginSubmit']" #submit button
                self.my_page = "700"
                self.next = "//*[@id='fNext']"
                self.next_disable = "//*[@id='fNext' and not (@disabled)]"
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
                self.first_checkbox = "/html/body/div[1]/div[1]/div/table/tbody/tr/td[2]/div/div[3]/div[1]/form/div/div/div[2]/table/tbody/tr[1]/td[1]/input" #always the first input since it was most recently uploaded. no need to iterate.
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
                self.helv_in_enc_file = "/html/body/div[2]/div[1]/div/div[1]/div/div"
                profile = FirefoxProfile("/Users/wiki/Library/Application Support/Firefox/Profiles/3ig8qcrl.default-1435148476151")#profile is necessary to run the greasemonkey scripts.
                profile.set_preference("toolkit.startup.max_resumed_crashes", "-1")
                self.private_input = "/html/body/div[1]/div[3]/div[3]/div[2]/div/div[2]/form/div[1]/div[2]/div/div[4]/div[2]/ul/li[1]/input"
                self.driver = webdriver.Firefox(profile) #start ff w/profile
                self.driver.set_window_position(6920,0)#browser size and position.
                self.driver.set_window_size(3460,1120)
                print "opening new browser to automate upload of: " + self.volume.title + " on Firefox"
                self.driver.get("http://www.lulu.com/author/wizard/index.php?fWizard=hardcover")#navigate to author page
                print "cur time= " + str(self.t_start)
                self.pdf_pages = self.count_pages(self.local.inFolder+self.volume.round_folder+"/"+self.volume.input_file)
                self.pdf_pages=str(self.pdf_pages)
                self.luluCruise() #start!
        
                
        def sleep_inc(self,slumber_number):
            slum_num = slumber_number*self.const.sleep_multiplier # sleep multiplier is in pass.py
            sleep(slum_num)

        def count_pages(self,filename): #get number of pages for particular pdf.
            rxcountpages = re.compile(r"/Type\s*/Page([^s]|$)", re.MULTILINE|re.DOTALL)
            data = file(filename,"rb").read()
            return len(rxcountpages.findall(data))

        def webFailure(self, error="unknown"): #for when it fails :(
                print "WHOOPS. WHAT A @#$%^ing MESS"
                self.whoops +=1
                if self.stage==12: #theres no good way to recover this page without writing code to navigate to buyitnow page. it can be done though. this is a quick fix.
                    self.local.immigration(self.volume, self.driver, True, "did not get sku.")

                if self.whoops == 4: #if 4 failures. give up and go to the next one.
                        print "giving up on this one."
                        self.local.immigration(self.volume, self.driver,True,"failed too many at stage: "+str(self.stage) + " error: " +str(error))

                if self.volume.fcid == False: #if you don't have the fcid then start over. you didn't get far.
                        self.luluCruise()
                else:
                        self.driver.get("http://www.lulu.com/author/wizard/index.php?fCID="+self.volume.fcid)
                        self.luluCruise()
                        
        def execution(self,waitElement,waitTime,ex_type): # called whenever interacting with an element. element xpath, time to wait, and type of interaction (click, text, finding it)
            #uses xpaths. best practices suggests using ids but when first starting to develop this lulu didn't have that many ids for elements we needed to access. again this could be extended
            
            if ex_type=="click":
                if self.elemWait(waitTime,waitElement,ex_type) == True:
                    try:
                        x = self.driver.find_element_by_xpath(waitElement)
                        return x.click()
                    except:
                        e = sys.exc_info()[0]
                        my_fail = str(e)
                        print my_fail
                        self.webFailure(my_fail)
                else:
                    print "element @ " +waitElement+ " could not be found."
            elif ex_type=="text":
                if self.elemWait(waitTime,waitElement,ex_type) == True:
                    try:
                        x = self.driver.find_element_by_xpath(waitElement)
                        return x
                    except:
                        e = sys.exc_info()[0]
                        my_fail = str(e)
                        print my_fail
                        self.webFailure(my_fail)
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

        def elemWait(self,waitTime,waitElement,ex_type): #using webdriverwait wait for presence of element. self explanatory
                try:
                        WebDriverWait(self.driver, waitTime).until(EC.presence_of_element_located((By.XPATH, waitElement))) #if you find it return true
                        return True
                except:
                        e = sys.exc_info()[0]
                        my_fail = str(e) 
                        print my_fail
                        if ex_type=="find":
                                return False
                        if my_fail == "<class 'selenium.common.exceptions.ElementNotVisibleException'>": #can't find it
                                self.webFailure(my_fail)                        
                        else:
                                self.webFailure(my_fail)

        def existingText(self,tElement,changeTo):#this function deletes textarea or text input fields. For whatever reason .clear() was not working
                tElement.clear()
                tElement.send_keys(changeTo)

        def applyBarcode(self): #gets the barcode from barcode page, saves it, applies it to both the copyright .pdf and the cover file. saves copyright file as mod####.pdf to appropriate folder
                if self.execution(self.bc_image,20,"find") is False:
                    self.driver.quit()
                    self.stage = 0
                    self.luluCruise()
                bcImage = self.driver.find_element_by_xpath(self.bc_image)
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
                imgDoc.drawImage(imgPath, 300, 115, imgWidth, imgHeight)    ## at (303,115) with size 160x160
                imgDoc.save()

                # Use PyPDF to merge the image-PDF into the template
                page = PdfFileReader(file(self.local.cFolder+"/volume&&&"+self.volume.num+".pdf","rb")).getPage(0)
                overlay = PdfFileReader(StringIO(imgTemp.getvalue())).getPage(0)
                page.mergePage(overlay)
                output = PdfFileWriter()
                output.addPage(page)
                output.write(file(self.local.cFolder+"/bcVolume&&&"+self.volume.num+".pdf","w"))
                print "cover is up"

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
                imgDoc.drawImage(imgPath, 46, 40, imgWidth, imgHeight)
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

        def findNext(self,numOfPages,inc_type): #used while iterating through pages in the file selection section.
        #this function is only really used with the iteratefiles function which is not the first option when iterating through the files.
            print "in findnext"
            x = 1
            while x <= 50: #should never have more than 3.
                isNext = "//*[@id='pageLinks']/a["+str(x)+"]"
                print isNext;
                r_pglink = self.execution(isNext,50,"text")
                pglink = r_pglink.text
                print pglink + " this is pglink"
                if pglink == inc_type:
                    print isNext 
                    print "\ni found the next page button\n"
                    return isNext
                else: 
                    x+=1
            return False

        def jsIterateFiles(self,mod,encoding="utf-8"): #uses javascript to navigate through files on lulu to find correct file, click it, and add to project. 
                #sometimes it doesn't work --either because there's a file in there that's named incorrectly, the pages aren't loading properly or the target file is not there, and then it tries using the old iterate files.
                #this function is janky in general because of the fact that you must load an iframe which sometimes takes a while. 
                #This is still far better than uploading the files straight up during the time of execution which adds an additional 3minutes to each upload.
                print "about to go through files"
                print "get in iframe"
                self.driver.switch_to_frame(self.driver.find_element_by_tag_name("iframe"))
                self.execution(self.folders_option,200,"click")#clickin the folders
                print "sleep 2"
                sleep(2)
                n = 1
                print "get the folder"
                while(1):#for finding the folder.
                        t_folder = self.execution("/html/body/div[1]/div[1]/div/table/tbody/tr/td[1]/div/div[2]/ul[2]/li["+str(n)+"]/a",10,"text")
                        print t_folder.text + " this is text of folder! n is at " + str(n) + " folder_name" + self.volume.round_folder
                        if(self.volume.round_folder.strip("/") == t_folder.text):
                                self.execution("/html/body/div[1]/div[1]/div/table/tbody/tr/td[1]/div/div[2]/ul[2]/li["+str(n)+"]/a",30,"click")
                                break
                        else:
                                n+=1
                self.driver.switch_to_default_content() #switch out of iframe to get number of pages.   
                sleep(2)
                numOfPages = self.driver.execute_script('return document.getElementById("myFilesFrame").contentDocument.getElementById("pageCount").innerHTML')
                print "there are a total of " + str(numOfPages) + " pages."
                numOfPages = int(numOfPages)
                self.driver.switch_to_frame(self.driver.find_element_by_tag_name("iframe"))
                sleep(2)
                file_is_here = False
                self.driver.switch_to_default_content() #switch to embed js script.
                print "open js file"
                self.driver.execute_script(open("./getFiles.js").read()) #javascript file containing the whereFile(). embeds onto page
                json_ifile = json.dumps(self.volume.input_file) #must do this to properly escape strings for the javascript function input.
                print json_ifile
                escaped_string = escape(json_ifile)
                wherefile_script = "return whereFile("+escaped_string+");" 
                iter_cycle = 0
                file_is_here = self.driver.execute_script(wherefile_script) #run the script. returns false if not there. otherwise returns string.
                print "first attempt at wherefile"
                if file_is_here != False: #hellyeah click on 
                    print str(file_is_here) + " < that is wherefile."
                    checkbox = "/html/body/div[1]/div[1]/div/table/tbody/tr/td[2]/div/div[3]/div[1]/form/div/div/div[2]/table/tbody/tr["+str(file_is_here)+"]/td[1]/input"
                    print checkbox
                    self.driver.switch_to_frame(self.driver.find_element_by_tag_name("iframe"))
                    self.execution(checkbox,30,"click")
                    self.execution(self.submit_button,40,"click")#click submit
                    is_error = self.execution(self.helv_in_enc_file,8,"find") 
                    if(isinstance(is_error,bool)==False): #check for helv error in the file.
                            print "this is not going to work. go to the next file plz"
                            self.local.immigration(self.volume, self.driver,True, "hevl error")
                    else:
                        if is_error==True:
                            print "sorry helv error."
                            self.local.immigration(self.volume,self.driver,True, "helv error")
                    self.driver.switch_to_default_content() #congrats you found it and are good.
                    return
                elif numOfPages>1: #if there is not a file there then go to next page. there will only ever be two pages so this should only happen once.
                    print "jsiter go to next page"
                    self.driver.switch_to_frame(self.driver.find_element_by_tag_name("iframe"))
                    xNext = self.findNext(numOfPages,">")
                    self.execution(xNext,300,"click")
                    print "clicked xnext"
                    self.driver.switch_to_default_content()
                    sleep(2)
                    file_is_here = self.driver.execute_script(wherefile_script)
                    print file_is_here
                    print "^^second page atteempt"
                    if file_is_here != False: #success
                        print str(file_is_here) + " < that is wherefile."
                        checkbox = "/html/body/div[1]/div[1]/div/table/tbody/tr/td[2]/div/div[3]/div[1]/form/div/div/div[2]/table/tbody/tr["+str(file_is_here)+"]/td[1]/input"
                        print checkbox
                        self.driver.switch_to_frame(self.driver.find_element_by_tag_name("iframe"))
                        self.execution(checkbox,30,"click")
                        self.execution(self.submit_button,40,"click")#click submit
                        is_error = self.execution("/html/body/div[2]/div[1]/div/div[1]/div/div",5,"find")
                        if(isinstance(is_error,bool)==False):
                                print "this is not going to work. go to the next file plz"
                                self.local.immigration(self.volume, self.driver,True, "hevl error")
                        else:
                            if is_error==True:
                                print "sorry helv error."
                                self.local.immigration(self.volume,self.driver,True, "helv error")
                        self.driver.switch_to_default_content()
                        return
                    else: #try using old iterate files function. this may be unecessary at this point. future dev may want to change this to ftping up the file because it's obviously missing.
                        if self.iterateFiles(False)==True:
                            sleep(1)
                            return
                        else:
                            # self.local.immigration(self.volume,self.driver,True,"something else...")
                            #attempt to upload file yourself. webfail involved here so it should fail gracefully.
                            self.local.ftpIt(True)
                            sleep(3)
                            self.webFailure()
                else:
                    self.driver.switch_to_default_content()
                    if(self.iterateFiles(False)==True):
                        sleep(1)
                        return
                    else:
                        # print 'it was not meant to be'
                        # self.local.immigration(self.volume, self.driver,True, "finding file error")
                        self.local.ftpIt(True)
                        sleep(3)
                        self.webFailure()
                
        def iterateFiles(self,mod,encoding="utf-8"):
                print "about to go through files"
                print "get in iframe"
                self.driver.switch_to_frame(self.driver.find_element_by_tag_name("iframe"))
                self.execution(self.folders_option,200,"click")#clickin the folders
                sleep(2)
                n = 1
                print "looking for folder"
                while(1):#for finding the folder.
                        t_folder = self.execution("/html/body/div[1]/div[1]/div/table/tbody/tr/td[1]/div/div[2]/ul[2]/li["+str(n)+"]/a",10,"text")
                        print t_folder.text + " this is text of folder! n is at " + str(n) + " folder_name" + self.volume.round_folder
                        if(self.volume.round_folder.strip("/") == t_folder.text):
                                self.execution("/html/body/div[1]/div[1]/div/table/tbody/tr/td[1]/div/div[2]/ul[2]/li["+str(n)+"]/a",30,"click")
                                sleep(1)
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
                                        print str(i) + " " + isFile.encode(encoding)
                                        if mod is True:
                                                if "mod"+self.volume.num==isFile[:7]:
                                                        print "~found your file~"
                                                        exitLoop = True
                                                        checkBox = xFile[:-8] + "[1]/input"
                                                        self.execution(checkBox,3,"click")
                                                        self.execution(self.submit_button,20,"click")#click submit
                                                        sleep(3)
                                                        return True
                                        else:
                                                if self.volume.num==isFile[:4]:
                                                        print "~found your file~"
                                                        exitLoop = True
                                                        checkBox = xFile[:-8] + "[1]/input"
                                                        self.execution(checkBox,3,"click")
                                                        self.execution(self.submit_button,20,"click")#click submit
                                                        sleep(1)
                                                        is_error = self.execution("/html/body/div[2]/div[1]/div/div[1]/div/div",6,"find")
                                                        print is_error
                                                        if(isinstance(is_error,bool)==False):
                                                                print "this is not going to work. go to the next file plz"
                                                                self.local.immigration(self.volume, self.driver,True, "finding file")
                                                        else:
                                                            if is_error==True:
                                                                print "sorry helv error."
                                                                self.local.immigration(self.volume,self.driver,True, "finding file")
                                                        sleep(2)
                                                        return True

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
                                        # sleep(1)
                                        self.execution(xNext,300,"click")
                                        continue
                                except StaleElementReferenceException:
                                        print "goddammit it's not attached to dom"
                                        i=0
                                        x=0
                                        continue


        def evalPageTitle(self): #this function is in here to monitor progress of the cruise. sometimes it will be on a page trying to do actions meant for a different stage (selecting files when it should be getting barcode). never implemented -- future dev?
                cur_title = self.driver.title.lower()
                print cur_title + " this is current title of this page~"
                return cur_title

        def evalWizTitle(self):

            wiz_title = self.execution(self.titlexpath,20,"text").text
            print wiz_title + " this is cur wiz title."
            return wiz_title

        def zoomit(self,in_or_out): #for zooming in to make the file look bigger for projector. zooming back out is so the barcode's location is in the same place 
                if self.execution('/html',10,"find") is True:
                    html = self.driver.find_element_by_tag_name("html")
                else:
                    self.driver.quit()
                    Browser(str(self.volume.input_file))
                if in_or_out=="out":
                        html.send_keys(Keys.COMMAND, Keys.SUBTRACT)
                        html.send_keys(Keys.COMMAND, Keys.SUBTRACT)
                        html.send_keys(Keys.COMMAND, Keys.SUBTRACT)
                        html.send_keys(Keys.COMMAND, Keys.SUBTRACT)
                elif in_or_out=="in":
                        html.send_keys(Keys.COMMAND, Keys.ADD)
                        html.send_keys(Keys.COMMAND, Keys.ADD)
                        html.send_keys(Keys.COMMAND, Keys.ADD)
                        html.send_keys(Keys.COMMAND, Keys.ADD)
#the following are the different stages of upload.
        def loginPage(self): #stage 1.
                # print "zoomin"
                # self.zoomit("in")
                self.sleep_inc(1)
                print self.evalPageTitle()
                if self.evalPageTitle() != "registrieren und anmelden" and self.evalPageTitle() != "sign up & log in" and self.evalPageTitle() != "registreren en aanmelden":
                        self.webFailure("got ahead of myself")
                
                r_uid = self.execution(self.uid,15,"text")

                r_uid.send_keys(self.const.lulu_email)
                self.sleep_inc(2)
                r_pw = self.execution(self.pw,15,"text")
                r_pw.send_keys(self.const.lulu_pass)
                self.sleep_inc(1)
                self.execution(self.logbutt,20,"click") #submit by clicking
                self.stage+=1
                print "successfully logged in, now get to creating the book"
                self.luluCruise()
        def bookOptions(self): #2
                print "setting book options"
                print "serv type"
                self.sleep_inc(1)
                self.execution(self.service_type,20,"click")
                self.execution(self.service_type2,20,"click")
                # self.execution(self.service_type,20,"click")
                print "size"
                self.sleep_inc(2)
                element_hover = self.driver.find_element_by_id("preset_1040_0")
                hover = ActionChains(self.driver).move_to_element(element_hover)
                hover.perform()
                self.execution(self.book_size,20,"click")
                self.execution(self.book_size2,20,"click")
                print "pg count"
                self.sleep_inc(2)
                self.existingText(self.execution(self.pg_count,10,"text"),self.pdf_pages)#clear this thing and put in 700
                self.execution(self.binding,10,"click")
                print "clicked binding"
                self.execution(self.next,10,"click")
                print "clicked next!"
                self.stage+=1
                self.luluCruise()
        def authorPage(self): #3. grabs fcid here.
                print self.volume.num
                print "^thats num"
                print "title and author page"

                if self.const.upload_type == "reg":
                        lulu_title = "Volume "+self.volume.num+", "+self.volume.title
                elif self.const.upload_type == "contrib":
                        lulu_title = "Contributor Appendix: Volume "+self.volume.num+", "+self.volume.title
                elif self.const.upload_type == "toc":
                        lulu_title = "Table of Contents: Volume "+self.volume.num+", "+self.volume.title
                self.sleep_inc(2)
                if isinstance(lulu_title, unicode):
                    self.existingText(self.execution(self.book_title,10,"text"),lulu_title)
                else:
                    self.existingText(self.execution(self.book_title,10,"text"),unicode(lulu_title, 'utf-8'))
                self.sleep_inc(2)
                self.existingText(self.execution(self.fname,10,"text"),self.const.author_fn)
                self.sleep_inc(2)
                self.existingText(self.execution(self.lname,10,"text"),self.const.author_ln)
                print "snag the fcid"
                self.driver.execute_script('document.getElementById("projectDetailsContent").style.display="block"') #show the meta info.
                self.volume.fcid = self.execution(self.project_id,30,"text").text
                print self.volume.fcid
                self.sleep_inc(2)
                self.execution(self.next,10,"click")
                self.stage+=1
                self.luluCruise()

        def isbn1(self): #4
                
                print "get to isbn page"
                self.execution(self.next,10,"click")
                print "only necessary for this page"
                self.stage+=1
                self.luluCruise()
        def isbn2(self): #5 barcode and ftp.
                print "zoom out for barcode"
                # self.zoomit("out")
                print "dl pdf on this page" 
                self.sleep_inc(1)
                # self.applyBarcode()
                print "back to normal"
                # self.zoomit("in")
                print "ftp modified pre-image ready to be sent on up"
                if self.local.ftpIt() is False:
                    self.webFailure()
                self.sleep_inc(2)
                self.execution(self.next,10,"click")
                self.stage+=1
                self.luluCruise()
        def selectFiles1(self): #6 selects modified copyright page.
                self.execution(self.myFiles,30,"click")
                
                print self.first_checkbox
                self.driver.switch_to_frame(self.driver.find_element_by_tag_name("iframe"))
                self.execution(self.first_checkbox,30,"click")
                self.execution(self.submit_button,40,"click")#click submit
                print "file selected soundly"
                self.sleep_inc(2)
                self.stage+=1
                self.luluCruise()
        def selectFiles2(self): #7 select actual pdf
                self.execution(self.myFiles,30,"click")
                self.sleep_inc(1)
                self.jsIterateFiles(False)
                print "got your file and sleep"
                self.sleep_inc(3)
                self.driver.switch_to_default_content()
                print "open js file for c2"
                self.driver.execute_script(open("./getFiles.js").read())
                #this was in place to make sure that the two files that were selected are the correct files and not two mod files or two encyclopedia files. was having issues toward end of upload. 
                #really only an issue if connection is bad.
                # check2 = self.driver.execute_script('return check2();')
                # print check2
                # if check2 == "over":
                #     self.driver.quit()
                #     Browser(str(self.volume.input_file))
                # elif check2==True:
                #     print "no mod"
                #     self.jsIterateFiles(check2)
                # elif check2 ==False:
                #     print "no large"
                #     self.jsIterateFiles(check2)
                # else:
                #     print "both files selected and placed soundly, phew!"
                self.execution(self.next_disable,30,"click")
                self.stage+=1
                self.luluCruise()

        def makingYour(self): #8 wait for that nightmarish gears animation to finish. there's a greasemonkey script that skips to next page.
                print "gears animation! lookit 'em go!"
                self.execution(self.next_disable,70,"click")
                print "let our grease monkey screw the wizard"
                self.stage+=1
                self.luluCruise()

        def uploadCover(self): #9 cold potentially upload these like was done to mod####.pdf ... would save *some time and then could iterate through and find like in #7
                self.sleep_inc(2)
                print self.evalWizTitle()
                # exit()
                title_stage_8 = open("title8.txt","a")
                title_stage_8.write("Making Your PrintReady Cover " + self.evalWizTitle().encode("ascii","ignore") + "\n")
                title_stage_8.close()
                if self.evalWizTitle() == "Making Your Print-Ready Cover" or self.evalWizTitle() == "Ihr druckfertiges Cover wird erstellt" or self.evalWizTitle() == "Je afdrukbare omslag wordt aangemaakt":
                    print "it is the print ready cover here after failure."
                    self.execution(self.next,30,"click")
                    self.stage+=1
                    self.luluCruise()
                    # sleep(2)
                print "uploading cover"
                r_cover_upload = self.execution(self.cover_upload,50,"text")
                r_cover_upload.send_keys(self.local.cFolder +"/"+"volume&&&"+self.volume.num+".pdf")
                self.execution(self.c_up_button,60,"click")
                print "wait for upload to complete..."
                self.execution(self.next_disable,50,"click")
                self.execution(self.next,30,"click")
                self.stage+=1
                self.luluCruise()

        def pubOptions(self): #10
                if self.execution("//*[@id='category']",10,"find")==False:
                    self.sleep_inc(3)
                catSelect=self.driver.find_element_by_xpath("//*[@id='category']")
                catOptions = catSelect.find_elements_by_tag_name("option")
                if self.const.upload_type=="reg":
 #                   pub_option = "Gedichte & Reime"
                    pub_option = ["Poetry","Gedichte & Reime","Gedichten"]
                else:
                	pub_option = ["Reference"]
                for option in catOptions:
                        if option.text in pub_option:
                            print "found particular option in select"
                            option.click()
                            break
                print "keywords"
                self.sleep_inc(2)
                r_keywords = self.execution(self.keywords,150,"text")
                r_keywords.send_keys("Poetry, Reference, Wikipedia, Mandiberg")
                self.sleep_inc(2)
                r_desc = self.execution(self.description,100,"text")
                #use twitter class to shorten strings appropriately
                title_arr = self.local.tweeter.proportionally_shorten_strings(self.volume.title.split(" --- ")[0],self.volume.title.split(" --- ")[1],100)
                if self.const.upload_type == "reg":
                        desc_title = "Volume "+self.volume.num+", "+title_arr[0]+"---"+title_arr[1]+"\n\n"
                elif self.const.upload_type == "contrib":
                        desc_title = "Contributor Appendix: Volume "+self.volume.num+", "+title_arr[0]+" --- "+title_arr[1]+"\n\n"
                elif self.const.upload_type == "toc":
                        desc_title = "Table of Contents: Volume "+self.volume.num+", "+title_arr[0]+" --- "+title_arr[1]+"\n\n"

                # r_desc.send_keys(unicode(desc_title+"Print Wikipedia is a both a utilitarian visualization of the largest accumulation of human knowledge and a poetic gesture towards the inhuman scale of big data. Michael Mandiberg wrote software transforms all of Wikipedia in thousands of print on print-on-demand volumes, drawing attention to the sheer size of the encyclopedia's content and the impossibility of rendering Wikipedia as a poetic material object in fixed form: Once a volume is printed it is already out of date. This German version encompases 3406 volumes that were uploaded in May and June 2016.\n\nEine gängige Größenordnung für Enzyklopädien sind die Bände ihrer Ausgaben. Michael Mandiberg hat die aktuelle Version der deutschsprachigen Wikipedia in enzyklopädische Artikel umgerechnet und eine ganze Bibliothek des Wissens geschaffen: 3.406 Bände.", 'utf-8','ignore'))
                r_desc.send_keys(unicode(desc_title+"Print Wikipedia is a both a utilitarian visualization of the largest accumulation of human knowledge and a poetic gesture towards the inhuman scale of big data. Michael Mandiberg wrote software transforms all of Wikipedia in thousands of print on print-on-demand volumes, drawing attention to the sheer size of the encyclopedia's content and the impossibility of rendering Wikipedia as a poetic material object in fixed form: Once a volume is printed it is already out of date. This Dutch version encompasses 1165 volumes that were uploaded in November 2016.\n\nIn november 2016 veranderde Michael Mandiberg de huidige versie van de Nederlandse versie van Wikipedia in een hele bibliotheek van kennis. Dit is er één van 1165 volumes.", 'utf-8','ignore'))
                self.sleep_inc(3)
                r_copy = self.execution(self.copyright,30,"text")
                self.sleep_inc(1)
                r_copy.send_keys("Wikipedia Contributors")
                lisc_select =self.execution(self.license,30,"text")
                liscOptions = lisc_select.find_elements_by_tag_name("option")
                for option in liscOptions:
                        if option.text=="Creative Commons Attribution-ShareAlike 2.0" or option.text=="Creative Commons Namensnennung-Weitergabe unter gleichen Bedingungen 2.0" or option.text=="Naamsvermelding-GelijkDelen 2.0":
#                        if option.text=="Creative Commons Namensnennung-Weitergabe unter gleichen Bedingungen 2.0":
                                option.click()
                                break
                r_edition =self.execution(self.edition,30,"text")
                self.sleep_inc(2)
                r_edition.send_keys('03')
                r_publisher = self.execution(self.publisher,30,"text")
                self.sleep_inc(2)
                r_publisher.send_keys("Michael Mandiberg")
                self.sleep_inc(2)
                self.execution(self.next,30,"click")
                self.stage+=1
                self.luluCruise()

        def changePrice(self): #11
                print 'set price now'
                if self.execution(self.setPrice,20,"find") is False:
                    self.sleep_inc(3)
                r_price = self.driver.find_element_by_xpath(self.setPrice)# self.execution(self.setPrice,30,"text")
                #unfortunately this needs to be done like this. clear() doesn't work because of some javascript monitor that lulu uses.
                r_price.send_keys(Keys.BACK_SPACE)
                r_price.send_keys(Keys.BACK_SPACE)
                r_price.send_keys(Keys.BACK_SPACE)
                r_price.send_keys(Keys.BACK_SPACE)
                r_price.send_keys(Keys.BACK_SPACE)
                r_price.send_keys(Keys.BACK_SPACE)
                self.sleep_inc(2)
                r_price.send_keys(Keys.NUMPAD7)
                self.sleep_inc(1)
                r_price.send_keys(Keys.NUMPAD5)
                self.sleep_inc(1)
                self.execution(self.next,50,"click")
                self.stage+=1
                self.luluCruise()

        def review(self): #12
                print "almost there. just lemme review the order here"
                print "book " +self.volume.title+ " pushed to lulu okay."
                self.sleep_inc(5)
                self.execution(self.next,50,"click")
                self.stage+=1
                self.luluCruise()

        def isbnNSku(self): #13 last one. gets isbn if you need it. goes to next page and gets sku. been having trouble recently with sku.
                lulu_isbn = self.execution(self.x_lulu_isbn,20,"text")
                self.volume.isbn = lulu_isbn.text
                self.sleep_inc(4)
                if self.const.is_private:
                    print "u r shy boy"
                    self.execution(self.private_input,10,"click")
                # self.sleep_inc(4)
                print "try click next"
                # exit()
                self.execution(self.next,10,"click")
                #this is new next button? or only after private input?
                # self.execution("/html/body/div[1]/div[3]/div[3]/div[3]/div[1]/ul/li/div/form/input[5]",10,"click")
                # self.execution(self.next,50,"click")
                print "go to the sale page to grab the SKU code."
                # exit()
                self.execution(self.x_sell,20,"click")
                self.sleep_inc(3)
                self.volume.sku = self.execution(self.sku_x,30,"text").get_attribute('value')
                print self.volume.sku +  " sku"
                print "begin next book"
                self.local.immigration(self.volume, self.driver)

        def luluCruise(self):
                self.t_now = datetime.now()
                self.t_diff = self.t_now-self.t_start
                print str(self.t_diff) + " on stage " + str(self.stage)
                if int(self.volume.num) % 2 == 0:
                    time_monitor = open("time_log.txt","a")
                    time_monitor.write(str(self.volume.num)+ " " + self.volume.title+str(self.t_diff) + " on stage " + str(self.stage)+"\n")
                    time_monitor.close()
                if self.stage==0:
                        self.loginPage()
                elif self.stage==1:
                        self.bookOptions()
                elif self.stage==2:
                        self.authorPage()
                elif self.stage==3:
                        self.isbn1()
                elif self.stage==4:
                        self.isbn2()
                elif self.stage==5:
                        self.selectFiles1()     
                elif self.stage==6:
                        self.selectFiles2()
                elif self.stage==7:
                        self.makingYour()
                elif self.stage==8:
                        self.uploadCover()
                elif self.stage==9:
                        self.pubOptions()
                elif self.stage==10:
                        self.changePrice()
                elif self.stage==11:
                        self.review()
                elif self.stage==12:
                        self.isbnNSku()         
                else:
                        errormsg = "stage not recognized. we need to quit"
                        exit()