package com.malykh.common.swing.table;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * Таблица только для чтения, с выделением по одной строке, с поддержкой tool tip для ячеек (значение должно быть
 * класса StringWithToolTip). 
 * @author Anton Malykh
 */
public class ROTable extends JTable
{
    public ROTable(TableModel dm)
    {
        super(dm);
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setColumnSelectionAllowed(false);
    }

    public boolean isCellEditable(int row, int column)
    {
        return false;
    }
    
    public Component prepareRenderer(TableCellRenderer renderer,
                                     int rowIndex, int vColIndex)
    {
        Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
        if (c instanceof JComponent)
        {
            JComponent jc = (JComponent)c;
            Object value = getValueAt(rowIndex, vColIndex);
            if (value instanceof StringWithToolTip)
            {
                StringWithToolTip s = (StringWithToolTip) value;
                jc.setToolTipText(s.getToolTip());
            }
            else
                jc.setToolTipText(String.valueOf(value));
        }
        return c;
    }
    //
    public static void main(String[] args)
    {
        BaseTableModel<String> model = new BaseTableModel<String>("строка1", "строка2")
        {
            protected List<?> convertToRow(int index, String s)
            {
                return Arrays.asList(s, s);
            }
        };
        model.addData("1");
        model.addData("2");
        model.addData("3");
        final ROTable table = new ROTable(model);
        TablePacker.pack(table, TablePackerMode.ALL_ROWS, false);
        JButton button = new JButton("pack");
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                TablePacker.pack(table, TablePackerMode.ALL_ROWS, false);
            }
        });
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        frame.getContentPane().add(button, BorderLayout.PAGE_START);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
