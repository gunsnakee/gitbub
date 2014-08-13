package com.meiliwan.search.spell.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.StringDistance;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;

import com.google.common.collect.MinMaxPriorityQueue;
import com.meiliwan.search.common.Constants;
import com.meiliwan.search.pinyin.PinyinEnumerator;
import com.meiliwan.search.spell.common.FMMAnalyzer;
import com.meiliwan.search.spell.common.SpellConstants;


import com.meiliwan.search.util.QueryStringUtil;

/**
 * Make sure the logic of ngram-query is the same of {@link #SpellIndexBuilder} 
 * @author lgn-mop
 *
 */
public class SpellChecker {
	Directory spellIndex;
	FMMAnalyzer fmmAnalyzer;
	private IndexSearcher indexSearcher;
	private StringDistance sd;
	private Comparator<SuggestWord> comparator;

	private volatile boolean closed = false;

	public static float defaultFullPyBoost = 32f;
	public static float defaultConsonantPyBoost = 24f;
	public static float defaultStartBoost = 3.0f;
	public static float defaultEndBoost = 2.0f;
	public static float defaultAccuracy = 0.1f;
	
	
	public SpellChecker(String dirOnFs) throws IOException{
		comparator = new SuggestWord.Sorter();
		sd = new LevensteinDistance();
		File file = new File(dirOnFs);
		spellIndex = new RAMDirectory(FSDirectory.open(file), IOContext.READ);
		indexSearcher = new IndexSearcher(DirectoryReader.open(spellIndex));
		File[] filesInDir = file.listFiles();
		for(File f : filesInDir){
			if (f.getName().endsWith(".word")){
				this.fmmAnalyzer = new FMMAnalyzer(f.getAbsolutePath());
				break;
			}
		}
	}
	
	public List<String> suggestSimilar(String queryStr, int numSug) throws IOException{
		return suggestSimilar(queryStr, numSug, defaultFullPyBoost, defaultConsonantPyBoost, defaultStartBoost, defaultEndBoost, defaultAccuracy);
	}

	/**
	 * return 0~k results
	 * @param queryStr
	 * @param numSug
	 * @param fullPyBoost
	 * @param consonantPyBoost
	 * @param startBoost
	 * @param endBoost
	 * @param accuracy
	 * @return
	 * @throws java.io.IOException
	 */
	public List<String> suggestSimilar(String queryStr, int numSug, 
			float fullPyBoost, float consonantPyBoost, float startBoost, float endBoost, float accuracy) throws IOException{
		BooleanQuery query = new BooleanQuery();
		List<String> fragments = QueryStringUtil.cutSequence(queryStr);

		for(String fragment : fragments){
			buildQuery(query, fragment, fullPyBoost, consonantPyBoost, startBoost, endBoost);
		}

		return doQuery(query, QueryStringUtil.cleanQuery(queryStr, ""), numSug, accuracy);
	}
	
	public List<String> retainKeywords(String queryStr) throws IOException{
		return fmmAnalyzer.analyze(queryStr);
	}
	
	
	public void buildQuery(BooleanQuery query, String text, 
			float fullPyBoost, float consonantPyBoost, float startBoost, float endBoost){
		String key;
		int ng1 = SpellConstants.getNgramMin(text.length());
		int ng2 = SpellConstants.getNgramMax(text.length());
		if (text.charAt(0) >= 0x4E00 && text.charAt(0) <= 0x9FA5){
			//hanzi
			String fullPY = PinyinEnumerator.wordsToPinyin(text);

			if (fullPyBoost > 0){
				add(query, SpellConstants.PINYIN_NAME, fullPY, fullPyBoost);
			}else {
				add(query,SpellConstants.PINYIN_NAME, fullPY, 1.0f);
			}
		}else{
			//English, deem as pinyin
			if (consonantPyBoost > 0){
				add(query, SpellConstants.PINYIN_NAME, text, consonantPyBoost);
			}else{
				add(query,SpellConstants.PINYIN_NAME, text, 1.0f);
			}
				
		}
		//ngram------------
		for (int ng = ng1; ng <=ng2; ng++){
			
			String[] grams = formGrams(text, ng); // form word into ngrams (allow dups too)
			if (grams.length == 0) {
				continue; // hmm
			}
			//----------- should we boost prefixes?
			if (startBoost > 0){
				add(query, SpellConstants.START_NAME + ng, grams[0], startBoost);
			}else {
				add(query, SpellConstants.START_NAME + ng, grams[0], 1.0f);
			}
			
			//-----------------------boost prefixes end-
			//------------should we boost suffixes?
			if (endBoost > 0){
				add(query, SpellConstants.END_NAME + ng, grams[grams.length - 1], endBoost);
			}else {
				add(query, SpellConstants.END_NAME + ng, grams[grams.length - 1], 1.0f);
			}
			//-----------------------boost suffixes end
			//---------- no boost for other n-grams
			key = SpellConstants.NGRAM_NAME + ng; // form key
			for (int i = 0; i < grams.length; i++) {
				add(query, key, grams[i]);
			}
		}

	}

	/**
	 * too near
	 * @param sug
	 * @param word
	 * @return
	 */
	public static boolean isEqualOriginalWord(char[] sug, char[] word) {
		if(sug.length != word.length)
			return false;
		Arrays.sort(sug);
		return Arrays.equals(sug, word);
	}
	
	
	private static String[] formGrams(String text, int ng) {
		int len = text.length();
		String[] res = new String[len - ng + 1];
		for (int i = 0; i < len - ng + 1; i++) {
			res[i] = text.substring(i, i + ng);
		}
		return res;
	}


	/**
	 * Add a clause to a boolean query.
	 */
	private static void add(BooleanQuery q, String name, String value, float boost) {
		Query tq = new TermQuery(new Term(name, value));
		tq.setBoost(boost);
		q.add(new BooleanClause(tq, BooleanClause.Occur.SHOULD));
	}

	/**
	 * Add a clause to a boolean query.
	 */
	private static void add(BooleanQuery q, String name, String value) {
		q.add(new BooleanClause(new TermQuery(new Term(name, value)), BooleanClause.Occur.SHOULD));
	}

	private float calculateHitScore(String originalPinyinForm, String pinyinForm, float hitScore, float hitScoreRate){
		float d = sd.getDistance(originalPinyinForm ,pinyinForm);
		float h = (float)Math.sqrt(hitScore);
		return d * (1.0f- hitScoreRate) + h *  hitScoreRate;
	}

	private List<String> doQuery(BooleanQuery query, String word, int limit, float accuracy) throws IOException{
		int maxHits = 10 * limit;
		//    System.out.println("Q: " + query);
		ScoreDoc[] hits = indexSearcher.search(query, null, maxHits).scoreDocs;
//		System.out.println(hits.length);
		MinMaxPriorityQueue<SuggestWord> sugQueue
		= MinMaxPriorityQueue.orderedBy(this.comparator).maximumSize(limit).create();
		int stop = Math.min(hits.length, maxHits);
		String originalPinyinForm = PinyinEnumerator.wordsToPinyin(word);
		char[] newWord = word.toLowerCase().toCharArray();
		Arrays.sort(newWord);
		for (int i = 0; i < stop; i++) {
			SuggestWord sugWord = new SuggestWord();
			sugWord.string = indexSearcher.doc(hits[i].doc).get(SpellConstants.ORIGIN);
			String pinyinForm = indexSearcher.doc(hits[i].doc).get(SpellConstants.PINYIN_FORM);
			if (isEqualOriginalWord(QueryStringUtil.cleanQuery(sugWord.string,"")
					.toLowerCase().toCharArray(), newWord)) {
				//too near
				continue;
			}
			sugWord.score = this.calculateHitScore(originalPinyinForm, pinyinForm, hits[i].score, 0.5f);
			if (sugWord.score < defaultAccuracy) {
				// too far away
				continue;
			}
			sugQueue.add(sugWord);

		}
		int sugQueueSize = sugQueue.size();
		List<String> list = new ArrayList<String>(sugQueueSize);
		for (int i = 0; i < sugQueueSize; i++) {
			list.add(sugQueue.poll().string);
		}
		return list;
	}




	public Directory getDirectory(){
		return this.spellIndex;
	}

	public synchronized void close() throws IOException{
		closed = true;
		if (indexSearcher != null) {
			indexSearcher.getIndexReader().close();
			
		}
//		spellIndex.close();
		indexSearcher = null;
	}

	public synchronized boolean isClosed(){
		return closed;
	}
	
	public static String defaultConfigInJSON(){
		JSONObject param = new JSONObject();
		param.put(SpellConstants.ACCLOWERBOUND, defaultAccuracy);
		param.put(SpellConstants.FULLPYBOOST, defaultFullPyBoost);
		param.put(SpellConstants.BSTARTBOOST, defaultStartBoost);
		param.put(SpellConstants.BENDBOOST, defaultEndBoost);
		param.put(SpellConstants.CONSONANTPYBOOST, defaultConsonantPyBoost);

		JSONObject json = new JSONObject();
		JSONObject strgy = new JSONObject();
		strgy.put(Constants.ABTEST_RATIO, 1.0);
		strgy.put(Constants.ABTEST_PARAM, param);
		strgy.put(Constants.ABTEST_ABBR, "someboosts");
		JSONObject ab = new JSONObject();
		ab.put("A", strgy);
		json.put(Constants.ABTEST_STRING, ab);
		return json.toString();
		
	}
}
