/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.cose;

/**
 * GasPriceTypeDef.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-20
 */
public class GasPriceTypeDef
{
  public static final int TYPE_BASIC_GRADE = 0;
  public static final int TYPE_MID_GRADE = 1;
  public static final int TYPE_HIGH_GRADE = 2;
  public static final int TYPE_DIESEL_GRADE = 3;
  private static final String[] PRICE_TYPE = { "Basic-87 Grade", "Mid-89 Grade", "High-91 Grade", "Diesel Grade" };

  public static String getPriceTypeDescription(int typeCode)
  {
    if ((typeCode >= 0) && (typeCode < PRICE_TYPE.length))
    {
      return PRICE_TYPE[typeCode];
    }

    return "UNKNOWN TYPE";
  }
}
