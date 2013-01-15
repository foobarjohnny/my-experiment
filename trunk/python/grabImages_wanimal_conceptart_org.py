# -*- coding: utf-8 -*-
"""
some function by metaphy,2007-04-03,copyleft
version 0.2
"""
import urllib, httplib, urlparse
import re
import random
import os
import logging  
import datetime
import time
from bs4 import BeautifulSoup
logging.basicConfig(level=logging.DEBUG, format='%(asctime)s %(levelname)s %(message)s', filename='log2.txt', filemode='a+') 

"""judge url exists or not,by others"""
def httpExists(url):
    host, path = urlparse.urlsplit(url)[1:3]
    if ':' in host:
        # port specified, try to use it
        host, port = host.split(':', 1)
        try:
            port = int(port)
        except ValueError:
            print 'invalid port number %r' % (port,)
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
            print "Status %d %s : %s" % (resp.status, resp.reason, url)
            found = False
    except Exception, e:
        print e.__class__, e, url
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
    except Exception, e: 
        print "gGetHtmlLines() error! Exception ==>>" + e 
        logging.debug("gGetHtmlLines() error! Exception ==>>" + e)
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
    except Exception, e: 
        print "gGetHtml() error! Exception ==>>" + e 
        logging.debug("gGetHtml() error! Exception ==>>" + e)
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
    try:
        urlopen = urllib.URLopener()
        fp = urlopen.open(url)
        data = fp.read()
        fp.close()
        file = open(savePath + file, 'w+b')
        file.write(data)
        file.close()
        print "download success!" + url
        logging.debug("download success!" + url)
    except IOError:
        print "download error!" + url
        logging.debug("download error!" + url)
        
"""根据url下载文件，文件名自动从url获取"""
def gDownload(url, savePath):
    # 参数检查，现忽略
    fileName = gGetFileName(url)
    # fileName =gRandFilename('jpg')
    gDownloadWithFilename(url, savePath, fileName)
        
"""根据某网页的url,下载该网页的jpg"""
def gDownloadHtmlJpg(imgUrls, savePath):
    lines = gGetHtmlLines(imgUrls)
    regx = r"""src\s*="?(\S+)\.jpg"""
    lists = gGetRegList(lines, regx)
    if lists == None: return 
    for jpg in lists:
        jpg = gGetAbslLink(imgUrls, jpg) + '.jpg'
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
            print link
    return rtnList

"""根据url，抓取其上的jpg和其链接htm上的jpg"""
def gDownloadAllJpg(url, savePath):
    # 参数检查，现忽略
    gDownloadHtmlJpg(url, savePath)
    # 抓取link上的jpg
    links = gGetHtmlLink(url)
    for link in links:
        gDownloadHtmlJpg(link, savePath)

def getPostAuthor(post):
    titleP = re.compile("(\s+?\w+)?username(\s+?\w+)?")
    a = post.find("a", attrs={"class":titleP});
    author = "null"
    strong = a.find("strong");
    if strong != None: author = strong.string;
    return author

def getPostTime(post):
    span = post.find("span", attrs={"class":"date"});
#    print "span", span
    postTime = span.text.strip()
    postTime = convertTime(postTime)
    postTime = postTime.strip()
#    print "time ", time
    return postTime

def getPost(postList):
    titleP = re.compile("(\s+?\w+)?postcontainer(\s+?\w+)?")
    a = postList.findAll("li", attrs={"class":titleP});
    return a

def getPostName(post):
    blockquote = post.find(attrs={"class":"postcontent restore"});
    name = blockquote.text.replace("\n", " ").replace("\r", " ").strip()
    return name

def getPostAttachImageUrls(post):
    imgs = post.findAll("img", attrs={"class":"attach"});
    rtnList = []
    for img in imgs:
        imgUrl = img["src"]
        imgUrl = "http://conceptart.org/forums/" + imgUrl
        rtnList.append(imgUrl)
    return rtnList

def gDownloadImgs(imgUrls, savePath):
    for imgUrl in imgUrls:
        gDownload2(imgUrl, savePath)
        
def gDownload2(url, savePath):
    # 参数检查，现忽略
    print "downloading.... " + url
    logging.debug("downloading.... " + url)
    try:
        urlopen = urllib.URLopener()
        fp = urlopen.open(url)
        disposition = fp.info().getheader('Content-disposition')
        file = gGetFileName2(disposition)
        print "file ", file
        logging.debug("file " + file)
        save = savePath + file
        if os.path.exists(save) and os.path.isfile(save):
            print save + ' exist!'
            logging.debug(save + ' exist!')
            print "Cancel download !" 
            logging.debug("Cancel download !")
            fp.close()
            return
        print 'save as [' + save + '] ...'
        logging.debug('save as [' + save + '] ...')
        
        data = fp.read()
        fp.close()
        file = open(savePath + file, 'w+b')
        file.write(data)
        file.close()
        print "download success!" 
        logging.debug("download success!")
    except IOError:
        print "download error!" 
        logging.debug("download error!")
    
def gGetFileName2(disposition):
    fileName = "null"
    regx = r"""filename=\"?(.+)\""""
    matchs = re.search(regx, disposition, re.IGNORECASE)
    if matchs != None:
        allGroups = matchs.groups()
        for foundStr in allGroups:
            fileName = foundStr
            break;
    return unicode(fileName.strip())

def convertTime(s):
#    s = "December 3rd, 2006, 08:20 AM"
    s = s.replace("2nd", "2")
    s = s.replace("th", "")
    s = s.replace("3rd", "3")
    s = s.replace("1st", "1")
    ch = s.split(",")
    s = ch[0] + " " + ch[1]
    FROM_FORMAT = '%B %d %Y' 
    TO_FORMAT = "%Y-%m-%d"
    d = datetime.datetime.strptime(s, FROM_FORMAT)
    time = d.strftime(TO_FORMAT)
    return time

"""test"""

def optimizeName(folderName):
    if folderName != None:  
        folderName = folderName.replace('?', '').replace('"', '').replace('>', '').replace('<', '').replace('|', '').replace('\\', '')
        folderName = folderName.replace('/', '').replace('*', '').replace('&nbsp;', ' ').replace('&amp;', '&').replace(':', '').strip()
        if len(folderName) > 150:
                     folderName = (folderName[0:149]).strip()
    return folderName

def test():
    start = 64
    end = 63
    for i in range(start, end, -1):
        print "page ", i
        logging.debug("page " + str(i))
        url = "http://conceptart.org/forums/showthread.php?83464-Wanimal-s-NSFW/page" + str(i)
        print "url ", url
        
        logging.debug("url " + url)
        start = time.time()
        doc = urllib.urlopen(url)
        elapsed = (time.time() - start) 
        print "open url : " + str(elapsed) + " s"
        logging.debug("open url : " + str(elapsed) + " s")
        
        
        start = time.time()
        soup = BeautifulSoup(doc)
        elapsed = (time.time() - start) 
        print "Build BeautifulSoup: " + str(elapsed) + " s"
        logging.debug("Build BeautifulSoup: " + str(elapsed) + " s")
        doc.close();
        
        start = time.time()
        postList = soup.find("div", {"id":"postlist"},);
        elapsed = (time.time() - start) 
        print "Get PostList: " + str(elapsed) + " s"
        logging.debug("Get PostList: " + str(elapsed) + " s")
#        print (len(postList))
    #    print "postList=",postList;
        start = time.time()
        posts = getPost(postList);
        elapsed = (time.time() - start) 
        print "Get Post: " + str(elapsed) + " s"
        logging.debug("Get Post: " + str(elapsed) + " s")
        
        length = len(posts)
        print "length ", length
        logging.debug("length " + str(length))
        
        j = 0
        for post in posts:
                j = j + 1
                print "page: " + str(i) + " post: " + str(j) + "/" + str(length)
                author = getPostAuthor(post)
#                print "author ", author
                if author != "wanimal": 
                    continue
                postTime = getPostTime(post)
                print "postTime ", postTime
                name = getPostName(post)
                print "name ", name
                folderName = 'P' + str(i) + "_" + postTime + '_' + name
                folderName = optimizeName(folderName)
                save = unicode('c:/WANIMAL_conceptart.org/' + folderName + '/')
                imgUrls = getPostAttachImageUrls(post)
    #            print "imgUrls ", imgUrls
#                print "save: ", save
                if os.path.exists(save) and os.path.isdir(save):
                    print save + ' exist!'
                    logging.debug(save + ' exist!')
                else:
                    print save + ' make dirs!'
                    logging.debug(save + ' make dirs!')
                    os.makedirs(save)
                print 'save to [' + save + '] ...'
                logging.debug('save to [' + save + '] ...')
                gDownloadImgs(imgUrls, save)
        print "page ", i, " finished"
        logging.debug("page " + str(i) + " finished")
    print "All over"
    logging.debug("All over")
test()                         
