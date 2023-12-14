package com.malykh.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Anton Malykh
 */
public class RussianTransliteration
{
    static final Map<Character, String> table = new HashMap<Character, String>();
    static
    {
        String rus = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        String lat = "a b v g d e e zhz i y k l m n o p r s t u f khtschshsh\" y \' e yuya";
        for (int t = 0; t < rus.length(); t++)
        {
            char ch = rus.charAt(t);
            String s = lat.substring(t*2, (t+1)*2).trim();
            table.put(ch, s);
            table.put(Character.toUpperCase(ch), Character.toUpperCase(s.charAt(0))+s.substring(1));
        }
    }
    public static String transliterate(String str)
    {
        StringBuilder sb = new StringBuilder(str.length());
        for (char ch : str.toCharArray())
        {
            final String s = table.get(ch);
            if (s != null)
                sb.append(s);
            else
                sb.append(ch);
        }
        return sb.toString();
    }
    public static void main(String[] args)
    {
        String t = "АзбУка-ЭЮЯ!эюя!abc";
        System.out.println(t);
        System.out.println(transliterate(t));
        System.out.println(transliterate("Маритуй"));
    }
}
