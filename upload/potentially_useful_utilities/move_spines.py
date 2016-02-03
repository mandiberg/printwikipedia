# michael, on line 22, starting_from you will set your starting point. 
# line 23, ending_at is your ending point. 
# line 43, iterator is how many you want per folder.
# this file should sit right outside another folder called spines. run from terminal in whatever directory.


import os
def roundDown(num):#returns a string of closest rounded down with 4 leading zeros
    fol = '{0:04d}'.format(int(num))
    print fol + " before"
    if(fol=="0000"):
        return "/0001"
    print fol
    return "/"+fol


pwd = os.path.abspath(os.getcwd())#current directory you are running this from
in_folder = pwd + "/spines"#your spines are in the folder next to this program
in_items =  os.listdir(in_folder)


starting_from = 43#this number is whatever you want to start from
ending_at     = 200

starting_from=starting_from-1
x = starting_from
item = in_items[starting_from]
splitFile = item.split('&&&')
numof = splitFile[1].split('.')
numof = numof[0]

#make the first folder.
vol_folder = roundDown(numof)
print vol_folder 

os.mkdir(in_folder + "/"+roundDown(numof))
iterator = 0
while x < len(in_items) and x < ending_at:

	item = in_items[x]
	if item == ".DS_Store":
		continue
	if iterator==20:
		iterator = 0
		splitFile = item.split('&&&')
		numof = splitFile[1].split('.')
		numof = numof[0]
		
		vol_folder = roundDown(numof)
		# print item + " i make folder: "+ vol_folder
		os.mkdir(in_folder + vol_folder)
	os.rename(in_folder+'/'+item, in_folder+vol_folder+"/"+item)
	# print(in_folder+'/'+item, in_folder+vol_folder+"/"+item)

	x+=1
	iterator+=1