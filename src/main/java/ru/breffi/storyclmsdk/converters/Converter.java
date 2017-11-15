package ru.breffi.storyclmsdk.converters;
@FunctionalInterface 
public interface Converter<Tin, Tout> {
public Tout Convert(Tin in);
}
