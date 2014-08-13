package com.meiliwan.search.spell.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.meiliwan.search.pinyin.PinyinEnumerator;
import com.meiliwan.search.spell.common.SpellConstants;
import com.meiliwan.search.util.QueryStringUtil;
import com.meiliwan.search.util.io.FileUtil;

/**
 * Make sure the logic here is the same of {@link #SpellChecker}
 * @author lgn-mop
 *
 */
public class SpellIndexBuilder {

	IndexWriter writer = null;

	public SpellIndexBuilder(String outDir) throws IOException{
		Directory dir = FSDirectory.open(new File(outDir));
		IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_43, new StandardAnalyzer(Version.LUCENE_43));
		indexConfig.setRAMBufferSizeMB(50);
		writer = new IndexWriter(dir, indexConfig);
	}


	public void buildFromFile(String file) throws IOException{
		BufferedReader br = FileUtil.UTF8Reader(file);

		for(String line = br.readLine(); line!= null; line = br.readLine()){
			Document doc = createDocument(line);	
			writer.addDocument(doc);
		}
		br.close();
		writer.commit();
		writer.forceMerge(1);
		writer.close();
	}


	private static void addGram(String text, Document doc, float boost){
		//TODO
		int ng1 = SpellConstants.getNgramMin(text.length());
		int ng2 = SpellConstants.getNgramMax(text.length());


		if (text.charAt(0) >= 0x4E00 && text.charAt(0) <= 0x9FA5){
			//hanzi
			String fullPY = PinyinEnumerator.wordsToPinyin(text);
			List<String> consonants = PinyinEnumerator.yieldConsonants(text, true);
			Field pinyinField = new Field(SpellConstants.PINYIN_NAME, fullPY, getFieldType(false));
			pinyinField.setBoost(boost * 3.0f);
			doc.add(pinyinField);
			for(String consonant : consonants){
				pinyinField = new Field(SpellConstants.PINYIN_NAME, consonant, getFieldType(false));
				pinyinField.setBoost(boost * 3.0f);
				doc.add(pinyinField);
			}
		}

		int len = text.length();
		for (int ng = ng1; ng <= ng2; ng++) {
			String key = SpellConstants.NGRAM_NAME + ng;
			String end = null;
			for (int i = 0; i < len - ng + 1; i++) {
				String gram = text.substring(i, i + ng);
				Field ngramField = new Field(key, gram, getFieldType(false));
				ngramField.setBoost(boost);
				// spellchecker does not use positional queries, but we want freqs
				// for scoring these multivalued n-gram fields.
				doc.add(ngramField);
				if (i == 0) {
					// only one term possible in the startXXField, TF/pos and norms aren't needed.
					Field startField = new Field(SpellConstants.START_NAME + ng, gram, getFieldType(false));
					startField.setBoost(boost);
					doc.add(startField);
				}
				end = gram;
			}
			if (end != null) { // may not be present if len==ng1
				// only one term possible in the endXXField, TF/pos and norms aren't needed.
				Field endField = new Field(SpellConstants.END_NAME + ng, end, getFieldType(false));
				endField.setBoost(boost);
				doc.add(endField);
			}
		}
	}

	private static FieldType getFieldType(boolean stored){
		FieldType ft = new FieldType();
		ft.setIndexed(true);
		ft.setStored(stored);
		ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		return ft;
	}
	

	
	private static Document createDocument(String str){
		String[] queryInfo = str.split("\t");

		Float boost = new Float(1.0);
		if (queryInfo.length > 1)
			boost = new Float(queryInfo[1]);

		Document doc = new Document();

		//TODO
		//original term
		Field f = new StringField(SpellConstants.ORIGIN, queryInfo[0], Field.Store.YES);
		
		doc.add(f);

		Field f2 = new StringField(SpellConstants.PINYIN_FORM, PinyinEnumerator.wordsToPinyin(str), Field.Store.YES);	
//		Field f2 = new Field(Constants.PINYIN_FORM, PinyinEnumerator.wordsToPinyin(str), getFieldType(true));	

//		f2.setBoost(boost);
		doc.add(f2);
		
		List<String> fragments = QueryStringUtil.cutSequence(str);
		for(String fragment : fragments){
			addGram(fragment, doc, boost);
		}
		return doc;
	}


	/**
	 * @param args
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String out = com.meiliwan.search.common.Constants.INDEX_DIR + "/mall_1";
		SpellIndexBuilder sib = new SpellIndexBuilder(out);
		sib.buildFromFile("d:/data/dianshang3.txt");
	}

}
