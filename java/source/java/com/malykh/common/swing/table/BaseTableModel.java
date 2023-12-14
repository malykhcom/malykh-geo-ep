package com.malykh.common.swing.table;

import javax.swing.table.DefaultTableModel;
import java.util.*;

/**
 * Базовая модель таблицы для хранения данных.
 * @author Anton Malykh
 */
public abstract class BaseTableModel<D> extends DefaultTableModel
{
    protected final List<D> data = new ArrayList<D>();
    protected BaseTableModel(String firstColumn, String... columns)
    {
        addColumn(firstColumn);
        for (String column : columns)
        {
            addColumn(column);
        }
    }
    public synchronized boolean isCellEditable(int row, int column)
    {
        return false;
    }
    /**
     * Добавить данные
     * @param d данные
     */
    public synchronized void addData(D d)
    {
        int index = data.size();
        data.add(d);
        addRow(new Vector<Object>(convertToRow(index, d)));
    }
    public synchronized void addDataSorted(final D d, final Comparator<D> comparator)
    {
        final int len = data.size();
        for (int index = 0; index < len; index++)
        {
            final D rowD = data.get(index);
            if (comparator.compare(rowD, d) > 0)
            {
                data.add(index, d);
                insertRow(index, new Vector<Object>(convertToRow(index, d)));
                return;
            }
        }
        addData(d);
    }

    /**
     * Заменить данные в указанной строке
     * @param index номер строки (от 0 включительно)
     * @param d новые данные для указанной строки
     */
    public synchronized void setData(int index, D d)
    {
        data.set(index, d);
        List<?> els = convertToRow(index, d);
        for (int t = 0; t < els.size(); t++)
        {
            Object v = getValueAt(index, t);
            Object newV = els.get(t);
            if (!v.equals(newV))
                setValueAt(newV, index, t);
        }
    }

    /**
     * Получить данные в указанной строке
     * @param index номер строки (от 0 включительно)
     * @return данные из указанной строки
     */
    public synchronized D getData(int index)
    {
        return data.get(index);
    }

    /**
     * Получить все данные
     * @return все данные (копия)
     */
    public synchronized List<D> getAllData()
    {
        return new ArrayList<D>(data);
    }

    /**
     * Удалить все данные
     */
    public synchronized void removeAllData()
    {
        int size = data.size();
        for (int t = 0; t < size; t++)
            removeRow(0);
    }

    /**
     * Удалить данные в указанной строке
     * @param index номер строки (от 0 включительно)
     */
    public synchronized void removeRow(int index)
    {
        super.removeRow(index);
        data.remove(index);
    }

    /**
     * Просто поиск данных. Поиск только по прямому равенству (==)
     * @param d данные для поиска
     * @return -1, если не найдено. номер строки иначе (от 0 включительно)
     */
    public synchronized int simpleFindData(D d)
    {
        int t = 0;
        for (D d2 : data)
        {
            if (d2 == d)
                return t;
            t++;
        }
        return -1;
    }

    /**
     * Метод для преобразования данных в данные колонок таблицы
     * @param index номер строки (от 0 включительно)
     * @param d данные для преобразования
     * @return значения ячеек для ряда, который будет представлять данные (количество значений должно быть равно количеству колонок
     */
    protected abstract List<?> convertToRow(int index, D d);
}
