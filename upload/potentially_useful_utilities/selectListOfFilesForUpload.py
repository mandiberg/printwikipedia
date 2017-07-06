import os
import shutil
# in_folder = "../../dist/output"
# in_folder = "../../dist/copyright"
in_folder = "../../dist/covers/output covers"
in_items =  os.listdir(in_folder)
files_needed = [42,61,109,110,111,112,113,114,115,223,226,227,228,229,230,231,231,232,233,234,235,236,237,238,239,240,241,242,243,244,249,274,275,276,277,278,301,302,303,304,305,306,307,308,309,356,357,358,359,440,442,508,509,510,511,515,516,517,520,670,738,739,740,741,742,743,744,745,746,747,780,840,918,930,952,994,1033,1033,1034,1035,1069,1089,1090,1091,1093,1094,1095,1097,1130,1131,1132,1133,1134,1135]

for item in in_items:
	if item == ".DS_Store" or item[0] == "_":
		continue
	# volume = int(item.split("&&&")[0])
	# volume = int(item.split(".")[0][3:7])
	volume = int(item.split("&&&")[1][0:4])
	if volume in files_needed:
		shutil.copy2(in_folder + "/" + item, "../covers/" + item)
 