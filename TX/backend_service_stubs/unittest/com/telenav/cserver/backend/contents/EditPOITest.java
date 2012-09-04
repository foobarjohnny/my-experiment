/**
 * 
 */
package com.telenav.cserver.backend.contents;

import com.telenav.cserver.backend.contents.ContentManagerServiceProxy;
import com.telenav.cserver.backend.contents.EditPOIRequest;
import com.telenav.cserver.backend.datatypes.contents.EditablePoi;
import com.telenav.cserver.backend.datatypes.contents.Stop;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @author zhjdou@telenav.cn
 * 
 */
public class EditPOITest
{

    /**
     * Please use the real poi id that in database, if you do not know please get one from COSE team or other guys
     * @param args
     */
    public static void main(String[] args)
    {
        // ContentManager mang;
        // mang= ContentManager.
        ContentManagerServiceProxy server = ContentManagerServiceProxy.getInstance();
        double lat = 3735548 / 100000.0;
        // stop.lat = 0;
        double lon = -12195426 / 100000.0;
        EditablePoi editPOi = new EditablePoi();    
        editPOi.setUserId(123344566);//user id
        editPOi.setPhone("911.911.0000");
       editPOi.setBrandName("McDonald's"); // brand name is the key
        editPOi.setPoiId(8200148782L);// poi id
        
        Stop addr = new Stop();//address start
        addr.setLatitude(lat);
        addr.setCountry("us");
        addr.setAddressId("8027001333");
        addr.setLongitude(lon);
        addr.setCityName("sunnyvale");
        addr.setState("ca");
        addr.setCrossStreetName("1130 kifer rd");
        editPOi.setAddress(addr); //address end

        
        editPOi.setRating(4f);//edit reating start
        editPOi.setReview("great restaurant");
        editPOi.setReviewerName("eddy");//edit rating end

        editPOi.setPriceRange(3.5);//edit price

        editPOi.setBusinessHours("8am to 8pm");//edit business hour

       
        EditPOIRequest editRequest = new EditPOIRequest();
        editRequest.setEditPOI(editPOi);
        editRequest.setContext("dataset=TA");
        editRequest.setMapDataSet("TeleAtlas");
        editRequest.setPoiDataSet("TA");
        editRequest.setUserId(123344566);
        TnContext tc=new TnContext();
        tc.addProperty("login", "test");
//        try
//        {
//            //server.editPOI(editRequest,tc);
//        }
//        catch (ThrottlingException e)
//        {
//            e.printStackTrace();
//        }
    

    }

}
