package com.malykh.common.swing.exception;

import com.malykh.common.swing.CommonRB;

/**
 * @author Anton Malykh
 */
public class ErrorHandler
{
    public void handle(Throwable e)
    {
        ErrorMessageDialog dia =
                new ErrorMessageDialog(null, CommonRB.get("exception.swing.error.title"),
                                       e);
        dia.setVisible(true);
    }

    public static void installYourself()
    {
        System.setProperty("sun.awt.exception.handler",
                           ErrorHandler.class.getName());
    }
}
