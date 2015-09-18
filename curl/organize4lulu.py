import os
def roundDown(num,divisor=20):#returns a string of closest rounded down with 4 leading zeros if possible.
    fol = '{0:04d}'.format(int(num) - (int(num)%divisor))
    print fol + " befire"
    if(fol=="0000"):
        return "/0001"
    print fol
    return "/"+fol

pwd = os.path.abspath(os.getcwd())
in_folder = pwd + "/in"
in_items =  os.listdir(in_folder)
x=0
item = in_items[x]
splitFile = item.split('&&&')
vol_folder = splitFile[0]
os.mkdir(in_folder + "/"+vol_folder)
x+=1
while x < len(in_items):
	item = in_items[x]
	if item == ".DS_Store":
		continue
	tmpspl = item.split('&&&')

	splitFile = item.split('&&&')
	vol_folder = roundDown(splitFile[0])
	if os.path.isdir(in_folder+vol_folder) is False:
		print item + " i make folder: "+ vol_folder
		os.mkdir(in_folder + vol_folder)
	os.rename(in_folder+'/'+item, in_folder+vol_folder+"/"+item)
	os.rename(in_folder+'/'+"pre"+str(tmpspl[0])+".pdf", in_folder+vol_folder+"/"+"pre"+str(tmpspl[0])+".pdf")
	x+=1