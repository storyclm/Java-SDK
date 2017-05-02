package ru.breffi.storyclmsdk;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GenericListType<T> implements ParameterizedType {

    private Type wrapped;

    public GenericListType(Type wrapped) {
        this.wrapped = wrapped;
    }
    
    public Type[] getActualTypeArguments() {
        return new Type[] {wrapped};
    }

    public Type getRawType() {
        return  List.class;
    }

    public Type getOwnerType() {
        return null;
    }

}