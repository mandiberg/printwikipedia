import os

pwd = os.path.abspath(os.getcwd())
in_folder = pwd + "/in"
in_items =  os.listdir(in_folder)
x=1
item = in_items[x]
splitFile = item.split('&&&')
vol_folder = splitFile[0]
os.mkdir(in_folder + "/"+vol_folder)
while(x < len(in_items)):
	item = in_items[x]
	if( x % 20 ==0 ):
		splitFile = item.split('&&&')
		vol_folder = splitFile[0]
		os.mkdir(in_folder + "/"+vol_folder)
	os.rename(in_folder+'/'+item, in_folder+"/"+vol_folder+"/"+item)
	x+=1