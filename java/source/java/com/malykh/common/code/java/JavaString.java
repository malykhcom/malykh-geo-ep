package com.malykh.common.code.java;

/**
 * @author Anton Malykh
 */
public class JavaString
{
    public static String javaString(String str)
    {
        if (str == null)
            return "null";
        return '\"'+escape(str)+'\"';
    }
    public static String escapeToUnicode(char c)
    {
        String hex = Integer.toHexString((int) c);
        String leadingZeroes = "0000".substring(hex.length());
        return "\\u"+leadingZeroes +hex;
    }
    public static String escape(String str)
    {
        StringBuilder sb = new StringBuilder(str.length());
        for (final char c : str.toCharArray())
        {
            switch (c)
            {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < ' ')
                        sb.append(escapeToUnicode(c));
                    else
                        sb.append(c);
            }
        }
        return sb.toString();
    }

}
