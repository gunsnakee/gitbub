package com.meiliwan.emall.antispam.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class KeywordUtil {

    public static Set<String> doKeyword(Set<String> keywords,
        HashMap<String, Set<String>> doubleKeyword) {
        Set<String> res = new HashSet<String>();
        for (String string : keywords) {
            Set<String> link = doubleKeyword.get(string);
            if (link != null && !link.isEmpty()) {
                for (String string2 : link) {
                    string2.hashCode();
                    if (keywords.contains(string2)) {
                        String s = null;
                        if (string.compareTo(string2) < 0) {
                            s = string + "|" + string2;
                        } else {
                            s = string2 + "|" + string;
                        }
                        // 增加返回信息可读性，对|含义进行解释 
                        s += "(备注： \"|\"表示内容中不能同时出现用\"|\"分隔开的这些词)";
                        res.add(s);
                    }
                }
            } else {
                res.add(string);
            }
        }
        return res;
    }

    public static List<List<String>> getAllSub(List<String> content) {
        List<List<String>> res = new ArrayList<List<String>>();
        List<List<String>> subString = getSubStrings(content);
        for (List<String> list : subString) {
            List<List<String>> p = perm(list);
            res.addAll(p);
        }
        return res;
    }

    public static List<String> getAllSubString(String content) {
        String[] ss = content.split("\\|");
        ArrayList<String> res = new ArrayList<String>();
        ArrayList<String> toDo = new ArrayList<String>();
        for (String string : ss) {
            toDo.add(string);
        }
        List<List<String>> list = getAllSub(toDo);
        for (List<String> list2 : list) {
            res.add(combine(list2));
        }
        return res;

    }

    /**
     * 输出全排列
     * 
     * @param content
     * @return
     */
    private static List<List<String>> perm(List<String> content) {
        List<List<String>> res = new ArrayList<List<String>>();
        if (content.size() == 1) {
            res.add(content);
            return res;
        }
        for (int i = 0; i < content.size(); i++) {
            ArrayList<String> array = new ArrayList<String>();
            array.addAll(content);
            array.remove(i);
            List<List<String>> sub = perm(array);
            for (int j = 0; j < sub.size(); j++) {
                List<String> toAdd = null;
                for (int j2 = 0; j2 < content.size(); j2++) {
                    toAdd = new ArrayList<String>();
                    toAdd.addAll(sub.get(j));
                    toAdd.add(j2, content.get(i));

                }
                res.add(toAdd);
            }
        }
        return res;

    }

    private static String combine(List<String> strings) {
        String res = "";
        for (String string : strings) {
            res += string;
            res += "|";
        }
        res = res.substring(0, res.length() - 1);
        return res;
    }

    /**
     * 获得所有的子串 如输入 abc 输出 [abc,ab,ac,bc,a,b,c]
     * 
     * @return
     */
    private static List<List<String>> getSubStrings(List<String> content) {
        List<List<String>> res = new ArrayList<List<String>>();
        int n = content.size();
        for (int i = (int) Math.pow(2, n) - 1; i > 0; i--) {
            List<String> l = new ArrayList<String>();
            res.add(l);
            for (int j = 0; j <= n - 1; j++) {
                if ((((1 << j) & i) >> j) == 1) {
                    l.add(content.get(j));
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        ArrayList<String> c = new ArrayList<String>();
        c.add("a");
        c.add("b");
        c.add("c");
        String s = "a|b|c|d";
        // 这么多 坑爹
        System.out.println(KeywordUtil.getSubStrings(c));
        System.out.println(KeywordUtil.perm(c));
        System.out.println(KeywordUtil.getAllSub(c));
        System.out.println(KeywordUtil.getAllSubString(s));
    }
}
