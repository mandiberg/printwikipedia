import os


f = open('repeated_titles.txt','w')
k = open('all_titles.txt','w')
repeated_titles = []
pwd = os.path.abspath(os.getcwd())
in_folder = pwd + "/../../dist/output/"
in_items =  os.listdir(in_folder)
x=0
hit_counter = 0
is_matched=False

#print in_items
while x < len(in_items):
	item = in_items[x]
	#print item
	if item == ".DS_Store" or item[0] == "_":
		x=x+1
		continue
		
	splitFile = item.split('&&&')

	k.write(splitFile[0] + ", " + splitFile[1] + ", " + splitFile[2] + "\n")
	# print item
	# print splitFile[1][0:3] + "     " + splitFile[2][0:3]
	if splitFile[1][0:3] == splitFile[2][0:3]:
		if is_matched:
			hit_counter+=1
			x+=1 
			continue
		is_matched = True
		d= splitFile[1][0:3]
		hit_counter+=1
		print item +  " " + d
		
	elif is_matched and d!=splitFile[2][0:3] :	
		print "in elseif " + d 
		f.write(d+" " + str(hit_counter)+"\n")
		is_matched= False
		hit_counter=0
		


	x=x+1

#d = {y:repeated_titles.count(y) for y in repeated_titles}


f.close()
k.close()