import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;

public class CSSParserUtils {
	private static Logger log = Logger.getLogger(CSSParserUtils.class);
	public static String URIPath = "file:///" + "";

	public static void getCssList(String path, ArrayList<String> list,
			String fileName) throws Exception {
		File file = new File(path);
		File[] files = file.listFiles();
		if (files != null && files.length != 0) {
			for (File f : files) {
				if (f.isFile()) {
					if (f.getAbsolutePath().endsWith(fileName)
							&& f.getAbsolutePath().indexOf("config") != -1) {
						list.add(f.getAbsolutePath().replaceAll("\\\\", "/"));
						System.out.println(f.getAbsolutePath());
					}
				} else if (f.isDirectory()) {
					if (!f.getAbsolutePath().endsWith(".svn")
							&& !f.getAbsolutePath().endsWith("image")) {
						getCssList(f.getAbsolutePath(), list, fileName);
					}
				}
			}
		}
	}

	/**
	 * ����֮��
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> paths = new ArrayList<String>();
//		paths.add("D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/poi_service/trunk/config/device/ATTNAVPROG");
//		paths.add("D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/movie/trunk/config/device/ATTNAVPROG");
//		paths.add("D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/login/trunk/config/device/ATTNAVPROG");
//		paths.add("D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/CommuteAlerts/trunk/config/device/ATTNAVPROG");
//		paths.add("D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/poi_service/trunk/config/device/SNNAVPROG");
//		paths.add("D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/movie/trunk/config/device/SNNAVPROG");
//		paths.add("D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/login/trunk/config/device/SNNAVPROG");
//		paths.add("D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/CommuteAlerts/trunk/config/device/SNNAVPROG");
		 paths.add("D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/poi_service/trunk/config/device/");
		 paths.add("D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/movie/trunk/config/device/");
		 paths.add("D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/login/trunk/config/device/");
		 paths.add("D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/CommuteAlerts/trunk/config/device/");
		String fileName = "prog.css";
		for (String path : paths) {
			getCssList(path, list, fileName);
		}
		System.out.println("csss\n" + list);
//		Map map = new HashMap();
//		for (String fileLocation : list) {
//			parserCSSSelectors(fileLocation, map);
//		}
//		System.out.println(map.keySet());
//		System.out.println(map);
//		int i = 0;
//		Map map2 = new HashMap();
//		for (Iterator<String> iterator = map.keySet().iterator(); iterator
//				.hasNext();) {
//			String string = (String) iterator.next();
//			HashSet set = (HashSet) map.get(string);
//			map2.put(string, set.size());
//			i += set.size();
//		}
//		exprotCss(map);
//		System.out.println(map.size());
//		System.out.println(i);
//		System.out.println(map2);
		// String fileLocation =
		// "D:/SVN/shsvn.china.telenav.com/tnclient-java/J2ME/telenav60_server/CommuteAlerts/trunk/config/device/default/default/7_1_0/default/320x240_240x320/common/style/Style.css";
		// System.out.println(parserCSSSelectors(fileLocation));

	}

	public static void exprotCss(Map map) {
		try {
			File csv = new File("/prog_attnavprog.csv"); // CSV�����ļ�

			BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true)); // ����
			// ����µ�������
			for (Iterator<String> iterator = map.keySet().iterator(); iterator
					.hasNext();) {
				String s = (String) iterator.next();
				bw.write("\"" + s + "\"" + ",");
				HashSet set = (HashSet) map.get(s);
				bw.write("\"" + set.size() + "\"");
				for (Iterator it2 = set.iterator(); it2.hasNext();) {
					String style = (String) it2.next();
					bw.write(",");
					bw.write("\"" + style + "\"");
				}
				bw.newLine();
			}
			bw.close();

		} catch (FileNotFoundException e) {
			// File����Ĵ��������е��쳣����
			e.printStackTrace();
		} catch (IOException e) {
			// BufferedWriter�ڹرն���׽�쳣
			e.printStackTrace();
		}
	}

	/**
	 * ����������url��Ӧ��ҳ���ļ��е�cssѡ����
	 * 
	 * @param url
	 *            ����ҳ���ļ�url
	 * @param map
	 * @return cssѡ�����б�
	 */
	public static List<String> parserCSSSelectors(String url, Map map) {
		return parserCSSSelectors(null, url, map);
	}

	/**
	 * �ϲ�����������һ��ͨ�õķ���
	 * 
	 * @param read
	 *            �����Ϊnull��������Ϊ��
	 * @param url
	 *            �����Ϊnull�����ʾҳ���ļ���url
	 * @return cssѡ�����б�
	 */
	public static List<String> parserCSSSelectors(Reader read, String url,
			Map map) {
		CSSOMParser cssparser = new CSSOMParser();
		CSSStyleSheet css = null;
		try {
			if (read == null)
				css = cssparser.parseStyleSheet(new InputSource(URIPath + url),
						null, null);
			else
				css = cssparser.parseStyleSheet(new InputSource(read), null,
						null);
		} catch (IOException e) {
			log.error("����css�ļ��쳣", e);
			return null;
		}

		return parseCSSGeneral(url, css, map);
	}

	private static List<String> parseCSSGeneral(String url, CSSStyleSheet css,
			Map map) {
		CSSRuleList cssrules = css.getCssRules();

		if (cssrules == null)
			return null;

		List<String> selectors = new ArrayList<String>();
		for (int i = 0; i < cssrules.getLength(); i++) {
			CSSRule rule = cssrules.item(i);
			// System.out.println(rule.getCssText());
			if (rule instanceof CSSStyleRule) {
				CSSStyleRule cssrule = (CSSStyleRule) rule;
				// System.out.println(cssrule.getSelectorText());
				// System.out.println(cssrule.getStyle());
				if (null == map.get("|" + cssrule.getSelectorText() + "|")) {
					Set set1 = new HashSet();
					set1.add("{" + cssrule.getStyle() + "}");
					map.put("|" + cssrule.getSelectorText() + "|", set1);
				} else {
					Set set1 = (Set) map.get("|" + cssrule.getSelectorText()
							+ "|");
					set1.add("{" + cssrule.getStyle() + "}");
					map.put("|" + cssrule.getSelectorText() + "|", set1);
				}

				// processFilter(selectors, new Filter<String>() {
				//
				// public void accept(List<String> list, String t) {
				// if (t.indexOf(".") != -1) {
				// String[] result = t.split("\\s");
				// for (int x = 0; x < result.length; x++) {
				// System.out.println("result[" + x + "]"
				// + result[x]);
				// int index = result[x].indexOf(".");
				// if (index != -1) {
				// list.add(result[x].substring(index + 1));
				// }
				// }
				//
				// }
				//
				// }
				//
				// }, cssrule.getSelectorText());
			} else if (rule instanceof CSSImportRule) {
				CSSImportRule cssrule = (CSSImportRule) rule;
				selectors.addAll(parserCSSSelectors(
						processURL(url, cssrule.getHref()), map));
			}

		}
		return selectors;
	}

	/**
	 * ����url
	 * 
	 * @param url
	 *            ԭʼurl
	 * @param dest
	 *            Ŀ��url
	 * @return
	 */
	public static String processURL(String url, String dest) {
		int indexWin = url.lastIndexOf("\\");
		int indexUnix = url.lastIndexOf("/");

		int index = indexWin == -1 ? indexUnix : indexWin;
		if (index != -1) {
			String ret = url.substring(0, index) + "/" + dest;
			return ret;
		}

		return dest;
	}

	public static void processFilter(List<String> selectors,
			Filter<String> selectorFilter, String selectorText) {
		selectorFilter.accept(selectors, selectorText);
	}

}