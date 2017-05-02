package ru.breffi.storyclmsdk.Models;


import java.util.Date;

import com.google.gson.annotations.JsonAdapter;

import ru.breffi.storyclmsdk.TypeAdapters.GMTZoneForcedAdapter;

public class ApiTable {
	 public int id;

     public String name;

     public ApiTableSchema[] schema;
     
     @JsonAdapter(GMTZoneForcedAdapter.class)
     public Date created;
}
