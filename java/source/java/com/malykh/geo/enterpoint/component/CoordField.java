package com.malykh.geo.enterpoint.component;

import com.malykh.geo.enterpoint.Help;
import com.malykh.geo.kml.Point;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Anton Malykh
 */
public class CoordField extends JTextField
{
    private final boolean dmsMode;
    public CoordField(boolean dmsMode)
    {
        this.dmsMode = dmsMode;
    }

    public void setDouble(double d)
    {
        if (dmsMode)
        {
            int[] dms = Point.getDMS(d);
            setText(String.valueOf(dms[0])+' '+dms[1]+' '+dms[2]);
        }
        else
        {
            String val = String.valueOf(d);
            if (val.endsWith(".0"))
                val = val.substring(0, val.length()-1);
            setText(val);
        }
    }
    public Double getDouble()
    {
        try
        {
            if (dmsMode)
            {
                String text = getText();
                text = text.replace('.', ' ');
                text = text.replace(',', ' ');
                text = text.trim();
                int index1 = text.indexOf(' ');
                int index2 = text.lastIndexOf(' ');
                int d;
                int m = 0;
                int s = 0;
                if (index1 == -1)
                {
                    d = Integer.parseInt(text);
                }
                else
                {
                    if (index2 == index1)
                    {
                        d = Integer.parseInt(text.substring(0, index1).trim());
                        m = Integer.parseInt(text.substring(index1+1).trim());
                    }
                    else
                    {
                        d = Integer.parseInt(text.substring(0, index1).trim());
                        m = Integer.parseInt(text.substring(index1+1, index2).trim());
                        s = Integer.parseInt(text.substring(index2+1).trim());
                    }
                }
                if (d >= 0 && d < 360 &&
                    m >= 0 && m < 60 &&
                    s >= 0 && s < 60)
                    return Point.getD(d, m, s);
            }
            else
            {
                String text = getText().trim();
                text = text.replace(',', '.');
                text = text.replace(' ', '.');
                final double ret = Double.parseDouble(text);
                if (ret >= 0d && ret < 360d)
                    return ret;
            }
            return null;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public String getHint()
    {
        return dmsMode? Help.DMS_HINT:Help.D_HINT;
    }
    public void addChangeListener(final CoordChangeListener listener)
    {
        getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(DocumentEvent e)
            {
                listener.changed();
            }
            public void removeUpdate(DocumentEvent e)
            {
                throw new UnsupportedOperationException();
            }
            public void changedUpdate(DocumentEvent e)
            {
                throw new UnsupportedOperationException();
            }
        });
    }
}
