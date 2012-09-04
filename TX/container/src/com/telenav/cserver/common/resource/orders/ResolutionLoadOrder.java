/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * DeviceLoadOrder.java
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2010-11-24
 *
 */
public class ResolutionLoadOrder  extends LoadOrder 
{
	{
		setType("resolution");
	}
    
    private static final String RESOLUTION_PATTERN = "\\d+x\\d+(_\\d+x\\d+)*+";
    private static Category logger = Category.getInstance(ResolutionLoadOrder.class);
    
	
	/**
	 * get the attribute value from UserProfile
	 * 
	 * @param profile
	 * @return
	 */
	public String getAttributeValue(UserProfile profile, TnContext tnContext)
	{
	    return profile.getResolution();
	}
    
    public List<String> getAttributeValueList(UserProfile profile, TnContext tnContext)
    {
        List<String> list = new ArrayList<String>();
        String resolution = getAttributeValue(profile, tnContext);
        if (resolution != null)
            list.add(resolution);
        list.add("default");
        return list;
    }
    
    
    //get the folder which name is match the resolution pattern
    //  e.g. we need the folder device/ATT/400x480_480x400/common_bar
    //and there are some other folders exist
    //like device/ATT/320x480_480x320/common_bar 
    //and device/ATT/8900/common_bar 
    //and device/ATT/800x600_600x300/common_bar
    //will return the list <device/ATT/320x480_480x320/common_bar>
    public List<String> getResemblanceFullPath(String fileName, String configFileName, UserProfile profile, TnContext tnContext)
    {
        List<String> resemFileList = new ArrayList<String>();
        try
        {
            String attr = getAttributeValue(profile, tnContext);
            List<String> resolutionResemList = getResolutionResem(fileName, attr);
            String resolutionResem = getResemblance(resolutionResemList, attr);
            if (resolutionResem != null)
            {
                resemFileList.add(fileName.replaceFirst(attr, resolutionResem));
            }
        }
        catch(Exception e)
        {
           logger.fatal("getResemblance" ,e);
        }
       
        return resemFileList;
    }
    
    
    //get the resemblance which name is match the resolution pattern
    //e.g. we need a folder device/ATT/400x480_480x400/common_bar
    //and there are some other folders exist
    //like device/ATT/320x480_480x320/common_bar 
    //and device/ATT/8900/common_bar 
    //and device/ATT/800x600_600x300/common_bar
    //will return the list <320x480_480x320, 800x600_600x300>
    private List<String> getResolutionResem(String fileName, String attr)
    {
        List<String> resolutionResemList = new ArrayList<String>();
        String paraentFolder = getParaentFolder(fileName, attr);
        if (paraentFolder == null)
            return resolutionResemList;
        
        URL url = Thread.currentThread().getContextClassLoader().getResource(paraentFolder);
        if (url == null)
            return resolutionResemList;
        
        String absFile = url.getFile();
        
        File dir = new File(absFile);
        if (dir.isDirectory())
        {
            File[] files = dir.listFiles();
            for(File file:files )
            {
                if (file.isDirectory() && file.getName().matches(RESOLUTION_PATTERN))
                {
                    resolutionResemList.add(file.getName());
                }
            }
        }
        return resolutionResemList;
    }
    
    
    //get the paraent folder
    //e.g. there is a folder device/ATT/320x640_640x320/common_bar
    //will return the folder device/ATT/
    private String getParaentFolder(String fileName, String attr)
    {
        fileName = fileName.replaceFirst(attr, "\n");
        int idx = fileName.indexOf("\n");
        if (idx < 0)
            return null;
        String paraentFolder = fileName.substring(0, idx);
        
        return paraentFolder;
    }
    
    
    //get the resemblance one
    //the resolution is HxL and the closest one is H'xL'
    //then we need satisify the following condition
    //H'<H and L'<L and H'xL' is the largest one in the list
    //e.g. if we have a list <320x480_480x320, 800x600_600x300>
    //and the resolution is 400x480_480x400
    //will return the closest one: 320x480_480x320
    public static String getResemblance(List<String> list, String resolution)
    {
        if (list == null || list.size() == 0)
            return null;
        
        if (list.size() == 1)
            return list.get(0);
        
        String result = list.get(0);
        try
        {
            String[] actuality = resolution.split("_")[0].split("x");
            double width = Double.parseDouble(actuality[0]);
            double height = Double.parseDouble(actuality[1]);

            for(int i=1; i<list.size(); i++)
            {
                String str = list.get(i);
                String[] resemblance = str.split("_")[0].split("x");
                String[] last = result.split("_")[0].split("x");
                
                double currW = Double.parseDouble(resemblance[0]);
                double currH = Double.parseDouble(resemblance[1]);
                double lastW = Double.parseDouble(last[0]);
                double lastH = Double.parseDouble(last[1]);
                
                if (lastW>width || lastH>height)
                {
                    if (currW*currH<lastW*lastH )
                    {
                        result = str;
                        continue;
                    }
                }
                else if (currW<=width && currH<=height && currW*currH>lastW*lastH)
                {
                    result = str;
                    continue;
                }
            }
        }
        catch(Exception e)
        {
            logger.warn("getResemblance", e);
        }
        
        
        return result;
        
    }

}
