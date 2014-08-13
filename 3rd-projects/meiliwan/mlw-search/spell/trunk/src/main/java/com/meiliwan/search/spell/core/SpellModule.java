package com.meiliwan.search.spell.core;

import java.io.IOException;


import com.meiliwan.search.common.Constants;
import com.meiliwan.search.common.ProcessorModule;


/**
 * basic operation for SpellChecker
 * @author lgn-mop
 *
 */
public class SpellModule extends ProcessorModule{

	volatile SpellChecker spell = null;

	
	public static String getZkServicePath(){
		return Constants.ZK_SEARCH_ROOT + "/spell";
	}
	
	public SpellModule(String JSON) throws IOException {
		super(JSON);
		String pathOnFS = Constants.INDEX_DIR + "/" + this.realCollection;
		spell = new SpellChecker(pathOnFS);
	}

	@Override
	protected void close() throws Exception {
		if (!spell.isClosed())
			spell.close();
	}
	
	public SpellChecker getSpellCheck(){
		return this.spell;
	}
		
}
