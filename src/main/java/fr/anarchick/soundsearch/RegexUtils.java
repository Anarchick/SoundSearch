package fr.anarchick.soundsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	
	public static boolean regexMatch(String pattern, String matcher) {
        Pattern p = Pattern.compile(pattern);
        return p.matcher(matcher).find();
    }
    
    public static List<String> getRegexGroup(String pattern, String matcher, int group) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(matcher);
        List<String> list = new ArrayList<>();
        while (m.find()) {
        	if (m.groupCount() <= group) {
        		list.add(m.group(group));
        	}
        }
        return list;
    }

}
