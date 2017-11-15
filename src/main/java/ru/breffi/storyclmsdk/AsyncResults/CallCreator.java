package ru.breffi.storyclmsdk.AsyncResults;
@FunctionalInterface
public interface CallCreator<Tin,Tout> {
	public Tout Create(Tin in);
}
