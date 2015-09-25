#use this file with cron in order to check if things are being uploaded correctly.
# this checks twitter to see if there has been a tweet <30 minutes ago.

import tweepy
from datetime import datetime, timedelta
import smtplib
consumer_key = "BhvUb8DUsRsrXh5ODEBt2VhQT"
consumer_secret = "etbxqJI4pYlbFbHADpvdkx8G7SxDg058pa4pJYrIEDlNJJR7gn"
access_token = "3219884864-AAPlg5nZvjJtbNTbYCMnY5FqJrTpCwylXnyLmll"
access_token_secret = "rMJ35vSCi4jkU5SWbj6Ihe5mU5amGL9MebOPpLbzCUTOM"
#basic authorization for read/write, see: http://tweepy.readthedocs.org/en/v3.3.0/auth_tutorial.html
auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)
api = tweepy.API(auth) #wraps the api paths in handy functions
roll = api.home_timeline()
print roll[0].text
last_at = roll[0].created_at
now = datetime.now()# + timedelta(hours=4)
diff = now - last_at
#print last_at
#print now
#print diff
#print timedelta(minutes=30)
# if 1==1:#put in place for testing. use above line.
if diff > timedelta(minutes=30):
    sender = 'uffablabtest@gmail.com'
    receivers = ['example@ualreadyknoboiiiiiii.biz']

    message = """check pwiki!!!

    this is last tweet text:

    """
    message+=roll[0].text
    print "yo this is something newer than 30 minutes"
    try:
       smtpObj = smtplib.SMTP('smtp.gmail.com:587')
       smtpObj.ehlo();
       smtpObj.starttls();
       smtpObj.login('bacn@mandiberg.com', 'fltn@brdg')
       smtpObj.sendmail(sender, receivers, message)
       print "Successfully sent email"
       smtpObj.quit()
    except smtplib.SMTPException:
       print "Error: unable to send email"
else:
    print "not that long!"
