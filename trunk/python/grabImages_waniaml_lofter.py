# -*- coding: utf-8 -*-
"""
some function by metaphy,2007-04-03,copyleft
version 0.2
"""
import urllib, httplib, urlparse
import re
import random
import os
import thread  
import time  
import logging  
import threading  
logging.basicConfig(level=logging.DEBUG, format='%(asctime)s %(levelname)s %(message)s', filename='logThre32.txt', filemode='a+') 
logging.getLogger('').addHandler(logging.StreamHandler())

mylock = threading.RLock()  
"""judge url exists or not,by others"""
def httpExists(url):
    host, path = urlparse.urlsplit(url)[1:3]
    if ':' in host:
        # port specified, try to use it
        host, port = host.split(':', 1)
        try:
            port = int(port)
        except ValueError:
            logging.debug('invalid port number %r' % (port,))
            return False
    else:
        # no port specified, use default port
        port = None
    try:
        connection = httplib.HTTPConnection(host, port=port)
        connection.request("HEAD", path)
        resp = connection.getresponse()
        if resp.status == 200:  # normal 'found' status
            found = True
        elif resp.status == 302:  # recurse on temporary redirect
            found = httpExists(urlparse.urljoin(url, resp.getheader('location', '')))
        else:  # everything else -> not found
            logging.debug("Status %d %s : %s" % (resp.status, resp.reason, url))
            found = False
    except Exception, e:
        logging.debug(e.__class__, e, url)
        found = False
    return found

"""get html src,return lines[]"""
def gGetHtmlLines(url):
    if url == None : return
    if not httpExists(url): return 
    try:
        page = urllib.urlopen(url)   
        html = page.readlines()
        page.close()
        return html
    except:
        logging.debug("gGetHtmlLines() error!")
        return
"""get html src,return string"""
def gGetHtml(url):
    if url == None : return
    if not httpExists(url): return 
    try:
        page = urllib.urlopen(url)   
        html = page.read()
        page.close()
        return html
    except:
        print "gGetHtml() error!"
        logging.debug("gGetHtml() error!")
        return

"""根据url获取文件名"""
def gGetFileName(url):
    if url == None: return None
    if url == "" : return ""
    arr = url.split("/")
    return arr[len(arr) - 1]

"""生成随机文件名"""
def gRandFilename(type):
    fname = ''
    for i in range(16):
        fname = fname + chr(random.randint(65, 90))
        fname = fname + chr(random.randint(48, 57))
    return fname + '.' + type
"""根据url和其上的link，得到link的绝对地址"""
def gGetAbslLink(url, link):
    if url == None or link == None : return 
    if url == '' or link == '' : return url 
    addr = '' 
    if link[0] == '/' : 
        addr = gGetHttpAddr(url) + link 
    elif len(link) > 3 and link[0:4] == 'http':
        addr = link 
    elif len(link) > 2 and link[0:2] == '..':
        addr = gGetHttpAddrFatherAssign(url, link)
    else:
        addr = gGetHttpAddrFather(url) + link 

    return addr 

"""根据输入的lines，匹配正则表达式，返回list"""
def gGetRegList(linesList, regx):
    if linesList == None : return 
    rtnList = []
    for line in linesList:
        matchs = re.search(regx, line, re.IGNORECASE)
        if matchs != None:
            allGroups = matchs.groups()
            for foundStr in allGroups:
                if foundStr not in rtnList:
                    rtnList.append(foundStr)
    return rtnList
"""根据url下载文件，文件名参数指定"""
def gDownloadWithFilename(url, savePath, file):
    # 参数检查，现忽略
    logging.debug("downloading image.... " + url)
    try:
        urlopen = urllib.URLopener()
        fp = urlopen.open(url)
        # url decode
        logging.debug("file " + file)
        save = savePath + file
        if os.path.exists(save) and os.path.isfile(save):
            logging.debug(save + ' exist!')
            logging.debug("Cancel download !")
            fp.close()
            return
        logging.debug('save as [' + save + '] ...')
        
        data = fp.read()
        fp.close()
        file = open(savePath + file, 'w+b')
        file.write(data)
        file.close()
        logging.debug("download success!")
    except IOError, e:
        logging.debug("download error!" + str(e))
        
"""根据url下载文件，文件名自动从url获取"""
def gDownload(url, savePath):
    # 参数检查，现忽略
    fileName = gGetFileName(url)
    # fileName =gRandFilename('jpg')
    gDownloadWithFilename(url, savePath, fileName)
        
"""根据某网页的url,下载该网页的jpg"""
def gDownloadHtmlJpg(downloadUrl, savePath):
    lines = gGetHtmlLines(downloadUrl)
    regx = r"""src\s*="?(\S+)\.jpg"""
    lists = gGetRegList(lines, regx)
    if lists == None: return 
    sStr2 = "imgurl"
    for jpg in lists:
        jpg = gGetAbslLink(downloadUrl, jpg) + '.jpg'
        if jpg.find(sStr2) == -1:
            gDownload(jpg, savePath)
   # ##     print gGetFileName(jpg)
"""根据url取主站地址"""
def gGetHttpAddr(url):
    if url == '' : return ''
    arr = url.split("/")
    return arr[0] + "//" + arr[2]
"""根据url取上级目录"""
def gGetHttpAddrFather(url):
    if url == '' : return ''
    arr = url.split("/")
    addr = arr[0] + '//' + arr[2] + '/'
    if len(arr) - 1 > 3 :
        for i in range(3, len(arr) - 1):
            addr = addr + arr[i] + '/'
    return addr

"""根据url和上级的link取link的绝对地址"""
def gGetHttpAddrFatherAssign(url, link):
    if url == '' : return ''
    if link == '': return ''
    linkArray = link.split("/")
    urlArray = url.split("/")
    partLink = ''
    partUrl = ''
    for i in range(len(linkArray)):        
        if linkArray[i] == '..': 
            numOfFather = i + 1  # 上级数
        else:
            partLink = partLink + '/' + linkArray[i]
    for i in range(len(urlArray) - 1 - numOfFather):
        partUrl = partUrl + urlArray[i] 
        if i < len(urlArray) - 1 - numOfFather - 1 : 
            partUrl = partUrl + '/'
    return  partUrl + partLink

"""根据url获取其上的相关htm、html链接，返回list"""
def gGetHtmlLink(url):
    # 参数检查，现忽略
    rtnList = []
    lines = gGetHtmlLines(url)
    regx = r"""href="?(\S+)\.htm"""
    for link in gGetRegList(lines, regx):
        link = gGetAbslLink(url, link) + '.htm'
        if link not in rtnList:
            rtnList.append(link)
    return rtnList

def gGetPostLink(url):
    rtnList = []
    lines = gGetFileLines(url)
    regx = r"""href="?(/post/\S+)\""""
    for link in gGetRegList(lines, regx):
      
        link = "http://wanimal.lofter.com" + link
        if link not in rtnList:
            rtnList.append(link)
    return rtnList

def gGetFileLines(url):
    try:
        file = open(url)   
        html = file.readlines(1000000)
        file.close()
        return html
    except Exception , e:
        logging.debug("gGetFileLines() error!" + str(e))
        return
    
def gDownloadName(downloadUrl):
    lines = gGetHtmlLines(downloadUrl)
    regx = r"""<p>?([^<]*)</p>"""
    lists = gGetRegList(lines, regx)
    if lists == None: return 
    if len(lists) <= 0: return "null"
    return lists[0]

def gDownloadTime(downloadUrl):
    lines = gGetHtmlLines(downloadUrl)
    regx = r"""<a href=\"""" + downloadUrl + """\">?([^<]*)</a>"""
    lists = gGetRegList(lines, regx)
    if lists == None: return 
    return lists[0]



"""根据url，抓取其上的jpg和其链接htm上的jpg"""
def gDownloadAllJpg(url, savePath):
    # 参数检查，现忽略
    gDownloadHtmlJpg(url, savePath)
    # 抓取link上的jpg
    links = gGetHtmlLink(url)
    for link in links:
        gDownloadHtmlJpg(link, savePath)

"""test"""
def test():
    posts = gGetPostLink("c.html")
    i = 0
    x = 750
    length = str(len(posts))
    logging.debug("type:" + str(type(posts)))
    sss = set(posts)
    logging.debug("sss length:" + str(len(sss)))
    logging.debug(len(posts))
    ll = []
    for link in posts:
        i = i + 1
        logging.debug(str(i) + "/" + length + ":" + link)
#        if i < x or i >= x + 50 : continue
        time = gDownloadTime(link).strip()
        name = gDownloadName(link).strip()
        name = time + '_' + name
        name = name.replace('?', '')
        name = name.replace('&nbsp;', ' ')
        name = name.replace('&amp;', '&')
        name = name.replace(':', '')
        name = name.strip()
        save = unicode('c:/WANIMAL/' + name + '/')
        ll.append(save)
        logging.debug(save)
        if os.path.exists(save) and os.path.isdir(save):
            logging.debug(save + ' exist!')
            continue
        else:
            logging.debug(save + ' make dirs!')
            os.makedirs(save)
        logging.debug('download pic from [' + link + ']')
        logging.debug('save to [' + save + '] ...')
        gDownloadHtmlJpg(link, save)
        logging.debug("download finished! " + str(i) + "/" + length + ":" + link)
    logging.debug('All over')
    logging.debug(len(posts))
    logging.debug(len(set(ll)))
    return 
num = 0
def testThread():
    posts = gGetPostLink("c.html")
    i = 0
    length = len(posts)
    logging.debug(len(posts))
    saves = set([])
    dict = {};
    print dict
    x = 150
    interval = length / x
    print str(interval)
    j = 0
    while i < length:
        j = j + 1
        start = i
        end = i + interval
        print "start", start, "end", end
        thread1 = getImgsThread(posts[start:end], saves, dict, "Thread_" + str(j))  
        thread1.start()
        i = i + interval
    
    
    global num
    num = j
    while True :
        time.sleep(5)
        mylock.acquire()
        logging.debug('num:' + str(num) + '----========================--')
        if num > 0:
            mylock.release()
        else:
            mylock.release()
            break
    
    m = 0
    n = 0
    for s in dict:
        if len(dict[s]) > 1 :
            n = n + 1 
            m = m + len(dict[s])
            logging.debug('ssss:' + s + "====" + str(len(dict[s])) + "_" + str(dict[s]) + '----!!!!!!!--')
    logging.debug('m:' + str(m) + '----!!!!!!!--')
    logging.debug('n:' + str(n) + '----!!!!!!!--')
    return 

class getImgsThread(threading.Thread):

    def __init__ (self, m1, m2, m3, m4):
        threading.Thread.__init__(self)
        self.ps = m1
        self.ss = m2
        self.dic = m3
        self.name = m4

    def run(self):
        self.ps = set(self.ps)
        length = str(len(self.ps))
        i = 0;
        while True:
            if len(self.ps) > 0:
                link = self.ps.pop()
                i = i + 1
                time = gDownloadTime(link).strip()
                name = gDownloadName(link).strip()
                name = time + '_' + name
                name = name.replace('?', '')
                name = name.replace('&nbsp;', ' ')
                name = name.replace('&amp;', '&')
                name = name.replace(':', '')
                name = name.strip()
                save = unicode('c:/WANIMAL/' + name + '/')
                if save in self.ss:
                    logging.debug('save has in [' + save + ']---' + self.name)
                else:
                    self.ss.add(save)
                    self.dic[save] = []
                self.dic[save].append(link)
                logging.debug(save)
                if os.path.exists(save) and os.path.isdir(save):
                    logging.debug(save + ' exist! ---' + self.name)
                else:
                    logging.debug(save + ' make dirs!---' + self.name)
                    os.makedirs(save)
                logging.debug('download pic from [' + link + ']---' + self.name)
                logging.debug('save to [' + save + '] ...---' + self.name)
                gDownloadHtmlJpg(link, save)
                logging.debug("download finished! " + str(i) + "/" + length + ":" + link + " ---" + self.name)
            else:
                break;
        logging.debug('All over --- self.ss length :' + str(len(self.ss)) + '------' + self.name)
        mylock.acquire()
        global num
        num = num - 1
        mylock.release()
        return 

testThread()
