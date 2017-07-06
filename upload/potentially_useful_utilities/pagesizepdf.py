
import os
import pyPdf

f = open('pg_count.txt','w')

pwd = os.path.abspath( os.getcwd() )
in_folder = pwd + "/../../dist/output/"
in_items =  os.listdir(in_folder)
x=1
while x < len(in_items):
	reader = pyPdf.PdfFileReader(open(in_folder+in_items[x]))
	if reader.getNumPages() > 714:
		print in_items[x]
		f.write(in_items[x] + "\n")
	x+=1

f.close()