#main
from Pass import IPass
from Local import Local, Volume
from Tweeter import Tweeter
from Browser import Browser
from RealPass import RealPass

p = RealPass()
ip = IPass(p)
v = Volume(ip)
t = Tweeter(ip)
l = Local(v,t,ip)
b = Browser(ip,v,l)