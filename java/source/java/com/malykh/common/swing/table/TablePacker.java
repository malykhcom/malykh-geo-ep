package com.malykh.common.swing.table;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * @author Anton Malykh
 */
public class TablePacker
{
    private TablePackerMode rowsIncluded = TablePackerMode.VISIBLE_ROWS;
    private boolean distributeExtraArea = true;


    public TablePacker(TablePackerMode rowsIncluded, boolean distributeExtraArea)
    {
        this.rowsIncluded = rowsIncluded;
        this.distributeExtraArea = distributeExtraArea;
    }

    private int preferredWidth(JTable table, int col)
    {
        TableColumn tableColumn = table.getColumnModel().getColumn(col);
        int width = (int)table.getTableHeader().getDefaultRenderer()
                .getTableCellRendererComponent(table, tableColumn.getIdentifier()
                        , false, false, -1, col).getPreferredSize().getWidth();

        if(table.getRowCount()!=0)
        {
            int from = 0, to = 0;
            if (rowsIncluded == TablePackerMode.VISIBLE_ROWS)
            {
                Rectangle rect = table.getVisibleRect();
                from = table.rowAtPoint(rect.getLocation());
                to = table.rowAtPoint(new Point((int)rect.getMaxX(), (int)rect.getMaxY()))+1;
            }
            else if(rowsIncluded == TablePackerMode.ALL_ROWS)
            {
                from = 0;
                to = table.getRowCount();
            }
            for(int row = from; row<to; row++)
            {
                int preferedWidth = (int)table.getCellRenderer(row, col).getTableCellRendererComponent(table,
                                                                                                       table.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
        }
        return width + table.getIntercellSpacing().width+4;
    }

    public void pack(JTable table){
        if(!table.isShowing())
            throw new IllegalStateException("table must be showing to pack");

        if(table.getColumnCount()==0)
            return;

        int[] width = new int[table.getColumnCount()];
        int total = 0;
        for(int col = 0; col<width.length; col++){
            width[col] = preferredWidth(table, col);
            total += width[col];
        }

        int extra = table.getVisibleRect().width - total;
        if(extra>0){
            if(distributeExtraArea){
                int bonus = extra/table.getColumnCount();
                for(int i = 0; i<width.length; i++)
                    width[i] += bonus;
                extra -= bonus*table.getColumnCount();
            }
            width[width.length-1] += extra;
        }

        TableColumnModel columnModel = table.getColumnModel();
        for(int col = 0; col<width.length; col++){
            TableColumn tableColumn = columnModel.getColumn(col);
            table.getTableHeader().setResizingColumn(tableColumn);
            tableColumn.setWidth(width[col]);
        }
    }

    public static void pack(final JTable table, TablePackerMode rowsIncluded, boolean distributeExtraArea)
    {
        final TablePacker packer = new TablePacker(rowsIncluded, distributeExtraArea);
        if (table.isShowing())
        {
            packer.pack(table);
        }
        else
        {
            table.addAncestorListener(new AncestorListener()
            {
                private void tryToPack()
                {
                    if (table.isShowing())
                    {
                        packer.pack(table);
                        table.removeAncestorListener(this);
                    }
                }
                public void ancestorAdded(AncestorEvent event)
                {
                    tryToPack();
                }
                public void ancestorRemoved(AncestorEvent event)
                {
                }
                public void ancestorMoved(AncestorEvent event)
                {
                }
            });
        }
    }
}
