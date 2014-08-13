package com.meiliwan.search.spell.common;

public interface IData<T> {

	public T value();
	
	public void process(String data);
}
