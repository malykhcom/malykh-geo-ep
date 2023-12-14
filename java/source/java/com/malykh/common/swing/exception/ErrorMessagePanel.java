package com.malykh.common.swing.exception;

import com.malykh.common.swing.CommonRB;
import com.malykh.common.swing.component.ButtonPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author Anton Malykh
 */
public class ErrorMessagePanel extends JTabbedPane implements ActionListener
{
    protected final Throwable tr;

    public ErrorMessagePanel(Throwable tr)
    {
        super();
        this.tr = tr;
        JButton copy = new JButton(CommonRB.get("exception.copy.to.clipboard"));
        copy.addActionListener(this);
        JTextArea simpleArea = createArea(tr);

        JTextArea moreArea = createArea(getStackTrace(tr));
        addTab(CommonRB.get("exception.error.tab"),
               new JScrollPane(simpleArea));
        JPanel morePanel = new JPanel(new BorderLayout());
        morePanel.add(new JScrollPane(moreArea), BorderLayout.CENTER);
        morePanel.add(new ButtonPanel(copy), BorderLayout.SOUTH);
        addTab(CommonRB.get("exception.more.tab"),
               morePanel);
    }

    protected String getStackTrace(Throwable tr)
    {
        Writer writer = new StringWriter();
        tr.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    protected JTextArea createArea(String text)
    {
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setCaretPosition(0);
        return area;
    }

    protected JTextArea createArea(Throwable tr)
    {
        if (tr.getMessage() != null)
            return createArea(tr.getMessage());
        else
            return createArea(tr.toString());
    }

    public void actionPerformed(ActionEvent e)
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        StringSelection sc = new StringSelection(getStackTrace(tr));
        Clipboard clip = tk.getSystemClipboard();
        clip.setContents(sc, null);
    }
}
