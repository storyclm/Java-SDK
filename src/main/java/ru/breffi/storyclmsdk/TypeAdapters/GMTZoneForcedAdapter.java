package ru.breffi.storyclmsdk.TypeAdapters;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class GMTZoneForcedAdapter implements JsonDeserializer<Date> {

  @Override
  public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
      String date = element.getAsString();

      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      format.setTimeZone(TimeZone.getTimeZone("GMT"));
      System.out.println(date);
    
         try {
        	  System.out.println("GMT converter");
        	  Date d = format.parse(date);
        	 
        	  System.out.println(d.toGMTString());
        	  System.out.println(d.toLocaleString());
        	  System.out.println(d.toString());
        	  return d;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JsonParseException("Custom date deserializing error",e);
		}
  
   }
}