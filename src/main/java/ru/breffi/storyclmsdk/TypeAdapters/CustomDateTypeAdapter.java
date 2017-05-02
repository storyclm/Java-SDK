package ru.breffi.storyclmsdk.TypeAdapters;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


public class CustomDateTypeAdapter  extends TypeAdapter<Date>
//extends DateTypeAdapter  
implements JsonSerializer<Date>, JsonDeserializer<Date> 
{
	    private final DateFormat dateFormat;

	    private CustomDateTypeAdapter() {
	      dateFormat =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	      dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	    }

	    @Override public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
	      return new JsonPrimitive(dateFormat.format(date));
	    }

	   @Override public synchronized Date deserialize(JsonElement jsonElement, Type type,
	        JsonDeserializationContext jsonDeserializationContext) {
	      try {
	        return dateFormat.parse(jsonElement.getAsString());
	      } catch (ParseException e) {
	        throw new JsonParseException(e);
	      }
	    }

		@Override
		public void write(JsonWriter out, Date value) throws IOException {
			out.value(dateFormat.format(value));
			
		}

		@Override
		public Date read(JsonReader reader) throws IOException {
			DateTypeAdapter ad= new DateTypeAdapter();
			return ad.read(reader);
			 /* if (reader.peek() == JsonToken.NULL) {
		            reader.nextNull();
		            return null;
		        }
		        String dateAsString = reader.nextString();
		        try {
			        return dateFormat.parse(dateAsString);
			      } catch (ParseException e) {
			        throw new JsonParseException(e);
			      }*/
		}
	  }
