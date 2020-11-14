package Model;

import View.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComboListener implements ActionListener {
    TableInterface editor;
    GUI gui;

    public ComboListener(TableInterface editor, GUI gui){
        this.editor = editor;
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                editor.updateTable(gui.getSelectedValue().toString());
                return null;
            }
        };
        worker.execute();
    }
}
