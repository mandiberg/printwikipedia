import os


f = open('alltitles.txt','w')

pwd = os.path.abspath(os.getcwd())
in_folder = pwd + "/../../dist/output/"
in_items =  os.listdir(in_folder)
x=0
while x < len(in_items):
	item = in_items[x]
	if item == ".DS_Store" or item[0] == "_":
		continue
	splitFile = item.split('&&&')
	# print item
	f.write("vol: " + splitFile[0] + " " + splitFile[1] + " - " + splitFile[2] + " \n")

	x+=1
f.close()