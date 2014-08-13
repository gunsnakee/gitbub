package com.meiliwan.emall.antispam.filter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Set;
//import java.util.Vector;
//import org.htmlparser.Node;
//import org.htmlparser.NodeFilter;
//import org.htmlparser.Parser;
//import org.htmlparser.filters.NodeClassFilter;
//import org.htmlparser.nodes.TagNode;
//import org.htmlparser.util.NodeList;


public class XSSFilter {

	private static XSSFilter xssFilter = new XSSFilter();
	
//	private static final String[] htmlTag = {" ", "&", "<", ">", "\"", "<!--", "-->", "",
//        "‮", "‫", "‏"                              };
	
//	private static final String[] encodeTag = {"&nbsp;", "&amp;", "&lt;", "&gt;", "&quot;",
//        "", "", "", "", "", "", ""                 };
//	
//	private final NodeFilter filter = new NodeClassFilter(TagNode.class);
//	
//	private static final String[] arrBadName       = {"applet", "base", "basefont", "bgsound",
//        "blink", "body", "embed", "frame", "frameset", "head", "html", "ilayer", "iframe", "layer",
//        "link", "meta", "object", "style", "title", "script", "@media", "@import", "behavior",
//        "form"                                     };
//	
//	private static final String[] arrBadAttribute  = {"\\", "@media", "@import", "expression",
//        "&#", "<", ">", "!import", "url(", "!--", "behavior"};
//	
//	private static final String[] arrBadLink       = {"dynsrc", "href", "lowsrc", "src",
//        "background", "value", "action", "bgsound", "behavior"};
//	
//	private static final String[] arrExpression1   = {"e", "x", "p", "r", "e", "s", "s", "i", "o",
//    "n"                                        };
//	
//	private static final String[] arrExpression2   = {"ｅ", "ｘ", "ｐ", "ｒ", "ｅ", "ｓ", "ｓ", "ｉ", "ｏ",
//    "ｎ"                                        };
//	
//	private static final String[] arrExpression3   = {"Ｅ", "Ｘ", "Ｐ", "Ｒ", "Ｅ", "Ｓ", "Ｓ", "Ｉ", "Ｏ",
//    "Ｎ"                                        };
	
	public static synchronized XSSFilter getInstance(){
		return xssFilter;
	}
	
	private XSSFilter(){
		
	}
	
	public static boolean detectXss(String content){

		if(content.indexOf("<script") != -1 || content.indexOf("script>") != -1){
			return true;
		}
		
		Document doc=Jsoup.parse(content);
		Elements link=doc.getElementsByTag("link");
		//问题:如果只有link标签，没有内容？
		if(!link.isEmpty()){
			return true;
		}
		Elements script=doc.getElementsByTag("script");
		if(!script.isEmpty()){
			return true;
		}
	    	
	    	return false;
	}
	
	public static String simpleFilter(String content) {

        content = content.replaceAll("<!--|<xmp|<scritp", "");

        return content;
    }
	
//	public String filtrateEditorTag(String content) {
//        for (int i = 5; i < htmlTag.length; ++i) {
//            if (content.indexOf(htmlTag[i]) != -1)
//                content = content.replaceAll(htmlTag[i], encodeTag[i]);
//        }
//        String contentLow = content.toLowerCase();
//        Parser parser = Parser.createParser(contentLow, "gb2312");
//        NodeList nodes;
//        try {
//            nodes = parser.extractAllNodesThatMatch(this.filter);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            return null;
//        }
//        int hasCut = 0;
//        Node[] nodeArray = nodes.toNodeArray();
//        StringBuffer sb = new StringBuffer(content);
//        for (int i = 0; i < nodeArray.length; ++i) {
//            TagNode tagNode = (TagNode) nodeArray[i];
//            boolean cutTag = false;
//            if (isBadName(tagNode)) {
//                cutTag = true;
//            } else {
//                Vector vecBadAttribute = badAttribute(tagNode);
//                if (vecBadAttribute != null) {
//                		Iterator iteraror = vecBadAttribute.iterator();
//                    lab: while (iteraror.hasNext()) {
//                        String badAttribute = (String) iteraror.next();
//                        int pos = contentLow.indexOf(badAttribute, tagNode.getTagBegin());
//                        if ((pos == -1) || (pos > tagNode.getTagEnd())) {
//                            cutTag = true;
//                            break lab;// lab 是后加的
//                        }
//                        sb.delete(pos - hasCut, pos + badAttribute.length() - hasCut);
//                        hasCut += badAttribute.length();
//                    }
//                } else {
//                    cutTag = true;
//                }
//            }
//            if (cutTag) {
//                label248: sb.delete(tagNode.getTagBegin() - hasCut, tagNode.getTagEnd() - hasCut);
//                hasCut += tagNode.getTagEnd() - tagNode.getTagBegin();
//            }
//        }
//        return sb.toString();
//    }
	
//	private static boolean isBadName(TagNode tagNode) {
//        String name = tagNode.getTagName().toLowerCase();
//        if (name == null) return false;
//        for (int i = 0; i < arrBadName.length; ++i) {
//            if (name.indexOf(arrBadName[i]) != -1) return true;
//        }
//        return false;
//    }
	
//	private static Vector<String> badAttribute(TagNode tagNode) {
//        Vector vecAttribute = tagNode.getAttributesEx();
//        Vector<String> vecBadAttribute = new Vector<String>();
//        Set<String> setAttributeHead = new HashSet<String>();
//        for (int i = 0; i < vecAttribute.size(); ++i) {
//            String strAttribute = vecAttribute.elementAt(i).toString();
//            if (strAttribute == null) continue;
//            String strAttributeHead = null;
//            if (strAttribute.length() <= 3) {
//                continue;
//            }
//            strAttributeHead = strAttribute.substring(0, 3);
//            if (setAttributeHead.contains(strAttributeHead)) {
//                return null;
//            }
//            setAttributeHead.add(strAttributeHead);
//            if (strAttribute.startsWith("on")) {
//                vecBadAttribute.add(strAttribute);
//            } else if (hasExpression(strAttribute)) {
//                vecBadAttribute.add(strAttribute);
//            } else if (badLink(strAttribute, tagNode)) {
//                vecBadAttribute.add(strAttribute);
//            } else {
//                for (int j = 0; j < arrBadAttribute.length; ++j)
//                    if (strAttribute.contains(arrBadAttribute[j])) {
//                        vecBadAttribute.add(strAttribute);
//                        break;
//                    }
//            }
//        }
//        return vecBadAttribute;
//    }
	
//	private static boolean hasExpression(String strAttribute) {
//        int pos = 0;
//        for (int i = 0; i < arrExpression1.length; ++i) {
//            int pos1 = strAttribute.indexOf(arrExpression1[i], pos);
//            int pos2 = strAttribute.indexOf(arrExpression2[i], pos);
//            int pos3 = strAttribute.indexOf(arrExpression3[i], pos);
//            if (pos1 == -1) {
//                if (pos2 == -1) {
//                    if (pos3 == -1) {
//                        break;
//                    }
//                    pos = pos3;
//                } else if (pos3 == -1) {
//                    pos = pos2;
//                } else {
//                    pos = (pos2 < pos3) ? pos2 : pos3;
//                }
//
//            } else if (pos2 == -1) {
//                if (pos3 == -1)
//                    pos = pos1;
//                else {
//                    pos = (pos1 < pos3) ? pos1 : pos3;
//                }
//            } else if (pos3 == -1) {
//                pos = (pos1 < pos2) ? pos1 : pos2;
//            } else {
//                pos = (pos1 < pos2) ? pos1 : pos2;
//                pos = (pos < pos3) ? pos : pos3;
//            }
//
//            if (i >= arrExpression1.length - 1) return true;
//        }
//        return false;
//    }
//	
//	private static boolean badLink(String strAttribute, TagNode tagNode) {
//        String strContent = null;
//        for (int i = 0; i < arrBadLink.length; ++i) {
//            if (strAttribute.startsWith(arrBadLink[i])) {
//                strContent = tagNode.getAttribute(arrBadLink[i]);
//                if ((!strContent.startsWith("http://")) && (!strContent.startsWith("ftp://"))) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//	
//	public static String encodeHTMl(String InputString) {
//        if (InputString == null) {
//            return null;
//        }
//
//        for (int i = 0; i < htmlTag.length; ++i) {
//            if (InputString.indexOf(htmlTag[i]) != -1)
//                InputString = InputString.replace(htmlTag[i], encodeTag[i]);
//        }
//        return InputString;
//    }

}
