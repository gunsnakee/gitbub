package com.meiliwan.search.spell.core;


import com.meiliwan.search.common.RequestProcessor;


public class SpellProcessor extends RequestProcessor{

	public SpellProcessor(String address) throws Exception {
		super(SpellModule.class, SpellModule.getZkServicePath(), address);
	}
	
	public String toString(){
		return this.modules.toString();
	}
}
