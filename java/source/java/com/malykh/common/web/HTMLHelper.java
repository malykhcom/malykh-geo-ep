package com.malykh.common.web;

import java.io.Writer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Anton Malykh
 */
public class HTMLHelper
{
    protected static final Map<String, Character> fromEntity = new HashMap<String, Character>();
    protected static final Map<Character, String> toEntity = new HashMap<Character, String>();
    protected static void addEntity(char ch, String string)
    {
        toEntity.put(ch, '&'+string+";");
        fromEntity.put(string, ch);
    }
    static
    {
        addEntity('<', "lt");
        addEntity('>', "gt");
        addEntity('\'', "apos");
        addEntity('\"', "quot");
        addEntity('&', "amp");
        for (short c = 0; c < 0x20; c++)
        {
            char ch = (char) c;
            String s = Integer.toHexString((c & 0xff));
            if (s.length() == 1)
                s = '0'+s;
            addEntity(ch, "#x"+s);
        }
    }
    public static String unescape(String htmlText)
    {
        int len = htmlText.length();
        StringBuilder sb = new StringBuilder(len);
        StringBuilder entity = null;
        for (int t = 0; t < len; t++)
        {
            char ch = htmlText.charAt(t);
            if (entity != null)
            {
                if (ch == ';')
                {
                    Character entCh = fromEntity.get(entity.toString());
                    if (entCh == null)
                        sb.append('?');
                    else
                        sb.append(entCh);
                    entity = null;
                }
                else
                {
                    entity.append(ch);
                }
            }
            else
            {
                if (ch == '&')
                {
                    entity = new StringBuilder();
                }
                else
                {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }
    //
    public static String escapeLight(String text)
    {
        int len = text.length();
        StringBuilder sb = new StringBuilder(len);
        for (int t = 0; t < len; t++)
        {
            char ch = text.charAt(t);
            if (ch == '\r' || ch == '\n')
                sb.append(ch);
            else
            {
                String entity = toEntity.get(ch);
                if (entity != null)
                    sb.append(entity);
                else
                    sb.append(ch);
            }
        }
        return sb.toString();
    }
    public static String escape(String text)
    {
        int len = text.length();
        StringBuilder sb = new StringBuilder(len);
        for (int t = 0; t < len; t++)
        {
            char ch = text.charAt(t);
            String entity = toEntity.get(ch);
            if (entity != null)
                sb.append(entity);
            else
                sb.append(ch);
        }
        return sb.toString();
    }
    public static void escape(Writer writer, String text) throws IOException
    {
        int len = text.length();
        for (int t = 0; t < len; t++)
        {
            char ch = text.charAt(t);
            String entity = toEntity.get(ch);
            if (entity != null)
                writer.write(entity);
            else
                writer.write(ch);
        }
    }
    public static void escape(Writer writer, char[] text, int off, int len) throws IOException
    {
        for (int t = 0; t < len; t++)
        {
            char ch = text[off+t];
            String entity = toEntity.get(ch);
            if (entity != null)
                writer.write(entity);
            else
                writer.write(ch);
        }
    }
    //
    protected static String encode(String enc, char ch) throws UnsupportedEncodingException
    {
        String s = String.valueOf(ch);
        byte[] b = s.getBytes(enc);
        s = Integer.toHexString((b[0] & 0xff));
        if (s.length() == 1)
            s = '0'+s;
        return s;
    }
    public static String encodeParam(String enc, String param) throws UnsupportedEncodingException
    {
        int len = param.length();
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < len; t++)
        {
            char ch = param.charAt(t);
            if (ch == ' ')
                sb.append('+');
            else if ((ch >= 'a' && ch <= 'z') ||
                     (ch >= 'A' && ch <= 'Z') ||
                     (ch >= '0' && ch <= '9'))
                sb.append(ch);
            else
                sb.append('%').append(encode(enc, ch).toUpperCase());
        }
        return sb.toString();
    }
    protected static String decode(String enc, String code) throws Exception
    {
        int i = Integer.parseInt(code, 16);
        byte[] b = new byte[] { (byte)i };
        return new String(b, enc);
    }
    public static String decodeParam(String enc, String param) throws Exception
    {
        int len = param.length();
        StringBuilder sb = new StringBuilder();
        int t = 0;
        while (t < len)
        {
            char ch = param.charAt(t);
            if (ch == '%')
            {
                if (t+2 < len)
                {
                    StringBuilder code = new StringBuilder(2);
                    code.append(param.charAt(t+1));
                    code.append(param.charAt(t+2));
                    sb.append(decode(enc, code.toString()));
                    t += 2;
                }
                else
                {
                    throw new Exception();
                }
            }
            else if (ch == '+')
            {
                sb.append(' ');
            }
            else
            {
                sb.append(ch);
            }
            t++;
        }
        return sb.toString();
    }

    /**
     * Разбитие по словам с учетом кавычек.
     * @param text текст
     * @return список слов
     */
    public static List<String> smartSplit(String text)
    {
        boolean word = false;
        StringBuilder lastWord = new StringBuilder();
        List<String> words = new ArrayList<String>();
        for (char c : text.toCharArray())
        {
            if (c == '"')
            {
                word = !word;
            }
            else if (c == ' ' || c == '\t')
            {
                if (word)
                    lastWord.append(c);
                else if (lastWord.length() > 0)
                {
                    words.add(lastWord.toString());
                    lastWord = new StringBuilder();
                }

            }
            else
            {
                lastWord.append(c);
            }
        }
        if (lastWord.length() > 0)
            words.add(lastWord.toString());
        return words;
    }
    // test
    public static void main(String[] args) throws Exception
    {
        String text = "Привет! abc";
        String enc = "windows-1251";
        String e = encodeParam(enc, text);
        String d = decodeParam(enc, e);
        System.out.println(text);
        System.out.println(e);
        System.out.println(d);
        String t = "Текс, использующий <> и \"'&, \n\r \u0007";
        System.out.println(t);
        System.out.println(escape(t));
        System.out.println(unescape(escape(t)));
        System.out.println(unescape("&"));
        System.out.println(unescape("1&lll;2"));
        System.out.println(smartSplit("\"first word\"   second \"third"));
    }

}
