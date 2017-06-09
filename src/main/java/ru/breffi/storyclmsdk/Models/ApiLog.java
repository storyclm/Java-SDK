package ru.breffi.storyclmsdk.Models;

import java.util.Date;

public class ApiLog {
	
    
    /**  Идендификатор записи ло   га */
    public String Id ;

    
    /**Идендификатор сущности в таблице*/
    public String entityId;


    /**Дата выполненой операции*/
    public Date created ;

    /** Тип выполненной операции
    /// 0 - insert
    /// 1 - update
    /// 2 - delete
    */
    public int operationType ;
}
