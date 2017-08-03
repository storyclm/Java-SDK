package ru.breffi.storyclmsdk.Models;

import java.util.Date;

public class StoryPresentation {
	 public int id;

     public String name ;

     public String shortDescription ;

     public String longDescription ;

     public String thumbImgId ;

     public String imgId ;

     public int order ;

     public int revision ;

     public Date created ;

     public String updated ;

     public int clientId ;

     public Boolean debugModeEnabled ;

     public Boolean skip ;

     /// <summary
     /// Карта включина
     /// </summary
     public Boolean mapEnabled ;

     /// <summary
     /// Тип карты
     /// </summary
     public int mapType ;

     /// <summary
     /// Подтверждение перед выходом
     /// </summary
     public Boolean needConfirmation ;

     /// <summary
     /// 
     /// </summary
     public int previewMode ;

     /// <summary
     /// Карта 
     /// </summary
     public String map ;

     /// <summary
     /// Видимость презентации 
     /// </summary
     public Boolean visibility ;

     public StoryContentPackage sourcesFolder ;

     /// <summary
     /// Список медиафайлов
     /// </summary
     public StorySimpleModel[] mediaFiles ;

     /// <summary
     /// Список слайдов
     /// </summary
     public StorySimpleModel[] slides ;

     /// <summary
     /// Список презентаций, коорые необходимы для корректной работы
     /// </summary
     public int[] presentations ;

     /// <summary
     /// Пользователи
     /// </summary
     public StorySimpleUserForPresentation[] users ;

}
