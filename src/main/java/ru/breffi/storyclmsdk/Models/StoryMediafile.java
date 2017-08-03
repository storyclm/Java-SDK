package ru.breffi.storyclmsdk.Models;

import java.util.Date;

public class StoryMediafile extends StorySimpleModel{
	public String fileName;

    public String title;

    public String blobId;

    public int fileSize;

    public String mimeType;

    public Date created;

    public Date updated;

    public String sas;

    public Boolean isAvailableForSharing;
}
