package com.dx.common.core.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Slf4j
public class ValidUtil {

    private final static String[] PATTEN = new String[]{
            "AAAAAAA A (\\d)\\1{6} 1",
            "AAAAAA A (\\d)\\1{5} 2",
            "AABBCC A (\\d)\\1((?!\\1)\\d)\\2((?!\\2)(?!\\1)\\d)\\3 3",
            "AAABBB A (\\d)\\1{2}((?!\\1)\\d)\\2{2} 4",
            "ABABAB A (\\d)((?!\\1)\\d)(\\1\\2){2} 5",
            "ABCABC A ((\\d)((?!\\2)\\d)((?!\\2)(?!\\3)\\d))\\1 6",
            "ABCDEF A (?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){5}\\d 7",
            "FEDCBA A (?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){5}\\d 8",
            "ABCCBA A (\\d)((?!\\2)\\d)((?!\\2)(?!\\3)\\d)\\3\\2\\1 9",
            "ABCFABC A ((\\d)((?!\\2)\\d)((?!\\2)(?!\\3)\\d))(\\d+)\\1 10",
            "ABCFCBA A (\\d)((?!\\2)\\d)((?!\\2)(?!\\3)\\d)(\\d+)\\3\\2\\1 11",
            "AAAAA A (\\d)\\1{4} 12",
            "ABCDE A (?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){4}\\d 13",
            "EDCBA A (?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){4}\\d 14",
            "ABCD E (?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){3}\\d 15",
            "AAAA E (\\d)\\1{3} 16",
            "ABBB E (\\d)((?!\\1)\\d)\\2{2} 17",
            "AAAB E (\\d)\\1{2}((?!\\1)\\d) 18",
            "AABB E (\\d)\\1{1}((?!\\1)\\d)\\2{1} 19",
            "ABAB E (\\d)((?!\\1)\\d)(\\1\\2){1} 20"
    };

    public static boolean isNiceNo(String no) {
        for (String modeDef : PATTEN) {
            String[] split = modeDef.split(" ");
            String pattern = "";
            if ("E".equals(split[1])) {
                pattern = ".*" + split[2] + "$";
            } else if ("A".equals(split[1])) {
                pattern = ".*" + split[2] + ".*";
            }

            boolean isMatch = Pattern.matches(pattern, no);
            if (isMatch) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        return m.matches();
    }

    /**
     * 大陆号码或香港号码都可以
     *
     * @param str
     * @return 符合规则返回true
     * @throws PatternSyntaxException
     */
    public static boolean isPhoneLegal(String str) {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     *
     * @param str
     * @return 正确返回true
     * @throws PatternSyntaxException
     */
    public static boolean isChinaPhoneLegal(String str) {
        // ^ 匹配输入字符串开始的位置
        // \\d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     *
     * @param str
     * @return 正确返回true
     * @throws PatternSyntaxException
     */
    public static boolean isHKPhoneLegal(String str) {
        // ^ 匹配输入字符串开始的位置
        // \\d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^([5689])\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
