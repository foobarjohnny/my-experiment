# -*- coding: utf-8 -*-
# 导入smtplib和MIMEText
import smtplib
import os
import sys
import mimetypes
import logging  
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from email.mime.image import MIMEImage
from email.message import Message



logging.basicConfig(level=logging.DEBUG, format='%(asctime)s %(levelname)s %(message)s', filename='log_email.txt', filemode='a+') 
logging.getLogger('').addHandler(logging.StreamHandler())
#############
# 要发给谁，这里发给2个人
mailto_list = ["godlikewho1@gmail.com"]
mailto_list2 = ["godlikewho1@gmail.com", "dysong@telenav.cn"]
#####################
# 设置服务器，用户名、口令以及邮箱的后缀
mail_host = "smtp.126.com"
mail_user = "godlikewho"
mail_pass = "godlikewho1"
mail_postfix = "126.com"
me = mail_user + "126<" + mail_user + "@" + mail_postfix + ">"
port = "465"
osCodePage = "gbk"
######################
def send_mail(to_list, sub, contents):
    '''
    to_list:发给谁
    sub:主题
    content:内容
    send_mail("aaa@126.com","sub","content")
    '''
    
    
    try:
        msg = MIMEMultipart()
        msg['Subject'] = sub
        msg['From'] = me
        msg['To'] = ";".join(to_list)
        
        for content in contents:
            f = open("D:/sdy-experiment.googlecode.com/svn/trunk/python" + "/" + content, 'rb')
            msgText = MIMEText(f.read())
            f.close()
            msgText.add_header('Content-Disposition', 'attachment', filename=content)
            msg.attach(msgText)
        
        s = smtplib.SMTP_SSL()
        s.set_debuglevel(True)
        s.connect(mail_host, port)
        s.login(mail_user, mail_pass)
        s.sendmail(me, to_list, msg.as_string())
        s.close()
        return True
    except Exception, e:
        print str(e)
        logging.debug(str(e))
        return False
    
def get_smtp():
    s = None
    try:
        s = smtplib.SMTP_SSL()
#        s.set_debuglevel(True)
        s.connect(mail_host, port)
        s.login(mail_user, mail_pass)
        return s
    except Exception, e:
        logging.debug('Exception in get_smtp' + str(e)) 
        return None
    
def send_email_smtp(s, me, to_list, msg):
    try:
        s.sendmail(me, to_list, msg.as_string())
        return True
    except Exception, e:
        logging.debug('Exception in send_email_smtp' + str(e)) 
        return False

def get_email_msg(sub, content, picfiles , directory):
    msg = None
    try:
        # 创建实例,构造MIMEMultipart对象做为根容器
        msg = MIMEMultipart()
        msg['Subject'] = sub.decode(osCodePage).encode('utf-8')
        msg['From'] = me
        msg['To'] = ";".join(mailto_list)
        # 构造MIMEText对象做为邮件显示内容并附加到根容器
        txt = MIMEText(content, 'plain', 'utf-8')
        msg.attach(txt)
        logging.debug("msg : " + msg.as_string())
        for file in picfiles:
            f = open(directory + "/" + file, 'rb')
            img = MIMEImage(f.read())
            img.add_header('Content-Disposition', 'attachment', filename=file)
            f.close()
            msg.attach(img)
    except Exception, e:
        logging.debug('Exception in get_email_msg' + str(e)) 
        return None
    return msg

def get_pic_files(directory):
    picFiles = []
    for filename in os.listdir(directory):
        path = os.path.join(directory, filename)
        if not os.path.isfile(path):
            continue
        # Guess the content type based on the file's extension.  Encoding
        # will be ignored, although we should check for simple things like
        # gzip'd or compressed files.
        ctype, encoding = mimetypes.guess_type(path)
        if ctype is None or encoding is not None:
            # No guess could be made, or the file is encoded (compressed), so
            # use a generic bag-of-bits type.
            ctype = 'application/octet-stream'
        maintype, subtype = ctype.split('/', 1)
        if (maintype == 'image'):
            picFiles.append(filename)
    return picFiles

def get_subjects(directory):
    subjects = []
    for filename in os.listdir(directory):
        path = os.path.join(directory, filename)
        if not os.path.isdir(path):
            continue
        subjects.append(filename)
    return subjects
    
def send_images(directory, sucs):
    smtpClient = None
    fileHandle = None
    try:
        subjects = get_subjects(directory)
        completedList = getCompletedList()
        fileHandle = getCompletedTxt()
        length = len(subjects)
        logging.debug("Folder Count:" + str(length))
        i = 0
        for subject in subjects:
            i = i + 1
            if subject + "\n" in completedList:
                logging.debug(str(i) + "/" + str(length) + ":" + subject.decode(osCodePage) + " has been sent before!")
                continue
            smtpClient = get_smtp();
            logging.debug(str(i) + "/" + str(length) + ":" + subject.decode(osCodePage))
            pics = get_pic_files(directory + "/" + subject)
            msg = get_email_msg("WANIMAL LOFTER_" + subject, subject, pics , directory + "/" + subject)
            logging.debug("Sending....")
            result = send_email_smtp(smtpClient, me, mailto_list, msg)
            if result == True:
                logging.debug("Sent successfully")
                sucs.append(subject)
                completedList.append(subject + "\n")
                fileHandle.write(subject + "\n")
                fileHandle.flush()
            else:
                logging.debug("Sent failed")
            logging.debug(str(i) + "/" + str(length) + ":" + subject.decode(osCodePage) + " finished!")
            smtpClient.close()
        logging.debug("All over:" + str(length))
        return False
    except Exception, e:
        logging.debug('Exception in send_images' + str(e)) 
        fileHandle.close()
        smtpClient.close()
        return False
        
    
completedTxt = "D:/sdy-experiment.googlecode.com/svn/trunk/python/WANIMAL_LOFTER_EMAILED_completed.txt"
def getCompletedList():
    lines = []
    try:
        f = open(completedTxt, 'rb')
        lines = f.readlines()
        f.close()
    except Exception, e:
        logging.debug('Exception in getCompletedList()' + str(e)) 
    return lines

def getCompletedTxt():
    try:
        f = open(completedTxt, 'ab')
        return f
    except Exception, e:
        logging.debug('Exception in getCompletedTxt()' + str(e)) 
        return None

    
if __name__ == '__main__':
    directory = "c:/WANIMAL_LOFTER/"
    sucs = []
    send_images(directory, sucs)
    print "SUC FOLDERS:" + str(sucs)
    logging.debug("SUC FOLDERS:" + str(sucs).decode(osCodePage))
    if send_mail(mailto_list2, "email_logs_WANIMAL_LOFTER", ["log_email.txt", "WANIMAL_LOFTER_EMAILED_completed.txt"]):
        print "发送成功"
        logging.debug("发送成功")
    else:
        print "发送失败"
        logging.debug("发送失败")
