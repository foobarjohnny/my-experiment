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
logging.basicConfig(level=logging.DEBUG,  format='%(asctime)s %(levelname)s %(message)s', filename='log.txt',  filemode='a+') 

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
        resp = connection.getresponse( )
        if resp.status == 200:       # normal 'found' status
            found = True
        elif resp.status == 302:     # recurse on temporary redirect
            found = httpExists(urlparse.urljoin(url,resp.getheader('location', '')))
        else:                        # everything else -> not found
            print "Status %d %s : %s" % (resp.status, resp.reason, url)
            found = False
    except Exception, e:
        print e.__class__, e, url
        found = False
    return found

"""get html src,return lines[]"""
def gGetHtmlLines(url):
    if url==None : return
    if not httpExists(url): return 
    try:
        page = urllib.urlopen(url)   
        html = page.readlines()
        page.close()
        return html
    except:
        print "gGetHtmlLines() error!"
        return
"""get html src,return string"""
def gGetHtml(url):
    if url==None : return
    if not httpExists(url): return 
    try:
        page = urllib.urlopen(url)   
        html = page.read()
        page.close()
        return html
    except:
        print "gGetHtml() error!"
        logging.debug( "gGetHtml() error!")
        return

"""����url��ȡ�ļ���"""
def gGetFileName(url):
    if url==None: return None
    if url=="" : return ""
    arr=url.split("/")
    return arr[len(arr)-1]

"""��������ļ���"""
def gRandFilename(type):
    fname = ''
    for i in range(16):
        fname = fname + chr(random.randint(65,90))
        fname = fname + chr(random.randint(48,57))
    return fname + '.' + type
"""����url�����ϵ�link���õ�link�ľ��Ե�ַ"""
def gGetAbslLink(url,link):
    if url==None or link == None : return 
    if url=='' or link=='' : return url 
    addr = '' 
    if link[0] == '/' : 
        addr = gGetHttpAddr(url) + link 
    elif len(link)>3 and link[0:4] == 'http':
        addr =  link 
    elif len(link)>2 and link[0:2] == '..':
        addr = gGetHttpAddrFatherAssign(url,link)
    else:
        addr = gGetHttpAddrFather(url) + link 

    return addr 

"""���������lines��ƥ��������ʽ������list"""
def gGetRegList(linesList,regx):
    if linesList==None : return 
    rtnList=[]
    for line in linesList:
        matchs = re.search(regx, line, re.IGNORECASE)
        if matchs!=None:
            allGroups = matchs.groups()
            for foundStr in allGroups:
                if foundStr not in rtnList:
                    rtnList.append(foundStr)
    return rtnList
"""����url�����ļ����ļ�������ָ��"""
def gDownloadWithFilename(url,savePath,file):
    #������飬�ֺ���
    try:
        urlopen=urllib.URLopener()
        fp = urlopen.open(url)
        data = fp.read()
        fp.close()
        file=open(savePath + file,'w+b')
        file.write(data)
        file.close()
        print "download success!"+ url
        logging.debug( "download success!"+ url)
    except IOError:
        print "download error!"+ url
	logging.debug( "download error!"+ url)
        
"""����url�����ļ����ļ����Զ���url��ȡ"""
def gDownload(url,savePath):
    #������飬�ֺ���
    fileName = gGetFileName(url)
    #fileName =gRandFilename('jpg')
    gDownloadWithFilename(url,savePath,fileName)
        
"""����ĳ��ҳ��url,���ظ���ҳ��jpg"""
def gDownloadHtmlJpg(downloadUrl,savePath):
    lines= gGetHtmlLines(downloadUrl)
    regx = r"""src\s*="?(\S+)\.jpg"""
    lists =gGetRegList(lines,regx)
    if lists==None: return 
    for jpg in lists:
        jpg = gGetAbslLink(downloadUrl,jpg) + '.jpg'
        gDownload(jpg,savePath)
   ###     print gGetFileName(jpg)
"""����urlȡ��վ��ַ"""
def gGetHttpAddr(url):
    if url== '' : return ''
    arr=url.split("/")
    return arr[0]+"//"+arr[2]
"""����urlȡ�ϼ�Ŀ¼"""
def gGetHttpAddrFather(url):
    if url=='' : return ''
    arr=url.split("/")
    addr = arr[0]+'//'+arr[2]+ '/'
    if len(arr)-1>3 :
        for i in range(3,len(arr)-1):
            addr = addr + arr[i] + '/'
    return addr

"""����url���ϼ���linkȡlink�ľ��Ե�ַ"""
def gGetHttpAddrFatherAssign(url,link):
    if url=='' : return ''
    if link=='': return ''
    linkArray=link.split("/")
    urlArray = url.split("/")
    partLink =''
    partUrl = ''
    for i in range(len(linkArray)):        
        if linkArray[i]=='..': 
            numOfFather = i + 1    #�ϼ���
        else:
            partLink = partLink + '/'  + linkArray[i]
    for i in range(len(urlArray)-1-numOfFather):
        partUrl = partUrl + urlArray[i] 
        if i < len(urlArray)-1-numOfFather -1 : 
            partUrl = partUrl + '/'
    return  partUrl + partLink

"""����url��ȡ���ϵ����htm��html���ӣ�����list"""
def gGetHtmlLink(url):
    #������飬�ֺ���
    rtnList=[]
    lines=gGetHtmlLines(url)
    regx = r"""href="?(\S+)\.htm"""
    for link in gGetRegList(lines,regx):
        link = gGetAbslLink(url,link) + '.htm'
        if link not in rtnList:
            rtnList.append(link)
            print link
    return rtnList

"""����url��ץȡ���ϵ�jpg��������htm�ϵ�jpg"""
def gDownloadAllJpg(url,savePath):
    #������飬�ֺ���
    gDownloadHtmlJpg(url,savePath)
    #ץȡlink�ϵ�jpg
    links=gGetHtmlLink(url)
    for link in links:
        gDownloadHtmlJpg(link,savePath)

"""test"""
def test():
	map = {'1'   : 666,
          '2'   : 164,
          '3' : 1246,
          '4'   : 36,
          '5'   : 155,
          '6'   : 31
	}
	map2 = {'1'   : 217,
          '2'   : 1,
          '3' : 1,
          '4'   : 1,
          '5'   : 1,
          '6'   : 1
	}
	for j in range(1, 7):
		id = bytes(j)
		end = map[id]
		start = map2[id]
		print "gallery_id:" + id
		print "gallery page:"
		print "start:" + bytes(start)
		print "end:" + bytes(end)
		
		logging.debug("gallery_id:" + id)
		logging.debug("gallery page:")
		logging.debug("start:" + bytes(start))
		logging.debug("end:" + bytes(end))
		for i in range(start, end):
			print i
			page = bytes(i)
			u='http://www.fappyleaks.com/gallery/?gallery_id=' + id + '&gallery_page='+ page
			save='i:/fappyleaks/'+ id + '/' + page + '/'
			print save
			logging.debug(save)
			print u
			logging.debug(u)
			if os.path.exists(save) and os.path.isdir(save):
				print save + ' exist!'
				logging.debug(save + ' exist!')
			else:
				print save + ' make dirs!'
				logging.debug(save + ' make dirs!')
				os.makedirs(save)
			print 'download pic from [' + u +']'
			logging.debug('download pic from [' + u +']')
			print 'save to [' +save+'] ...'
			logging.debug('save to [' +save+'] ...')
			gDownloadHtmlJpg(u,save)
			print "download finished! " + "u"
			logging.debug("download finished! " + "u")
		else:
			print 'The gallery is over ' + id
			logging.debug( 'The gallery is over ' + id)
	else:
		print 'all is over'
		logging.debug('all is over')
    
test()
