import os
import ftplib
from Browser import Browser

class Local:
    #for doing things in the local filesystem.
    def __init__(self, volume, tweeter, IPass):
        self.const = IPass
        self.volume = volume
        self.tweeter = tweeter #twitter init here to tweet out once a file has successfully been written to the json file.
        def getFolders():#sets these vars.
                self.pwd = os.path.abspath(os.getcwd())
                self.outFolder= self.pwd+"/out"
                self.inFolder= self.pwd+"/in"
                self.cFolder = self.pwd+"/covers"
        getFolders()
    def immigration(self,volume,driver,i_fail=False,err_type=""): #end of one lulucruise and the start of new cruise.
        driver.quit()
        if i_fail==True: #if this failed then write about how.
                gen_error = open(self.const.fail_file,"a")
                gen_error.write(self.volume.num+ " " + self.volume.title+" upload:"+ self.const.upload_type+" reason: "+err_type+"\n")
                gen_error.close()
        else:
                print "make string and append to json1.txt" #write to json so you have a log of what's worked and can make a pretty site later :3
                json_s = '{"lulu_id":"'+str(volume.fcid)+'","sku":"'+str(volume.sku)+'","volume":"'+str(volume.num)+'","name":"'+str(volume.title)+'"},'
                print json_s
                json_f = open(self.const.success_file,"a")
                json_f.write(json_s)
                json_f.close()
        
        print "SKU!!!!!!  " 
        print str(volume.sku)
        #uncomment for tweeting.
        # self.tweeter.go_tweet(volume.num, volume.title, volume.sku)
        is_ignore=False
        nextCheck=int(volume.num)+1
        volume.num = int(volume.num)+1
        print "this is nextcheck before crashlist" + str(nextCheck)
        if len(self.const.crash_list)>0:
            if nextCheck not in self.const.crash_list:
                for i in self.const.crash_list:
                    if i > nextCheck:
                        nextCheck = i
                        print "make nextcheck i  " + str(i)
                        break
                volume.num = int(nextCheck)
            print "this is volnum " + str(volume.num)
            volume.num = int(volume.num)
            if volume.num not in self.const.crash_list:
                exit()
            print "nextcheck went to the next round_folder"
            tmp_fol = volume.roundDown()
            tmp_fol=tmp_fol.strip('/')
            tmp_fol=int(tmp_fol)
            # if nextCheck % 20 == 0:
            #     tmp_fol+=20
            volume.round_folder = str('{0:04d}'.format(tmp_fol))
            print volume.round_folder 

        else:
            volume.round_folder =  volume.roundDown()

        for i in os.listdir(self.inFolder+"/"+volume.round_folder): #find the next input_file
                if i == '.DS_Store':
                        print "ignore .ds_store"
                        continue
                if i.startswith("mod") or i.startswith("pre"):
                        continue
                splitFile = i.split('&&&')
                print splitFile
                nextDest = int(splitFile[0]) #also change that to an int to compare with nextcheck
                print nextDest
                print nextCheck
                if nextCheck == nextDest:
                        print "found it"
                        print i
                        self.const.input_file = i
                        #for changing the account after certain amount of volumes uploaded
                        if volume.num > 599:
                            self.const.lulu_email = ""
                            self.const.ftp_usr = ""
                        Browser(self.const,volume,self)
                        break

    def ftpIt(self): #ftp. couldn't get the ftp errors catching to catch properly.
        try:
            session = ftplib.FTP(self.const.ftp_host,self.const.lulu_email,self.const.lulu_pass)
            session.cwd(self.volume.round_folder)
            print self.volume.round_folder+'/pre'+self.volume.num+'.pdf'
            file = open(self.inFolder+self.volume.round_folder+'/pre'+self.volume.num+'.pdf','rb') # file to send
            session.storbinary('STOR pre'+self.volume.num+".pdf", file) # send the file
            file.close() # close file and FTP
            session.quit()
        except Exception, e:
            print "ftp mess up"
            print str(e)
            return False
        # except ftplib.error_temp:
        #     print ftplib.
        #     print "ftp temporary error"
        #     sleep(5)
        #     self.ftpIt
        # except ftplib.error_perm: 
        #     print "goddam ftp error"
        #     self.immigration(self.volume, self.driver,True,"ftp error")


class Volume:
    # stores variables for the individual volume should be extended by browser.
    def __init__(self,IPass):
            self.fcid = False
            self.const = IPass

    def roundDown(self,divisor=20):#to aid in looking for a particular folder since they are broken up by 20s. 4 digit number w/leading 0s
            fol = '{0:04d}'.format(int(self.num) - (int(self.num)%divisor))
            if(fol=="0000"):
                    return "/0001"
            return "/"+fol

    def travelAgent(self,input_file):
            print "splitting strings, encoding for unicode to make firefox happy. sending off file: " + self.const.input_file
            splitInput = self.const.input_file.split('&&&')#split on the dash symbol. to make a nice buncha strings
            self.num = splitInput[0]#this is the volume number we are currently on. it is a string not an int!
            self.title = splitInput[1]+" --- "+splitInput[2]
            if isinstance(self.const.input_file, unicode):
                self.input_file=self.const.input_file
            else:
                self.input_file=unicode(self.const.input_file,'utf-8')
            self.round_folder = self.roundDown()


