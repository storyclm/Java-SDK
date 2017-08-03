package ru.breffi.storyclmsdk.Models;

import java.util.Date;

import com.google.gson.annotations.JsonAdapter;

import ru.breffi.storyclmsdk.TypeAdapters.GMTZoneForcedAdapter;

public class StoryContentPackage {
	  /// <summary>
    /// Идентификатор
    /// </summary>
    public int id;

    /// <summary>
    /// Идентификатор блоба
    /// </summary>
    public String blobId;

    /// <summary>
    /// Размер пакета
    /// </summary>
    public int fileSize;
    
    /// <summary>
    /// Тип блоба
    /// </summary>
    public String mimeType;

    /// <summary>
    /// Ревизия
    /// </summary>
    public int revision;

    /// <summary>
    /// Дата создания
    /// </summary>
    @JsonAdapter(GMTZoneForcedAdapter.class)
    public Date created;

    /// <summary>
    /// Дата обновления
    /// </summary>
    public Date updated;
}
