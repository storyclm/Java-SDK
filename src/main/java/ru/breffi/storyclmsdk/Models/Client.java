package ru.breffi.storyclmsdk.Models;

import java.util.Date;

public class Client {
	/// <summary>
    /// Идентификатор клиента в системе
    /// </summary>
    public int id;

    /// <summary>
    /// Название клиента
    /// </summary>
    public String name ;

    /// <summary>
    /// Клиент заблокирован
    /// </summary>
    public Boolean lockoutEnabled;

    /// <summary>
    /// Короткое описание
    /// </summary>
    public String shortDescription ;

    /// <summary>
    /// Полное описание
    /// </summary>
    public String longDescription ;

    /// <summary>
    /// URL логтипа
    /// </summary>
    public String thumbImgId ;

    /// <summary>
    /// URL арта
    /// </summary>
    public String imgId ;
    
    /// <summary>
    /// URL сслыка на ресурс о компании или продукта
    /// </summary>
    public String url ;

    /// <summary>
    /// Адрес электронной почты контактного лица
    /// </summary>
    public String email ;

    /// <summary>
    /// Дата создания клиента
    /// </summary>
    public Date created ;

    /// <summary>
    /// Дата последнего изменения свойст клиента
    /// </summary>
    public Date updated ;

    /// <summary>
    /// Идентификатор таблицы в которой хранится лог устройства
    /// </summary>
    public String deviceLogTable ;

    /// <summary>
    /// Идетификатор таблицы которая хранит базовую статистику
    /// </summary>
    public String baseStatisticsTable ;

    /// <summary>
    /// Идентификатор таблицы коорая хранит геолокацию пользователей
    /// </summary>
    public String geolocationTable ;

    /// <summary>
    /// Список презентаций клиента
    /// </summary>
    public StorySimplePresentation[] presentations ;

    /// <summary>
    /// Список пользователей клиента
    /// </summary>
    public StorySimpleUser[] users ;

    /// <summary>
    /// Список групп клиента
    /// </summary>
    public StorySimpleGroup[] groups ;

    /// <summary>
    /// Список таблиц клиента
    /// </summary>
    public StorySimpleTable[] tables ;
}
