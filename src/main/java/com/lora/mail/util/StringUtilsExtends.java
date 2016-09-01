package com.lora.mail.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;



public class StringUtilsExtends extends StringUtils {
	public static String jsEncode(String str) {
        if(StringUtils.isBlank(str)) {
            return "";
        }

        String result = replace(str, "\\", "\\\\");
        result = replace(result, "\n", "\\n");
        result = replace(result, "\r", "\\r");
        result = replace(result, "\t", "\\t");
        result = replace(result, "\"", "\\\"");

        return result;
    }
	
	/**
	 * get object string.
	 * 
	 * @param obj
	 *            object.
	 * @return string
	 */
	public static String getObjString(Object obj) {
		if (null == obj)
			return "";
		else
			return obj.toString();
	}
	
	/**
	 * 判断字符串是非为空
	 * 
	 * @param str
	 * @return 空返回true 非空返回false
	 */
	public static boolean isBlank(String str) {
		return (str == null ||str.trim().length() == 0);
	}

	/**
	 * 判断字符串是否非空
	 * 
	 * @param str
	 * @return 非空返回true 空返回false
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	/**
	 * 判断是否是数字
	 * 
	 * @param str
	 * @return 数字返回true 非数字返回false
	 */
	public static boolean isNum(String str) {
		if (isBlank(str)) {
			return false;
		}

		String regEx = "[\\d]+";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);

		return m.matches();

	}

	/**
	 * 判断是否是中文字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		String regEx = "[\u4e00-\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);

		return m.matches();
	}

	/**
	 * 判断是是否含有特殊字符
	 * 
	 * @param input
	 * @return 特殊字符返回true 非特殊字符返回false
	 */
	public static boolean hasSpecialChars(String str) {
		boolean flag = false;
		if ((str != null) && (str.length() > 0)) {
			char c;
			for (int i = 0; i <= str.length() - 1; i++) {
				c = str.charAt(i);
				switch (c) {
				case '>':
					flag = true;
					break;
				case '<':
					flag = true;
					break;
				case '"':
					flag = true;
					break;
				case '&':
					flag = true;
					break;
				case '+':
					flag = true;
					break;
				case '!':
					flag = true;
					break;
				case '$':
					flag = true;
					break;
				case '^':
					flag = true;
					break;
				case '*':
					flag = true;
					break;
				case '#':
					flag = true;
					break;
				case ' ':
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 判断用户名是否合法（可以是中文、英文或数字(不能全部是数字)组成，长度为2~18个字符）
	 * 
	 * @param str
	 * @return 合法返回true 非法返回false
	 */
	public static boolean isVaildLoginName(String str) {
		if (isBlank(str)) {
			return false;
		}
		String regEx = "[^_a-zA-Z0-9\u4e00-\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find() || str.getBytes().length < 6 || str.getBytes().length > 20) {
			return false;
		}
		
		String regEx2 = "[\\d]+";
		if(str.matches(regEx2)){
			return false;
		}

		return true;
	}
	
	/**
	 * 判断昵称是否合法
	 * 
	 * @param str
	 * @return 合法返回true 非法返回false
	 */
	public static boolean isVaildNickName(String str) {
		if (isBlank(str)) {
			return false;
		}

		if (str.getBytes().length < 2 || str.getBytes().length > 18) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否和合法的Email
	 * 
	 * @param str
	 * @return 合法返回true 非法返回false
	 */
	public static boolean isVaildEmail(String str) {
		if (isBlank(str)) {
			return false;
		}

		// 检测是否以"www."为起始
		Pattern p = Pattern.compile("www.(.+)");
		Matcher m = p.matcher(str);
		if (m.matches()) {
			return false;
		}

		// 检测Email是否合法
		p = Pattern
				.compile("^([a-zA-Z0-9_-])+(\\.)?([a-zA-Z0-9_-])*@([a-zA-Z0-9_-])+(\\.)([a-zA-Z0-9_-])+(\\.)?([a-zA-Z0-9_-])+$");
		m = p.matcher(str);
		if (!m.find()) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String processSpace(String str) {
		if (StringUtilsExtends.isNotBlank(str)) {
			str = str.trim();
			str = str.replaceAll("　", " ");
			String regEx = "\\s+";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(str);
			String ret = m.replaceAll(" ");
			return ret;
		} else {
			return str;
		}

	}
	
	public static String htmlEncoder(String content) {
		content = content.replaceAll("\n", "<br>");		
		content = content.replaceAll("<", "&lt");
		content = content.replaceAll(">", "&gt");
		content = content.replaceAll(" ", "&nbsp;");
		return content;
	}

	public static String htmlDecoder(String content) {
		if (content == null) {
			return "";
		}
		content = content.replaceAll("<br>", "");
		content = content.replaceAll("\n", "");
		content = content.replaceAll("\r", "");
		content = content.replaceAll("<", "");
		content = content.replaceAll(">", "");
		return content;
	}
	
	public static String Html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			htmlStr = htmlStr.replace("&nbsp;", "").replaceAll("<", "").replaceAll(">", "");
			
			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text().Html2Text: " + e.getMessage());
		}

		return textStr;// 返回文本字符串
	}
	 public static String splitAndFilterString(String input, int length,String tip) {  
         if (input == null || input.trim().equals("")) {  
             return "";  
         }  
         // 去掉所有html元素,  
         input=StringUtilsExtends.trimToNull(input);
         String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(  
                 "<[^>]*>", "").replaceAll(".br","").replaceAll(".p", "");  
         str = str.replaceAll("[(/>)<]", "");  
         int len = str.length();  
         if (len <= length) {  
             return str;  
         } else {  
             str = str.substring(0, length);  
             str +=tip;  
         }  
         return str;  
     }  
		
	public static String subString(String str,int num,String token){
		String str1=str;
		if(str!=null && str.trim().length()>num){
			str1=str.substring(0, num);
		}
		if(token!=null){
			str1=str1+token;
		}
		return str1;
	}
	
	
	//截取字符串
	public static String subString(String str, int bytes) { 
		int cutLength = 0; 
		int byteNum = bytes; 
		byte bt[] = str.getBytes(); 
		if (bytes > 1) { 
		for (int i = 0; i < byteNum; i++) { 
		if (bt[i] < 0) { 
		cutLength++; 

		} 
		} 
		if (cutLength % 2 == 0) { 
		cutLength /= 2; 
		}else 
		{ 
		cutLength=0; 
		} 
		} 
		int result=cutLength+--byteNum; 
		if(result>bytes) 
		{ 
		result=bytes; 
		} 
		if (bytes == 1) { 
		if (bt[0] < 0) { 
		result+=2; 

		}else 
		{ 
		result+=1; 
		} 
		} 
		String substrx = new String(bt, 0, result); 
		return substrx; 
		}
	
	public static String replace(String str){
		String reg_script="\\s|\t|\r|\n";
		Pattern p=Pattern.compile(reg_script, Pattern.MULTILINE );
		Matcher m=p.matcher(str); 
		return m.replaceAll("").replaceAll("'", "’").replaceAll("\"", "”").replaceAll("\\\\", "、");
	}
	
	
	
	
	public static String getTagName(String[] tagId){
		StringBuffer sb = new StringBuffer();
		if(tagId == null) return sb.toString();
		sb.append(tagId[0]);
		for(int i = 1 ; i < tagId.length ; i++){
			sb.append("/" + tagId[i]);
		}
		return sb.toString();
	}
	
	public static String getTagName(String tagName){
		if("".equals(tagName))
			tagName = null;
		if(null == tagName)
			return null;
		return tagName.replace("/", " ");
	}
	
	//url编码
	public static String encodeStr(String s){
		if(null == s)
			s = "";
		if(null != s){
			try {
				s = URLEncoder.encode(s ,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return s;
	}
	
	//url解码
	public static String decodeStr(String s){
		if(null == s)
			s = "";
		if(null != s){
			try {
				s = URLDecoder.decode(s ,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return s;
	}
	
	

	public static String subzn(String s, int len) {
		if (len <= 0) {
			return "";
		}
		// 从右往左找到第一个小于0的byte，然后往右推到len的位置，如果是一个奇数，那么加1
		byte[] bs = s.getBytes();

		// 1. 找到第一个非中文字节
		if (len > bs.length) {
			len = bs.length;
		}
		int n = len - 1;
		while (bs[n] < 0) {
			n = n - 1;
			if (n < 0) {
				break;
			}
		}
		// n 是第一个中文字节
		n = n + 1;

		// 2. 从中文字节算起到len，是奇数还是偶数
		int r = len - n;
		if (r % 2 == 1) {
			len = len - 1;
		}

		byte[] bs2 = new byte[len];
		for (int i = 0; i < bs2.length; i++) {
			bs2[i] = bs[i];
		}
		return new String(bs2);
	}
	
	
	


	
	
	
	
	//根据后缀转换文件名
	public static String turnFileNameByPostFix(String sourse,String postFix)
	{
		return StringUtilsExtends.substringBeforeLast(sourse, ".")+"."+postFix;
	}
	
	public static String abbreviate(String str,int maxLength,String isTip){
		if(isTip.equals(isTip)){
		return StringUtilsExtends.abbreviate(str, maxLength);
		}
		else{
			return StringUtilsExtends.substringBefore(StringUtilsExtends.abbreviate(str, maxLength), ".");
		}
	}
	public static String subString2(String str,int maxLength,String isTip){
		if(StringUtilsExtends.isNotBlank(StringUtilsExtends.trimToNull(str))){
			if(str.length()>maxLength){
				return StringUtilsExtends.substring(str,0,maxLength)+isTip;
			}
			else{
				return str;
			}
		}
		return "";
	}
	
	public static String getStrictIpAddress(String str, boolean flag) {
		String reg = "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)";
		String reg2 = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
		Pattern p = null;
		if (flag) {
			p = Pattern.compile(reg);
		} else {
			p = Pattern.compile(reg2);
		}
		if(isNotBlank(str)){
			Matcher m = p.matcher(str);
			if (m.find()) {
				return m.group();
			}
		}
		return null;
	}
	
	public static int getContainCount(String str, String searchChars, int count) {
		int allCounter = count;
		if (isNotBlank(str) && isNotBlank(searchChars)) {
			int length = searchChars.length();
			if (str.indexOf(searchChars) == -1) {
				return allCounter;
			} else if (str.indexOf(searchChars) != -1) {
				allCounter++;
				allCounter = getContainCount(
						str.substring(str.indexOf(searchChars) + length),
						searchChars, allCounter);
				return allCounter;
			}
		}
		return 0;
	}

    public static void main(String[] args) {
        
    }
}
