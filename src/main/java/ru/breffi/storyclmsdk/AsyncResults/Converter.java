package ru.breffi.storyclmsdk.AsyncResults;

public interface Converter<Tin, Tout> {
public Tout Convert(Tin in);
}
