package com.malykh.common.swing.table;

/**
 * Строка с подсказкой
 * @author Anton Malykh
 */
public class StringWithToolTip
{
    protected final String string;
    protected final String toolTip;

    public StringWithToolTip(String string, String toolTip)
    {
        this.string = string;
        this.toolTip = toolTip;
    }

    public String getString()
    {
        return string;
    }

    public String getToolTip()
    {
        return toolTip;
    }

    public String toString()
    {
        return string;
    }
}
