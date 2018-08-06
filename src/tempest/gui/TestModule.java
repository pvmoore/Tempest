package tempest.gui;

import tempest.Engine;
import tempest.Session;
import tempest.sql.SQL;
import tempest.types.Catalog;
import tempest.types.Database;
import tempest.types.Schema;
import tempest.types.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestModule extends JFrame implements ActionListener {
    private static final long serialVersionUID = 131;
    private GUI gui = null;
    private JList list = null;
    private JButton button = null;
    private String[] tests = null;
    //private Session session = null;

    public TestModule(GUI gui) throws HeadlessException {
        this.gui = gui;
        Container c = this.getContentPane();
        this.setTitle("Test Module");
        loadTests();
        list = new JList(tests);
        list.setSelectedIndex(0);
        button = new JButton("Execute");
        button.addActionListener(this);
        JScrollPane sp = new JScrollPane(list);
        c.add(sp);
        c.add(BorderLayout.SOUTH, button);
        this.setBounds((int)(gui.getLocation().getX() + gui.getSize().getWidth()),
                       (int)gui.getLocation().getY(),
                       400,
                       (int)gui.getSize().getHeight());
        this.setVisible(true);
        //
        try {
            tempest.Engine.start("data");
            gui.session = new Session();
            Database database = Engine.createDatabase("test", "", Database.MEMORY);
            if(!database.open()) throw new SQLException("Couldn't open database");
            //
            Catalog catalog = database.getCatalog(null);
            Schema schema = catalog.getSchema(null);
            gui.session.setDatabase(database);
            gui.session.setCatalog(catalog);
            gui.session.setSchema(schema);
        } catch(Exception e) {
            System.out.println("TestModule() : " + e);
        }
    }

    private void loadTests() {
        ArrayList<String> al = new ArrayList<String>();
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("testqueries.sql");
            br = new BufferedReader(fr);
            String line = null;
            while((line = br.readLine()) != null) {
                al.add(line);
            }
            tests = al.toArray(new String[0]);
        } catch(Exception e) {

        } finally {
            try { if(br != null) br.close(); } catch(Exception ee) {}
            try { if(fr != null) fr.close(); } catch(Exception ee) {}
        }
    }

    public void actionPerformed(ActionEvent e) {
        int index = list.getSelectedIndex();
        String query = (String)list.getSelectedValue();
        //
        try {
            SQL sql = SQL.getSQL(gui.session, query);
            Table t = sql.execute();
            ResultsTableModel rtm = new ResultsTableModel(t);
            gui.refreshResults(rtm);
            gui.result.setText("" + t.getCardinality() + " row" + (t.getCardinality() == 1 ? "" : "s") + " returned");
            gui.lastQuery.setText(query);
            list.setSelectedIndex(index + 1);
        } catch(Exception ee) {
            System.out.println("actionPerformed: " + ee);
            ee.printStackTrace(System.out);
            gui.result.setText("" + ee);
        }
    }
}