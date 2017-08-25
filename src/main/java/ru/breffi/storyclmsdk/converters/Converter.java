package ru.breffi.storyclmsdk.converters;

public interface Converter<Tin, Tout> {
public Tout Convert(Tin in);
}
