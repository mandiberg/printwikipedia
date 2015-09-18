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
from cgi import escape
from reportlab.pdfgen import canvas
from StringIO import StringIO
from selenium.common.exceptions import StaleElementReferenceException
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import UnexpectedAlertPresentException
from selenium.webdriver.firefox.webdriver import FirefoxProfile
from browsermobproxy import Server
import selenium
import os
import sys
import urllib
import time
from time import sleep
import re
import json
import smtplib
from datetime import datetime
from email.mime.text import MIMEText
import tweepy
import math

execfile("crash_pass.py")
# execfile("too_big_crash.py")
execfile("crash_list.py")
sys.setrecursionlimit(4800)
if make_log is True:#send output to logfile.
        log_file = open("log.txt","w")
        sys.stdout = log_file

class Volume:
        #stores variables for the individual volume should be extended by browser.
        def __init__(self):
                self.fcid = False
        def roundDown(self,divisor=20):#returns a string of closest rounded down with 4 leading zeros if possible.
                fol = '{0:04d}'.format(int(self.num) - (int(self.num)%divisor))
                if(fol=="0000"):
                        return "/0001"
                return "/"+fol

        def travelAgent(self,input_file):
                print "splitting strings, encoding for unicode to make firefox happy. sending off file: " + input_file
                splitInput = input_file.split('&&&')#split on the dash symbol. to make a nice buncha strings
                self.num = splitInput[0]#this is the volume number we are currently on. it is a string!
                self.title = splitInput[1]+" --- "+splitInput[2]
                self.input_file=unicode(input_file,'utf-8')
                self.round_folder = self.roundDown()
        
class Browser:
        #for traversing the lulu interface
        def __init__(self, input_file):
                # print "input file: " + input_file
                
                self.stage = 0 #use stage to see where in the process you are so you know where to come back to.
                self.whoops = 0
                self.volume = Volume()
                self.makeVolume(input_file) #makevolume
                self.local = Local(self.volume)
                self.t_start = datetime.now()
                self.service_type = "//*[@id='productline_3']"
                self.book_size = "//*[@id='preset_1037_73']"
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
                # server = Server("/Users/wiki/Downloads/browsermob-proxy-2.1.0-beta-1/bin/browsermob-proxy")
                # server.start()
                # proxy = server.create_proxy()
                # proxy.blacklist('http://.*\\.veinteractive.com/.*', 200)
                profile = FirefoxProfile("/Users/wiki/Library/Application Support/Firefox/Profiles/3ig8qcrl.default-1435148476151")
                profile.set_preference("toolkit.startup.max_resumed_crashes", "-1")
                # profile.set_proxy(proxy.selenium_proxy())
                print profile
                self.driver = webdriver.Firefox(profile)
                # self.driver = webdriver.Firefox()
                self.driver.set_window_position(1920,0)
                self.driver.set_window_size(1920,1080)
                # self.driver.set_window_size(880,620)
                print "opening new browser to automate upload of: " + self.volume.title + " on Firefox"
                self.driver.get("http://www.lulu.com/author/wizard/index.php?fWizard=hardcover")#change to author page
                print "cur time= " + str(self.t_start)
                nextCheck = self.volume.input_file
                nextCheck = nextCheck.split("&&&")
                nextCheck = int(nextCheck[0])
                if nextCheck<1400:
                    lulu_email = 'printwikipedia@printwikipedia.com'
                elif nextCheck > 1400 and nextCheck < 2540:
                    lulu_email = 'printwikipedia+1@printwikipedia.com'
                elif nextCheck > 2540 and nextCheck < 4420:
                    lulu_email = 'printwikipedia+2@printwikipedia.com'
                elif nextCheck > 4420 and nextCheck < 5120:
                    lulu_email = 'printwikipedia+3@printwikipedia.com'
                elif nextCheck > 5120 and nextCheck < 6280:
                    lulu_email = 'printwikipedia+4@printwikipedia.com'
                elif nextCheck > 6280:
                    lulu_email = 'printwikipedia+5@printwikipedia.com'

                
                self.luluCruise() #start
        
        def makeVolume(self,input_file):
                self.volume.travelAgent(input_file)
        

        def count_pages(self,filename):
            rxcountpages = re.compile(r"/Type\s*/Page([^s]|$)", re.MULTILINE|re.DOTALL)
            data = file(filename,"rb").read()
            return len(rxcountpages.findall(data))

        def webFailure(self, error="unknown"):
                print "WHOOPS. WHAT A @#$%^ing MESS"
                self.whoops +=1
                if self.stage==11:
                    self.local.immigration(self.volume,self.driver,True,"did not get sku.")
                if self.whoops == 4:
                        print "giving up on this one."
                        # self.emergencyMail(error)
                        self.local.immigration(self.volume, self.driver,True,"failed too many at stage: "+str(self.stage) + " error: " +str(error))
                if self.volume.fcid == False:
                        self.luluCruise()
                else:
                        self.driver.get("http://www.lulu.com/author/wizard/index.php?fCID="+self.volume.fcid)
                        self.luluCruise()
                        
        def execution(self,waitElement,waitTime,ex_type):#called whenever interacting with an element
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

        def elemWait(self,waitTime,waitElement,ex_type): #this is a certain kind of wait (upload buttons and loading screens)
                try:
                        WebDriverWait(self.driver, waitTime).until(EC.presence_of_element_located((By.XPATH, waitElement)))
                        return True
                except:
                        e = sys.exc_info()[0]
                        my_fail = str(e)
                        print my_fail
                        if my_fail == "<class 'selenium.common.exceptions.UnexpectedAlertPresentException'>":
                                print "this is not going to work. go to the next file plz"
                                self.local.immigration(self.volume, self.driver,True,"UnexpectedAlertPresentException")
                        elif my_fail == "<class 'selenium.common.exceptions.ElementNotVisibleException'>":
                                self.webFailure(my_fail)
                        if ex_type=="find":
                                return False
                        else:
                                self.webFailure(my_fail)

        def existingText(self,tElement,changeTo):#this function deletes textarea or text input fields. For whatever reason .clear() was not working
                tElement.clear()
                tElement.send_keys(changeTo)
        def applyBarcode(self):
                # xbc = self.execution(self.bc_image,20,"")
                # location = xbc.location
                if self.execution("/html/body/div/div[3]/div[2]/div[2]/div/div[2]/div[4]/a/img",20,"find") is False:
                    self.driver.quit()
                    self.stage = 0
                    self.luluCruise()
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
                imgDoc.drawImage(imgPath, 46, 40, imgWidth, imgHeight)    ## at (303,115) with size 160x160
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
            print "in findnext"
            x = 1
            while x <= 50:
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
        def jsIterateFiles(self,mod,encoding="utf-8"):
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
                self.driver.switch_to_default_content()         
                sleep(2)
                numOfPages = self.driver.execute_script('return document.getElementById("myFilesFrame").contentDocument.getElementById("pageCount").innerHTML')
                print "there are a total of " + str(numOfPages) + " pages."
                numOfPages = int(numOfPages)
                self.driver.switch_to_frame(self.driver.find_element_by_tag_name("iframe"))
                sleep(2)
                file_is_here = False
                self.driver.switch_to_default_content()
                print "open js file"
                self.driver.execute_script(open("./getFiles.js").read())
                json_ifile = json.dumps(self.volume.input_file)
                print json_ifile
                escaped_string = escape(json_ifile)
                wherefile_script = "return whereFile("+escaped_string+");"
                iter_cycle = 0
                file_is_here = self.driver.execute_script(wherefile_script)
                print "first attempt at wherefile"
                if file_is_here != False:
                    print str(file_is_here) + " < that is wherefile."
                    checkbox = "/html/body/div[1]/div[1]/div/table/tbody/tr/td[2]/div/div[3]/div[1]/form/div/div/div[2]/table/tbody/tr["+str(file_is_here)+"]/td[1]/input"
                    print checkbox
                    self.driver.switch_to_frame(self.driver.find_element_by_tag_name("iframe"))
                    self.execution(checkbox,30,"click")
                    self.execution(self.submit_button,40,"click")#click submit
                    is_error = self.execution("/html/body/div[2]/div[1]/div/div[1]/div/div",8,"find")
                    if(isinstance(is_error,bool)==False):
                            print "this is not going to work. go to the next file plz"
                            self.local.immigration(self.volume, self.driver,True, "hevl error")
                    else:
                        if is_error==True:
                            print "sorry helv error."
                            self.local.immigration(self.volume,self.driver,True, "helv error")
                    self.driver.switch_to_default_content()
                    return
                elif numOfPages>1: #there will only ever be two pages so this should only happen once.
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
                    else:
                        if self.iterateFiles(False)==True:
                            sleep(1)
                            return
                        else:
                            # self.local.immigration(self.volume,self.driver,True,"something else...")
                            self.local.ftpIt(True)
                            sleep(3)
                            self.webFailure()

                else:
                    self.driver.switch_to_default_content()
                    if(self.iterateFiles(False)==True):
                        sleep(1)
                        return
                    else:
                        print 'it was not meant to be'
                        self.local.ftpIt(True)
                        sleep(3)
                        self.webFailure()
                        # self.local.immigration(self.volume, self.driver,True, "finding file error")
               
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
                #this is the checkbox which is just one td over so we chop off the end of the xfile path

        def emergencyMail(self,errormsg):
                msg['Subject'] = 'lulu fucked up.'
                msg['From'] = "jkiritharan@gmail.com"
                msg['To'] = "jkiritharan@gmail.com"
                msg.attach(MIMEText(errormsg, "plain"))
                s = smtplib.SMTP('localhost')
                s.sendmail("jkiritharan@gmail.com", ["jkiritharan@gmail.com"], msg.as_string())
                s.quit()

        def evalPageTitle(self):
                cur_title = self.driver.title.lower()
                print cur_title
                return cur_title

        def zoomit(self,in_or_out):
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

        def loginPage(self):
                print "zoomin"
                self.zoomit("in")
                if self.evalPageTitle() != "sign up & log in":
                        self.webFailure("got ahead of myself")
                r_uid = self.execution(self.uid,15,"text")
                r_uid.send_keys(lulu_email)
                r_pw = self.execution(self.pw,15,"text")
                r_pw.send_keys(lulu_pass)
                self.execution(self.logbutt,20,"click") #submit by clicking
                self.stage+=1
                print "successfully logged in, now get to creating the book"
                self.luluCruise()
        def bookOptions(self):
                print "setting book options"
                print "serv type"
                self.execution(self.service_type,20,"click")
                print "size"
                self.execution(self.book_size,20,"click")
                print "pg count"
                self.existingText(self.execution(self.pg_count,10,"text"),self.my_page)#clear this thing and put in 700
                self.execution(self.binding,10,"click")
                print "clicked binding"
                self.execution(self.next,10,"click")
                print "clicked next!"
                self.stage+=1
                self.luluCruise()
        def authorPage(self):
                print "title and author page"
                if upload_type == "reg":
                        lulu_title = "Volume "+self.volume.num+", "+self.volume.title
                elif upload_type == "contrib":
                        lulu_title = "Contributor Appendix: Volume "+self.volume.num+", "+self.volume.title
                elif upload_type == "toc":
                        lulu_title = "Table of Contents: Volume "+self.volume.num+", "+self.volume.title
                self.existingText(self.execution(self.book_title,10,"text"),unicode(lulu_title, 'utf-8'))
                self.existingText(self.execution(self.fname,10,"text"),author_fn)
                self.existingText(self.execution(self.lname,10,"text"),author_ln)
                print "snag the fcid"
                self.driver.execute_script('document.getElementById("projectDetailsContent").style.display="block"')
                self.volume.fcid = self.execution(self.project_id,30,"text").text
                print self.volume.fcid
                self.execution(self.next,10,"click")
                self.stage+=1
                self.luluCruise()
        def isbn1(self):
                print "get to isbn page"
                self.execution(self.next,10,"click")
                print "only necessary for this page"
                self.stage+=1
                self.luluCruise()
        def isbn2(self):
                print "zoom out for barcode"
                self.zoomit("out")
                print "dl pdf on this page" 
                self.applyBarcode()
                print "back to normal"
                self.zoomit("in")
                print "ftp modified pre-image ready to be sent on up"
                if self.local.ftpIt() is False:
                    self.webFailure()
                self.execution(self.next,10,"click")
                self.stage+=1
                self.luluCruise()
        def selectFiles1(self):
                self.execution(self.myFiles,30,"click")
                checkbox = "/html/body/div[1]/div[1]/div/table/tbody/tr/td[2]/div/div[3]/div[1]/form/div/div/div[2]/table/tbody/tr[1]/td[1]/input"
                print checkbox
                self.driver.switch_to_frame(self.driver.find_element_by_tag_name("iframe"))
                self.execution(checkbox,30,"click")
                self.execution(self.submit_button,40,"click")#click submit
                print "file selected soundly"
                sleep(1)
                self.stage+=1
                self.luluCruise()
        def selectFiles2(self):
                self.execution(self.myFiles,60,"click")
                self.jsIterateFiles(False)
                print "supposedly got your file and sleep 2"
                sleep(3)
                self.driver.switch_to_default_content()
                print "open js file for c2"
                # self.driver.execute_script(open("./getFiles.js").read())
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
                self.execution(self.next_disable,70,"click")
                self.stage+=1
                self.luluCruise()
        def makingYour(self):
                print "gears animation! lookit 'em go!"
                self.execution(self.next_disable,95,"click")
                print "let our greasey monkey screw the wizard"
                self.stage+=1
                self.luluCruise()
        def uploadCover(self):
                print "uploading cover"
                r_cover_upload = self.execution(self.cover_upload,50,"text")
                r_cover_upload.send_keys(self.local.cFolder +"/"+"bcVolume&&&"+self.volume.num+".pdf")
                self.execution(self.c_up_button,80,"click")
                print "wait for upload to complete..."

                is_disabled = self.execution(self.next_disable,40,"find")
                if is_disabled is True:
                    self.execution(self.next_disable,10,"click")
                else:
                    is_helv_cover = self.execution("/html/body/div[1]/div[3]/div[1]/div/span",8,"find")
                    if is_helv_cover is True:
                        check_helv_cover = self.execution("/html/body/div[1]/div[3]/div[1]/div/span",8,"text").text
                        if "The Helvetica font is not embedded." in check_helv_cover:
                            print "helv cover error!"
                            self.local.immigration(self.volume, self.driver,True, "hevl error in cover")
                self.stage+=1
                self.luluCruise()
        def extra(self):
                self.execution(self.next,165,"click")
                self.stage+=1
                self.luluCruise()
        def pubOptions(self):
                if self.execution("//*[@id='category']",10,"find")==False:
                    sleep(3)
                catSelect=self.driver.find_element_by_xpath("//*[@id='category']")
                catOptions = catSelect.find_elements_by_tag_name("option")
                if upload_type=="reg":
                	pub_option = "Poetry"
                else:
                	pub_option = "Reference"
                for option in catOptions:
                        if option.text==pub_option:
                                print "found particular option in select"
                                option.click()
                                break
                print "keywords"
                r_keywords = self.execution(self.keywords,150,"text")
                r_keywords.send_keys("Poetry, Reference, Wikipedia, Mandiberg")
                r_desc = self.execution(self.description,100,"text")
                title_arr = self.local.tweeter.proportionally_shorten_strings(self.volume.title.split(" --- ")[0],self.volume.title.split(" --- ")[1],300)
                if upload_type == "reg":
                        desc_title = "Volume "+self.volume.num+", "+title_arr[0]+"---"+title_arr[1]+"\n\n"
                elif upload_type == "contrib":
                        desc_title = "Contributor Appendix: Volume "+self.volume.num+", "+title_arr[0]+" --- "+title_arr[1]+"\n\n"
                elif upload_type == "toc":
                        desc_title = "Table of Contents: Volume "+self.volume.num+", "+title_arr[0]+" --- "+title_arr[1]+"\n\n"

                r_desc.send_keys(unicode(desc_title+"Print Wikipedia is a both a utilitarian visualization of the largest accumulation of human knowledge and a poetic gesture towards the inhuman scale of big data. Michael Mandberg wrote software that parses the entirety of the English-language Wikipedia database and programmatically lays out nearly 7500 volumes, complete with covers, and then uploads them to Lulu.com for print-on-demand. Print Wikipedia draws attention to the sheer size of the encyclopedia's content and the impossibility of rendering Wikipedia as a material object in fixed form: Once a volume is printed it is already out of date. It is also a work of found poetry built on what is likely the largest appropriation ever made. As we become increasingly more dependent on information and source material on the Internet today, Mandiberg explores the accessibility of its vastness.", 'utf-8'))

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
                self.execution(self.next,50,"click")
                print 'set price now'
                if self.execution(self.setPrice,20,"find") is False:
                    sleep(3)
                r_price = self.driver.find_element_by_xpath(self.setPrice)# self.execution(self.setPrice,30,"text")
                r_price.send_keys(Keys.BACK_SPACE)
                r_price.send_keys(Keys.BACK_SPACE)
                r_price.send_keys(Keys.BACK_SPACE)
                r_price.send_keys(Keys.BACK_SPACE)
                r_price.send_keys(Keys.BACK_SPACE)
                r_price.send_keys(Keys.BACK_SPACE)
                r_price.send_keys(Keys.NUMPAD8)
                r_price.send_keys(Keys.NUMPAD0)
                self.execution(self.next,50,"click")
                self.stage+=1
                self.luluCruise()
        def review(self):
                print "almost there. just lemme review the order here"
                print "book " +self.volume.title+ " pushed to lulu okay."
                self.execution(self.next,50,"click")
                self.stage+=1
                self.luluCruise()
        def isbnNSku(self):
                lulu_isbn = self.execution(self.x_lulu_isbn,20,"text")
                self.volume.isbn = lulu_isbn.text
                self.execution(self.next,50,"click")
                print "go to the sale page to grab the SKU code."
                # sleep(300)
                # self.execution(self.x_sell,20,"click")
                self.volume.sku = "" #self.execution(self.sku_x,30,"text").get_attribute('value')
                print self.volume.sku +  " sku"
                print "begin next book at vol #"+self.volume.num
                self.local.immigration(self.volume, self.driver)

        def luluCruise(self):
                self.t_now = datetime.now()
                self.t_diff = self.t_now-self.t_start
                print str(self.t_diff) + " on stage " + str(self.stage)
                if int(self.volume.num) % 5 == 0:
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
                        self.extra()
                elif self.stage==10:
                        self.pubOptions()
                elif self.stage==11:
                        self.review()
                elif self.stage==12:
                        self.isbnNSku()         
                else:
                        errormsg = "stage not recognized."
                        self.emergencyMail(errormsg)
                        exit()
        
class Local:
        #for doing things in the local 
        def __init__(self, volume):
                self.volume = volume
                self.tweeter = Tweeter()
                def getFolders():#sets these vars.
                        self.pwd = os.path.abspath(os.getcwd())
                        self.outFolder= self.pwd+"/out"
                        self.inFolder= self.pwd+"/in"
                        self.cFolder = self.pwd+"/covers"
                getFolders()
        def immigration(self,volume,driver,i_fail=False,err_type=""):#move file from one folder to another. give next file over. This is done so there isn't such a long read for the 6k file run through.
                # print "get luluid to be put into json string and added to file."
                driver.quit()
                if i_fail==True:
                        helv_error = open("crasherrfiles.txt","a")
                        helv_error.write(self.volume.num+ " " + self.volume.title+" upload:"+ upload_type+" reason: "+err_type+"\n")
                        helv_error.close()
                else:
                        print "make string and append to 7-15-goodboy.json"

                        json_s = '{"lulu_id":"'+str(volume.fcid)+'","sku":"'+str(volume.sku)+'","volume":"'+str(volume.num)+'","name":"'+str(volume.title)+'"},'
                        print json_s
                        json_f = open("lastbit.json","a")
                        json_f.write(json_s)
                        json_f.close()
                        # self.tweeter.go_tweet(volume.num, volume.title, author_fn, author_ln, volume.sku)
                is_ignore=False
                nextCheck=int(volume.num)+1
                print "this is nextcheck before crashlist" + str(nextCheck)
                if nextCheck not in crash_list:
                    for i in crash_list:
                        if i > nextCheck:
                            nextCheck = i
                            if nextCheck<1400:
                                lulu_email = 'printwikipedia@printwikipedia.com'
                            elif nextCheck > 1400 and nextCheck < 2540:
                                lulu_email = 'printwikipedia+1@printwikipedia.com'
                            elif nextCheck > 2540 and nextCheck < 4420:
                                lulu_email = 'printwikipedia+2@printwikipedia.com'
                            elif nextCheck > 4420 and nextCheck < 5120:
                                lulu_email = 'printwikipedia+3@printwikipedia.com'
                            elif nextCheck > 5120 and nextCheck < 6280:
                                lulu_email = 'printwikipedia+4@printwikipedia.com'
                            elif nextCheck > 6280:
                                lulu_email = 'printwikipedia+5@printwikipedia.com'
                            break
                    volume.num = int(nextCheck)
                print "this is volnum " + str(volume.num)
                volume.num = int(volume.num)
                if volume.num not in crash_list:
                    exit()
                if volume.num % 20 == 0:   
                    print "this is %20!!!"
                    volume.round_folder=str('{0:04d}'.format(volume.num))+"/"
                    print volume.round_folder
                    print " ^^ expected to be the same as the nextcheck: " + str(nextCheck)
                else:
                    print "not %20"
                    volume.round_folder = volume.roundDown()
                print volume.round_folder + " this is roundfoul"
                #make list and then use .sort() to get through all of them save it elsewhere. and keep pulling the last one out.
                print self
                for i in os.listdir(self.inFolder+"/"+volume.round_folder):#find the next input_file
                        if i == '.DS_Store':
                                print "ignore .ds_store"
                                continue
                        if i.startswith("mod") or i.startswith("pre"):
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
                #if you can't find a next book thing...

                    # Browser(i)
        def ftpIt(self,file_name=False):
                if file_name is True:
                    print "sup ho i am so true."
                    try:
                       session = ftplib.FTP(ftp_host,lulu_email,lulu_pass)
                       session.cwd(self.volume.round_folder)
                       print self.inFolder+self.volume.round_folder+'/'+self.volume.input_file
                       #file = open(self.inFolder+self.volume.round_folder+'/'+self.volume.input_file,'rb') # file to send
                       if os.path.isfile(self.inFolder+self.volume.round_folder+'/'+self.volume.input_file):
                           file = open(self.inFolder+self.volume.round_folder+'/'+self.volume.input_file,'rb') # file to send
                       else:
                           file = open(self.inFolder+self.volume.round_folder+'/'+self.volume.input_file,'rb') # file to send
                       session.storbinary('STOR '+input_file, file) # send the file
                       file.close() # close file and FTP
                       session.quit()
                    except:
                       print "ftp mess up"
                       return False
                else:
                    try:
                        session = ftplib.FTP(ftp_host,lulu_email,lulu_pass)
                        session.cwd(self.volume.round_folder)
                        print self.inFolder+self.volume.round_folder+'/mod'+self.volume.num+'.pdf'
                        file = open(self.inFolder+self.volume.round_folder+'/mod'+self.volume.num+'.pdf','rb') # file to send
                        session.storbinary('STOR mod'+self.volume.num+".pdf", file) # send the file
                        file.close() # close file and FTP
                        session.quit()
                    except:
                        print "ftp mess up"
                        return False
                # except ftplib.error_temp:
                #     print "ftp temporary error"
                #     sleep(5)
                #     self.ftpIt
                # except ftplib.error_perm:
                #     print "goddam ftp error"
                #     self.immigration(self.volume, self.driver,True,"ftp error")

class Tweeter:
        #keys, tokens for @PrintWikipedia account
        consumer_key = "BhvUb8DUsRsrXh5ODEBt2VhQT"
        consumer_secret = "etbxqJI4pYlbFbHADpvdkx8G7SxDg058pa4pJYrIEDlNJJR7gn"
        access_token = "3219884864-AAPlg5nZvjJtbNTbYCMnY5FqJrTpCwylXnyLmll"
        access_token_secret = "rMJ35vSCi4jkU5SWbj6Ihe5mU5amGL9MebOPpLbzCUTOM"
        #basic authorization for read/write, see: http://tweepy.readthedocs.org/en/v3.3.0/auth_tutorial.html
        auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
        auth.set_access_token(access_token, access_token_secret)

        api = tweepy.API(auth) #wraps the api paths in handy functions

        def tweet(self, text):
                print "tweeting: " + text
                self.api.update_status(status=text) #tweet it

        def print_timeline(self): #for testing
                public_tweets = self.api.home_timeline()
                for tweet in public_tweets:
                        print tweet.text

        def shorten_title(self, title, diff):
                diff = int(diff)
                if diff > 0:
                        return title[:len(title)-(int(diff)+3)] + "..."
                else:
                        return title

        def proportionally_shorten_strings(self, str1, str2, max_length):
                out1 = ""
                out2 = ""
                len1 = len(str1)
                len2 = len(str2)
                if (len1 + len2) >= max_length:
                        diff = (len1 + len2) - max_length
                        #always small/big, always float
                        smallratio = (len1/float(len2)) if len1 <= len2 else (len2/float(len1))
                        #bigratio = (len1/float(len2)) if len1 > len2 else (len2/float(len1))
                        half_diff = math.ceil(diff/float(2))
                        small_chop = smallratio * half_diff
                        big_chop = half_diff + (half_diff - small_chop) + 1 #correct for odd/even
                        #big_chop = bigratio * half_diff
                        if len1 <= len2:
                                out1 = self.shorten_title(str1, small_chop)
                                out2 = self.shorten_title(str2, big_chop)
                        else:
                                out1 = self.shorten_title(str1, big_chop)
                                out2 = self.shorten_title(str2, small_chop)
                else:
                        out1 = str1
                        out2 = str2 
                #returns array of strings
                return [out1, out2]

        def format_text(self, num, title, fn, ln, sku): #format incoming object into a string for tweet
                #tweets should look like: Uploaded Volume XXXX, "From - To" http://URLHERE
                #http://www.lulu.com/shop/michael-mandiberg/product-22187323.html
                reload(sys)
                sys.setdefaultencoding('utf-8')
                title_from = title.split(" --- ")[0]
                title_to = title.split(" --- ")[1]
                config = self.api.configuration()
                #for testing, when we go over rate limit...
                #config = {'short_url_length' : 22}
                max_titles_length = 140 - (len('Uploaded Volume_XXXX,__-____') + config['short_url_length'])
                titles = self.proportionally_shorten_strings(title_from, title_to, max_titles_length)
                text_sans_url = ('Uploaded Volume %s, "%s - %s" ' % (num, titles[0], titles[1]))#.decode('latin-1')
                fn = re.sub(r'\s', '-', fn.lower())
                ln = re.sub(r'\s', '-', ln.lower())
                url = "http://www.lulu.com/shop/%s-%s/product-%s.html" % (fn, ln, sku)

                effective_length = len(text_sans_url) + config['short_url_length']
                #print effective_length
                text_array = [text_sans_url.encode('utf-8', errors="ignore"), url.encode('utf-8', errors="ignore")]
                text = ''.join(text_array)

                return text

        def go_tweet(self, num, title, fn, ln, sku):
                self.tweet(self.format_text(num, title, fn, ln, sku))

b = Browser(input_file)
