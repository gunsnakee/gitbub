package com.meiliwan.emall.antispam.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 文本检查结果
 * 
 * @author mac
 * 
 */
public class KeywordCheckResult {

    private List<KeywordPosition> keywordPositions;
    private Set<String>           keywords;

    public KeywordCheckResult() {
        keywordPositions = new ArrayList<KeywordPosition>();
        keywords = new TreeSet<String>();
    }
    // ---------------------------------------

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public List<KeywordPosition> getKeywordPositions() {
        return keywordPositions;
    }

    public void setKeywordPositions(List<KeywordPosition> keywordPositions) {
        this.keywordPositions = keywordPositions;
    }

    @Override
    public String toString() {
        return keywordPositions.toString();
    }

}
