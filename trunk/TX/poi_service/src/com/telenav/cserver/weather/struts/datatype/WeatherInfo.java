/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.weather.struts.datatype;

import java.io.Serializable;
import java.util.Date;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.util.TnConstants;
import com.telenav.cserver.util.TnUtil;
import com.telenav.j2me.framework.protocol.ProtoWeatherInfo;


/**
 * Weather Info Bean
 * 
 * @author jbtian
 * @version 1.0 
 */
public class WeatherInfo implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6357287265140890912L;
	private String temp = "";
    private String status = "";
    private String high = "";
    private String low = "";
    private int dayOfWeek = 0;
    private String feel = "";
    private String wind = "";
    private String humidity = "";
    private Date date;
    private String windDirection="";
    private float windSpeed=0.0f;

    
    int weatherCode = TnConstants.WEATHER_CODE_DEFAULT;
    int temperatureCode = TnConstants.TEMP_CODE_NORMAL;

    public String getTemp()
    {
        return temp;
    }

    public void setTemp(String temp)
    {
        this.temp = temp;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getHigh()
    {
        return high;
    }

    public void setHigh(String high)
    {
        this.high = high;
    }

    public String getLow()
    {
        return low;
    }

    public void setLow(String low)
    {
        this.low = low;
    }

    public int getDayOfWeek()
    {
        return dayOfWeek;
    }

    public void setDayOfWeek(int day)
    {
        this.dayOfWeek = day;
    }

    public String getFeel()
    {
        return feel;
    }

    public void setFeel(String feel)
    {
        this.feel = feel;
    }

    public String getWind()
    {
        return wind;
    }

    public void setWind(String wind)
    {
        this.wind = wind;
    }

    public String getHumidity()
    {
        return humidity;
    }

    public void setHumidity(String humidity)
    {
        this.humidity = humidity;
    }
    
    

    /**
     * @return Returns the temperatureCode.
     */
    public int getTemperatureCode()
    {
        return temperatureCode;
    }

    /**
     * @param temperatureCode The temperatureCode to set.
     */
    public void setTemperatureCode(int temperatureCode)
    {
        this.temperatureCode = temperatureCode;
    }

    /**
     * @return Returns the weatherCode.
     */
    public int getWeatherCode()
    {
        return weatherCode;
    }

    /**
     * @param weatherCode The weatherCode to set.
     */
    public void setWeatherCode(int weatherCode)
    {
        this.weatherCode = weatherCode;
    }
    
    public JSONObject toJSON()
    {
        JSONObject jo = new JSONObject();

        try {
            jo.put("temp", this.getTemp());
            jo.put("status", this.getStatus());
            jo.put("high", this.getHigh());
            jo.put("low", this.getLow());
            jo.put("feel", this.getFeel());
            jo.put("wind", this.getWind());
            jo.put("humidity", this.getHumidity());
            jo.put("dayOfWeek", this.getDayOfWeek());
            jo.put("imageWeatherBig", "weatherBig_" + this.getWeatherCode() + ".png");
            jo.put("imageWeatherSmall", "weatherSmall_" + this.getWeatherCode() + ".png");
            jo.put("tempCodeImage", "thermo_" + this.getTemperatureCode() + ".png");
            jo.put("longWeekDesc", TnUtil.getLongWeekDesc(this.getDayOfWeek()));
            jo.put("shortWeekDesc", TnUtil.getShotWeekDesc(this.getDayOfWeek()));
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jo;
    }
    
    public String toString()
    {
        return "WeatherInfo[temp=" + temp + ",status=" + status + ",high=" + high + ",low=" + low
            + ",windSpeed=" + windSpeed + ",windDirection="+windDirection+",feel=" + feel + ",weatherCode=" + weatherCode + ",temperatureCode=" + temperatureCode + ",dayOfWeek=" + dayOfWeek +  "]";
    }

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public float getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}
	
	public ProtoWeatherInfo toProtobuf()
	{
		ProtoWeatherInfo.Builder builder = ProtoWeatherInfo.newBuilder();
		builder.setTemp(this.temp);
		builder.setStatus(this.status);
		builder.setHigh(this.high);
		builder.setLow(this.low);
		builder.setDayOfWeek(this.dayOfWeek);
		builder.setFeel(this.feel);
		builder.setWind(this.wind);
		builder.setHumidity(this.humidity);
		builder.setDate(TnUtil.getWeatherDisplayDate(this.date));
		builder.setWindDirection(this.windDirection);
		builder.setWindSpeed(Integer.toString(Math.round(windSpeed)));
		builder.setWeatherCode(this.weatherCode);
		builder.setTemperatureCode(this.temperatureCode);
		return builder.build();
	}
	

}
