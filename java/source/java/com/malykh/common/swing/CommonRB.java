package com.malykh.common.swing;

import java.util.ResourceBundle;

/**
 * @author Anton Malykh
 */
public class CommonRB
{
    protected static final ResourceBundle rb = ResourceBundle.getBundle("com.malykh.common.swing.common-messages");

    public static String get(String key)
    {
        if (rb.containsKey(key))
            return rb.getString(key);
        return key;
    }
}
