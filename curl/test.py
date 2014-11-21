def foo():
	i=0
	while i<40:
		print i
		if i==20:
			return
		i+=1
	print "hi still in foo"

foo()
print "fuck that foo"