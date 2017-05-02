package Models;

import java.util.Date;

import com.google.gson.annotations.JsonAdapter;
//import com.google.gson.internal.bind.DateTypeAdapter;

import ru.breffi.storyclmsdk.TypeAdapters.CustomDateTypeAdapter;


public class Profile {
	  /// <summary>
    /// Идендификатор записи
    /// Зависит от провайдера таблиц
    /// </summary>
    public String _id ;

    /// <summary>
    /// Имя пользователя
    /// </summary>
    public String  Name ;

    /// <summary>
    /// Возраст
    /// </summary>
    public long Age;

    /// <summary>
    /// Пол
    /// </summary>
    public Boolean Gender ;

    /// <summary>
    /// Рейтинг
    /// </summary>
    public double Rating ;
    /// <summary>
    /// Дата регистрации
    /// </summary>
    @JsonAdapter(CustomDateTypeAdapter.class)
    public Date Created ;
}
