import tweepy
import sys
import re
import math
class Tweeter:
    def __init__(self,IPass):
    #keys, tokens for @PrintWikipedia account
        self.const = IPass
        consumer_key = self.const.pw_consumer_key
        consumer_secret = self.const.pw_consumer_secret
        access_token = self.const.pw_access_token
        access_token_secret = self.const.pw_access_token_secret

    #basic authorization for read/write, see: http://tweepy.readthedocs.org/en/v3.3.0/auth_tutorial.html
        self.auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
        self.auth.set_access_token(access_token, access_token_secret)

        self.api = tweepy.API(self.auth) #wraps the api paths in handy functions

    def tweet(self, text):
            print "tweeting: " + text
            self.api.update_status(status=text) #tweet it

    def print_timeline(self): #for testing
            public_tweets = self.api.home_timeline()
            for tweet in public_tweets:
                    print tweet.text

    def shorten_title(self, title, diff):
            diff = int(diff)
            if diff > 0:
                    return title[:len(title)-(int(diff)+3)] + "..."
            else:
                    return title

    def proportionally_shorten_strings(self, str1, str2, max_length):
            out1 = ""
            out2 = ""
            len1 = len(str1)
            len2 = len(str2)
            if (len1 + len2) >= max_length:
                    diff = (len1 + len2) - max_length
                    #always small/big, always float
                    smallratio = (len1/float(len2)) if len1 <= len2 else (len2/float(len1))
                    #bigratio = (len1/float(len2)) if len1 > len2 else (len2/float(len1))
                    half_diff = math.ceil(diff/float(2))
                    small_chop = smallratio * half_diff
                    big_chop = half_diff + (half_diff - small_chop) + 1 #correct for odd/even
                    #big_chop = bigratio * half_diff
                    if len1 <= len2:
                            out1 = self.shorten_title(str1, small_chop)
                            out2 = self.shorten_title(str2, big_chop)
                    else:
                            out1 = self.shorten_title(str1, big_chop)
                            out2 = self.shorten_title(str2, small_chop)
            else:
                    out1 = str1
                    out2 = str2 
            #returns array of strings
            return [out1, out2]

    def format_text(self, num, title, sku): #format incoming object into a string for tweet
            #tweets should look like: Uploaded Volume XXXX, "From - To" http://URLHERE
            #http://www.lulu.com/shop/michael-mandiberg/product-22187323.html
            if self.const.upload_type == "reg":
                    mtl_text = "Uploaded Volume_XXXX,__-____ "
                    wo_link = "Volume"
            elif self.const.upload_type == "contrib":
                    mtl_text = "Uploaded Contributor Appendix_XXXX,__-____"
                    wo_link = "Contributor Appendix"
            elif self.const.upload_type == "toc":
                    mtl_text = "Uploaded Table of Contents_XXXX,__-____"
                    wo_link = "Table of Contents"
            reload(sys)
            sys.setdefaultencoding('utf-8')
            title_from = title.split(" --- ")[0]
            title_to = title.split(" --- ")[1]
            config = self.api.configuration()
            #for testing, when we go over rate limit...
            #config = {'short_url_length' : 22}
            max_titles_length = 140 - (len(mtl_text) + config['short_url_length'])
            titles = self.proportionally_shorten_strings(title_from, title_to, max_titles_length)
            text_sans_url = ('Uploaded '+wo_link+' %s, "%s - %s" ' % (num, titles[0], titles[1]))#.decode('latin-1')
            fn = re.sub(r'\s', '-', self.const.author_fn.lower())
            ln = re.sub(r'\s', '-', self.const.author_ln.lower())
            url = "http://www.lulu.com/shop/%s-%s/product-%s.html" % (fn, ln, sku)

            effective_length = len(text_sans_url) + config['short_url_length']
            #print effective_length
            text_array = [text_sans_url.encode('utf-8', errors="ignore"), url.encode('utf-8', errors="ignore")]
            text = ''.join(text_array)

            return text

    def go_tweet(self, num, title, sku):
            print "CHIRP CHIRP"
            self.tweet(self.format_text(num, title, sku))