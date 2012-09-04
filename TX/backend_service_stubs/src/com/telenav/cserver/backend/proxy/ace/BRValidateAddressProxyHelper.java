/**
 * (c) Copyright 2011 TeleNav.
 * 
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.ace;

import java.util.StringTokenizer;

/**
 * BRValidateAddressProxyHelper
 * @author kwwang
 * @date 2011-6-29
 */
public class BRValidateAddressProxyHelper
{
    public static String[] getStreetAndNumber(String firstLine, String seprator)
    {
        String[] name = new String[2];
        if (firstLine.trim().indexOf(seprator) > -1)
        {
            String street = "";
            String number = "";
            StringTokenizer st = new StringTokenizer(firstLine, seprator);
            while (st.hasMoreElements())
            {
                String temp = st.nextToken().trim();
                char[] ch = temp.toCharArray();
                boolean isNum = true;
                for (int i = 0; i < ch.length; i++)
                {
                    if (!Character.isDigit(ch[i]))
                    {
                        isNum = false;
                        break;
                    }
                }
                if (!isNum)
                {
                    street += (temp + " ");
                }
                else
                {
                    number = temp;
                }
            }
            name[0] = street.trim();
            name[1] = number.trim();
        }
        else
        {
            name[0] = firstLine.trim();
            name[1] = "";
        }
        return name;
    }
}
