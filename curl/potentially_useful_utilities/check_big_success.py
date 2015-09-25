#!/usr/bin/env python
# -*- coding: utf-8 -*-

# for checking a list of files against your successful json.
# instead of a too_big list, you could also use a range to see that all files within a certain range have been uploaded properly.
import json 
too_big = [] #list of files here.


with open('mycompleted.json') as data_file:    #handling the json and comparing the list<<<<< can be ignored if using range. see comments below
    data = json.load(data_file)
completed=[]
for i in data:
	completed.append(int(i['volume']))

#if you use range just do====== for x in range(0,20000): =====to iterate through each number.
for x in too_big:
	if x not in completed:
		print x
