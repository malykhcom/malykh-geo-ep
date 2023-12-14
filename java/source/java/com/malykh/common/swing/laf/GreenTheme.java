package com.malykh.common.swing.laf;

import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.ColorUIResource;

/**
 * @author Anton Malykh
 */
public class GreenTheme extends DefaultMetalTheme
{
    public String getName()
    {
        return "GreenTheme";
    }

    protected final ColorUIResource primary1 = new ColorUIResource(51, 102, 51);
    protected final ColorUIResource primary2 = new ColorUIResource(102, 153, 102);
    protected final ColorUIResource primary3 = new ColorUIResource(153, 204, 153);

    protected ColorUIResource getPrimary1()
    {
        return primary1;
    }

    protected ColorUIResource getPrimary2()
    {
        return primary2;
    }

    protected ColorUIResource getPrimary3()
    {
        return primary3;
    }
}
