#this was for copying specific pre files from the copyright directory to the curl/in folder.

import os 
import shutil
cwd = os.getcwd()
cpr = cwd+"/../dist/copyright/"
crash_list = [] #change this as you need.
fs = os.listdir(cpr)
print fs
for x in fs:
	if x == ".DS_Store" or "mod" in x:
		continue
	f = x.split('pre')[1].split('.')[0]
	if int(f) in crash_list:
		shutil.copy2(cpr+x,cwd+"/in")