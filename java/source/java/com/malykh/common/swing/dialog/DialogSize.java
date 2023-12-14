package com.malykh.common.swing.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public class DialogSize
{
    protected static int calcSize(int size, int minNormal)
    {
        if (size <= minNormal)
            return size;
        int ret = size * 8 / 10;
//        System.out.println(ret+","+minNormal);
        return Math.max(ret, minNormal);
    }
    public static Dimension calcViewSize()
    {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = localGraphicsEnvironment.getMaximumWindowBounds();
//        System.out.println(bounds);
        int w = bounds.width;
        int h = bounds.height;
        return new Dimension(calcSize(w, 1024), calcSize(h, 768));
        //return new Dimension(calcSize(w, 2000), calcSize(h, 2000));
    }
    public static void main(String[] args)
    {
        JDialog d = new CloseButtonDialog(null, "1111", new JLabel("1111"));
        Dimension size = calcViewSize();
//        System.out.println(size);
        d.setSize(size);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }
}
