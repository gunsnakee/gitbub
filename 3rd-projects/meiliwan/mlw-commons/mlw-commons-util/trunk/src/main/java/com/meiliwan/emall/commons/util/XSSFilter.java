package com.meiliwan.emall.commons.util;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * XSSFilter。
 * 
 * 过滤XSS
 */
public class XSSFilter {

	private static XSSFilter instance = null;
	private static final String[] htmlTag = { " ", "&", "<", ">", "\"", "<!--",
			"-->", "", "‮", "‫", "‏" };
	private static final String[] encodeTag = { "&nbsp;", "&amp;", "&lt;",
			"&gt;", "&quot;", "", "", "", "", "", "", "" };

	private final NodeFilter filter = new NodeClassFilter(TagNode.class);

	private static final String[] arrBadName = { "applet", "base", "basefont",
			"bgsound", "blink", "body", "embed", "frame", "frameset", "head",
			"html", "ilayer", "iframe", "layer", "link", "meta", "object",
			"style", "title", "script", "@media", "@import", "behavior", "form" };

	private static final String[] arrBadAttribute = { "\\", "@media",
			"@import", "expression", "&#", "<", ">", "!import", "url(", "!--",
			"behavior" };

	private static final String[] arrBadLink = { "dynsrc", "href", "lowsrc",
			"src", "background", "value", "action", "bgsound", "behavior" };

	private static final String[] arrExpression1 = { "e", "x", "p", "r", "e",
			"s", "s", "i", "o", "n" };
	private static final String[] arrExpression2 = { "ｅ", "ｘ", "ｐ", "ｒ", "ｅ",
			"ｓ", "ｓ", "ｉ", "ｏ", "ｎ" };
	private static final String[] arrExpression3 = { "Ｅ", "Ｘ", "Ｐ", "Ｒ", "Ｅ",
			"Ｓ", "Ｓ", "Ｉ", "Ｏ", "Ｎ" };

	private static final String[] arrBadWordForCSS = { "@media", "@import",
			"expression", "behavior", "behaviour", "moz-binding",
			"include-source", "content", "<", ">", "\\", "&#" };

	public static XSSFilter getInstance() {
		if (instance == null)
			instance = new XSSFilter();
		return instance;
	}

	public static String encodeHTMl(String InputString) {
		if (InputString == null) {
			return null;
		}

		for (int i = 0; i < htmlTag.length; ++i) {
			if (InputString.indexOf(htmlTag[i]) != -1)
				InputString = InputString.replace(htmlTag[i], encodeTag[i]);
		}
		return InputString;
	}

	public String decodeHTML(String InputString) {
		if (InputString == null) {
			return null;
		}

		for (int i = 0; i < 5; ++i) {
			if (InputString.indexOf(encodeTag[i]) != -1) {
				InputString.replace(encodeTag[i], htmlTag[i]);
			}
		}
		return InputString;
	}

	public String changeTag(String content) {
		return encodeHTMl(content);
	}

	public String simpleFilte(String content) {
		
		content=content.replaceAll("<!--|<xmp|<scritp","");
		
		return content;
	}

	@SuppressWarnings("unused")
	public String filtrateEditorTag(String content) {
		for (int i = 5; i < htmlTag.length; ++i) {
			if (content.indexOf(htmlTag[i]) != -1)
				content=content.replaceAll(htmlTag[i], encodeTag[i]);
		}
		String contentLow = content.toLowerCase();
		Parser parser = Parser.createParser(contentLow, "gb2312");
		NodeList nodes;
		try {
			nodes = parser.extractAllNodesThatMatch(this.filter);
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
		int hasCut = 0;
		Node[] nodeArray = nodes.toNodeArray();
		StringBuffer sb = new StringBuffer(content);
		for (int i = 0; i < nodeArray.length; ++i) {
			TagNode tagNode = (TagNode) nodeArray[i];
			boolean cutTag = false;
			if (isBadName(tagNode)) {
				cutTag = true;
			} else {
				@SuppressWarnings("rawtypes")
				Vector vecBadAttribute = badAttribute(tagNode);
				if (vecBadAttribute != null) {
					@SuppressWarnings("rawtypes")
					Iterator iteraror = vecBadAttribute.iterator();
					lab: while (iteraror.hasNext()) {
						String badAttribute = (String) iteraror.next();
						int pos = contentLow.indexOf(badAttribute,
								tagNode.getTagBegin());
						if ((pos == -1) || (pos > tagNode.getTagEnd())) {
							cutTag = true;
							break lab;// lab 是后加的
						}
						sb.delete(pos - hasCut, pos + badAttribute.length()
								- hasCut);
						hasCut += badAttribute.length();
					}
				} else {
					cutTag = true;
				}
			}
			if (cutTag) {
				label248: sb.delete(tagNode.getTagBegin() - hasCut,
						tagNode.getTagEnd() - hasCut);
				hasCut += tagNode.getTagEnd() - tagNode.getTagBegin();
			}
		}
		return sb.toString();
	}

	private static boolean isBadName(TagNode tagNode) {
		String name = tagNode.getTagName().toLowerCase();
		if (name == null)
			return false;
		for (int i = 0; i < arrBadName.length; ++i) {
			if (name.indexOf(arrBadName[i]) != -1)
				return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	private static Vector<String> badAttribute(TagNode tagNode) {
		Vector vecAttribute = tagNode.getAttributesEx();
		Vector<String> vecBadAttribute = new Vector<String>();
		Set<String> setAttributeHead = new HashSet<String>();
		for (int i = 0; i < vecAttribute.size(); ++i) {
			String strAttribute = vecAttribute.elementAt(i).toString();
			if (strAttribute == null)
				continue;
			String strAttributeHead = null;
			if (strAttribute.length() <= 3) {
				continue;
			}
			strAttributeHead = strAttribute.substring(0, 3);
			if (setAttributeHead.contains(strAttributeHead)) {
				return null;
			}
			setAttributeHead.add(strAttributeHead);
			if (strAttribute.startsWith("on")) {
				vecBadAttribute.add(strAttribute);
			} else if (hasExpression(strAttribute)) {
				vecBadAttribute.add(strAttribute);
			} else if (badLink(strAttribute, tagNode)) {
				vecBadAttribute.add(strAttribute);
			} else {
				for (int j = 0; j < arrBadAttribute.length; ++j)
					if (strAttribute.contains(arrBadAttribute[j])) {
						vecBadAttribute.add(strAttribute);
						break;
					}
			}
		}
		return vecBadAttribute;
	}

	private static boolean badLink(String strAttribute, TagNode tagNode) {
		String strContent = null;
		for (int i = 0; i < arrBadLink.length; ++i) {
			if (strAttribute.startsWith(arrBadLink[i])) {
				strContent = tagNode.getAttribute(arrBadLink[i]);
				if ((!strContent.startsWith("http://"))
						&& (!strContent.startsWith("ftp://"))) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean hasExpression(String strAttribute) {
		int pos = 0;
		for (int i = 0; i < arrExpression1.length; ++i) {
			int pos1 = strAttribute.indexOf(arrExpression1[i], pos);
			int pos2 = strAttribute.indexOf(arrExpression2[i], pos);
			int pos3 = strAttribute.indexOf(arrExpression3[i], pos);
			if (pos1 == -1) {
				if (pos2 == -1) {
					if (pos3 == -1) {
						break;
					}
					pos = pos3;
				} else if (pos3 == -1) {
					pos = pos2;
				} else {
					pos = (pos2 < pos3) ? pos2 : pos3;
				}

			} else if (pos2 == -1) {
				if (pos3 == -1)
					pos = pos1;
				else {
					pos = (pos1 < pos3) ? pos1 : pos3;
				}
			} else if (pos3 == -1) {
				pos = (pos1 < pos2) ? pos1 : pos2;
			} else {
				pos = (pos1 < pos2) ? pos1 : pos2;
				pos = (pos < pos3) ? pos : pos3;
			}

			if (i >= arrExpression1.length - 1)
				return true;
		}
		return false;
	}

	public String filterForCSS(String content) {
		if (content == null)
			return null;
		content = content.replaceAll("/\\*.*\\*/", "");
		StringBuffer sbSrc = new StringBuffer(content);
		content = StandardTranformCharFilter.getInstance().toLowCase(content);
		StringBuffer sbChange = new StringBuffer(content);
		int pos = -1;
		for (int i = arrBadWordForCSS.length - 1; i >= 0; --i) {
			pos = sbChange.indexOf(arrBadWordForCSS[i]);
			while (pos != -1) {
				sbSrc.delete(pos, pos + arrBadWordForCSS[i].length());
				sbChange.delete(pos, pos + arrBadWordForCSS[i].length());
				pos = sbChange.indexOf(arrBadWordForCSS[i], pos + 1);
			}
		}
		return sbSrc.toString();
	}

	public static void main(String[] argc) {
		/*System.out.println(XSSFilter.getInstance().simpleFilte(
				"<!--<embed src='http://www.mop.com/a.swf'> </embed>"));*/
		// String content =
		// "SP公司在转型娱乐中，不管是版权概念，3G概念，还是艺人概念，最后在未来两年内都将破产。这都是因为每次转型都抓不住娱乐产业的大趋势，结果每次转型都转到一个被娱乐产业链淘汰的环节，导致刚转型还没站稳就被市场抛弃。 那么如何才能抓住娱乐产业趋势呢？很简单，需要人！也就是了解娱乐产业的操盘手。否则，就永远抓不到娱乐产业的本质，就一直找不到具体的方向。 纵观SP们向娱乐的转型，往往是在战略上看似行得通，结果在执行上却一直失败，以后也注定会失败！因为他们清一色犯了致命的错误： 有战略，无战术。 要成功运作一个娱乐集团，必须满足这三大要素：“概念（模式）+内容（版权和艺人）+营销”，三者缺一不可。 但是SP公司在转型娱乐中，一直都存在“重概念，轻内容，无营销”的弊端。具体的来讲，就是SP公司讲起概念和模式来都头头是道，每一个SP人都可以想出转型娱乐的这个战略，所以SP公司们的战略其实是最不值钱的。他们缺的是内容，所以SP公司开始转型娱乐，让自己由渠道商转向内容商。但是，有了概念、有了内容也没有用，因为，大家都缺一样东西——营销。 这里面相对应的关系是：战略就是要有清晰的概念（模式），战术就是内容（版权和艺人）运作能力要强，而战略和战术的统一，就是营销在里面所起的化学中和作用。 什么是战略，什么是战术？ 通俗地讲：战略是公司要转型娱乐，做唱片、做经纪、做影视，做娱乐产业链，做传媒帝国；而战术是怎么样把一首歌和一个艺人推红。现在SP公司普遍弊端是“有战略没战术”，而传统唱片公司普遍的弊端是“有战术没战略”。 战略是“面”，战术是服务于战略下面的一些“点”。要想成功，一定是要把战术提升到战略高度来运营，并要与战略保持一致性。娱乐产业跟其他行业相比，战术更重要，甚至战略是由战术提炼而成的。并不是说我要转型娱乐就可以转型娱乐，也并不是说我要做唱片、做艺人、做经纪、做影视就可以搭建成娱乐王国。 简言之，并不是战略对了就会成功。需要战术支撑起战略，否则战略就是空中楼阁。这个战术就是怎么样把歌曲做火，怎么样把一个低价签过来的二线艺人咸鱼大翻身。如果你没有这个本领，转型娱乐的战略是行不通的。而SP公司在具体战术上，因为公司在理念、人才等各种不足，而无法支撑战略的实现。我们都说老外不了解中国国情。其实SP公司做娱乐，比不懂国情的老外好不到哪去。 SP公司到现在还不明白娱乐产业的本质 SP公司们转型娱乐的失败，除了没有抓住娱乐产业的大趋势以外，还没有抓住娱乐产业的本质。空有战略，没有对娱乐产业本质的了解，就不会有不断正确的战术支持，战略目标就必定达不成。 那么娱乐产业的本质是什么呢？是垄断！娱乐产业的核心是什么呢？是品牌！娱乐产业的盈利靠什么呢？是大片模式。SP公司在这几个关键问题上都没做到。 娱乐产业的本质是垄断。垄断不了全部，就要垄断一个细分市场，并且这个细分市场是娱乐产业价值链中最高的一个环节。 但是，现在的SP公司们即便知道了娱乐产业的本质，也没有办法达到战略目标。因为： 人才短缺是娱乐公司的致命伤 说了这么多战略和战术的关系，我们顺着这个逻辑往下演进，缺乏战术的原因是缺乏懂运营的高手。但是这些高手都在唱片公司担任老总，不可能屈就了，并且由于年龄比较大，不懂IT并且这辈子可能不会去学IT了。另一方面，在SP公司内部，这些年却一直忽略了对跨行业人才的培养。并且同跨国公司不善于本土化一样，SP公司也按某种IT标准来衡量用人，更是以娱乐人才的草根性、与IT行业思维方式不一样而为其设置了天花板。加上IT和唱片两个行业有不同的游戏规则和思维方式，造成文化冲突：你嫌我草根，我觉得你不懂艺术。并且，仅仅懂IT和仅仅懂艺术都不够，还需要懂营销来中和一下。所以，具备三个条件（IT、艺术、现代化营销）的人真是难上加难。必须培养跨行业的人才，娱乐公司才能成功。";
		// System.out.println(content);
		// long now = System.currentTimeMillis();
		// for (int i = 0; i < 10000; ++i) {
		// getInstance().filtrateEditorTag(content);
		// }
		// System.out.println(System.currentTimeMillis() - now);
	}

}
