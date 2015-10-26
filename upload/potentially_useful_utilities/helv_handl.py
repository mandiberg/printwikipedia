#for moving files that are somehow damaged and need to be redistilled

import shutil
import os
reup_bigs = [] # list of bad files you got from crasherrfiles.
pwd = os.getcwd()
all_folders = os.listdir(pwd+"/in")
# print all_folders
for i in all_folders:
	if os.path.isfile(pwd+"/in/"+i):
		continue
	indv_fold = os.listdir(pwd+"/in/"+i)
	for x in indv_fold:
		if x[0:3].isdigit():
			print x
			tf = x.split("&&&")
			if int(tf[0]) in reup_bigs:
				print "yesm"
				shutil.copy2(pwd+"/in/"+i+"/"+x, "/some path u like")