class Volume:
        # stores variables for the individual volume should be extended by browser.
        def __init__(self):
                self.fcid = False
        def roundDown(self,divisor=20):#to aid in looking for a particular folder since they are broken up by 20s. 4 digit number w/leading 0s
                fol = '{0:04d}'.format(int(self.num) - (int(self.num)%divisor))
                if(fol=="0000"):
                        return "/0001"
                return "/"+fol

        def travelAgent(self,input_file):
                print "splitting strings, encoding for unicode to make firefox happy. sending off file: " + input_file
                splitInput = input_file.split('&&&')#split on the dash symbol. to make a nice buncha strings
                self.num = splitInput[0]#this is the volume number we are currently on. it is a string not an int!
                self.title = splitInput[1]+" --- "+splitInput[2]
                self.input_file=unicode(input_file,'utf-8')
                self.round_folder = self.roundDown()