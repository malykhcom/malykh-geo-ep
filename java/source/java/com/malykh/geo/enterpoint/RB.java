package com.malykh.geo.enterpoint;

import java.util.ResourceBundle;

/**
 * @author Anton Malykh
 */
public class RB
{
    protected static final ResourceBundle rb =
            ResourceBundle.getBundle("com.malykh.geo.enterpoint.messages");

    public static String get(String key)
    {
        if (rb.containsKey(key))
            return rb.getString(key);
        return key;
    }

}
