package ru.breffi.storyclmsdk.TypeAdapters;

//import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class StoryDateTypeAdapter  implements JsonSerializer<Date>, JsonDeserializer<Date> 
{
	    private final DateFormat dateFormat;
	    private final String dateFormatString  = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	    
	    public StoryDateTypeAdapter() {
	      dateFormat =new SimpleDateFormat(dateFormatString);
	      dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	    
	    }

	  @Override 
	    public  JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
	      return new JsonPrimitive(dateFormat.format(date));
	    }

	   @Override 
	   public  Date deserialize(JsonElement jsonElement, Type type,
	        JsonDeserializationContext jsonDeserializationContext) {
	      try 
	      {
	    	  String jsonEl = jsonElement.getAsString();
	    	  //В случае прихода урезанной даты
	    	  if (jsonEl.length()!=24){
		    	  jsonEl = jsonEl.substring(0,jsonEl.length()-1);
		    	  int restcount = 23 - jsonEl.length();
		    	  String restPattern = ".000Z";
		    	  jsonEl+=restPattern.substring(restPattern.length()-1-restcount, restPattern.length());
	    	  }
	    	  return dateFormat.parse(jsonEl);
	    	 
	      } catch (ParseException e) {
	        throw new JsonParseException(e);
	      }
	    }

	  }
