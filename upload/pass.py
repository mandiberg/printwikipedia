#!/usr/bin/env python
# -*- coding: utf-8 -*-


class IPass:
	def __init__(self, password):
		self.password = password
		self.lulu_email="blah@blah.com"
		self.lulu_pass = self.password.lulu_password
		self.input_file ="volume#&&&1stTitle&&&2ndTitle&&&.pdf"
		self.author_fn = ""
		self.author_ln = ""
		self.ftp_usr = self.lulu_email 	
		self.ftp_host = "ftpupload.lulu.com"
		self.make_log = False
		self.upload_type = "reg" #contrib/toc/reg
		self.sleep_multiplier = 0
		self.is_private = False
		self.success_file = "nl_success.json"
		self.fail_file = "nl_fail.txt"
		# if going through a list of volumes that are out of order, use crash list. fill with integers representative of the volume#s
		self.crash_list=[]

		# twitter
		self.pw_consumer_key = ""
		self.pw_consumer_secret = ""
		self.pw_access_token = ""
		self.pw_access_token_secret = ""
	