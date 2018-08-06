package tempest.gui;

import tempest.Engine;
import tempest.Session;
import tempest.sql.SQL;
import tempest.types.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame implements ActionListener, ChangeListener {
    static final long serialVersionUID = 131;
    private JTabbedPane tabbedPane;
    private JTable resultsTable;
    private ResultsTableModel rtm;
    private JTable statsTable;
    private JTable metaTable;
    private JTree tablesTree;
    private JScrollPane sp1;
    private JScrollPane sp2;
    private JScrollPane sp3;
    private JScrollPane sp4;
    private JTextArea queryArea;
    public JLabel result;
    public JTextArea lastQuery;
    private JButton executeButton;
    private TestModule testModule;

    Session session;

    public static void main(String[] args) { new GUI(); }

    public GUI() throws HeadlessException {
        Container c = this.getContentPane();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                destroy();
            }
        });
        try {
            UIManager.setLookAndFeel(
                //UIManager.getSystemLookAndFeelClassName());
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch(Exception e) {}

        tabbedPane = new JTabbedPane();
        c.add(tabbedPane);
        c.add(BorderLayout.SOUTH, createSouthPanel());
        //
        rtm = new ResultsTableModel();
        resultsTable = new JTable(rtm);
        statsTable = new JTable(1, 1);
        metaTable = new JTable(1, 1);
        tablesTree = new JTree();
        sp1 = new JScrollPane(resultsTable);
        sp2 = new JScrollPane(tablesTree);
        sp3 = new JScrollPane(statsTable);
        sp4 = new JScrollPane(metaTable);
        tabbedPane.add("Results", sp1);
        tabbedPane.add("Tables", sp2);
        tabbedPane.add("Statistics", sp3);
        tabbedPane.add("Meta Data", sp4);
        tabbedPane.addChangeListener(this);
        this.setTitle("Tempest SQL:1999 Database Engine v" + Engine.VERSION + " GUI");
        this.setSize(600, 500);
        this.setVisible(true);
        testModule = new TestModule(this);
        //assessMem();
        test();
    }

    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout());
        result = new JLabel("", SwingConstants.LEFT);
        result.setOpaque(true);
        result.setBackground(Color.BLUE);
        result.setForeground(Color.YELLOW);
        result.setFont(new Font("Arial", Font.BOLD, 11));
        result.setBorder(new BevelBorder(BevelBorder.RAISED));
        lastQuery = new JTextArea("");
        lastQuery.setEditable(false);
        //lastQuery.setFocusable(false);
        lastQuery.setLineWrap(true);
        lastQuery.setOpaque(true);
        lastQuery.setBackground(Color.BLUE);
        lastQuery.setForeground(Color.WHITE);
        lastQuery.setFont(new Font("Arial", Font.BOLD, 11));
        lastQuery.setBorder(new BevelBorder(BevelBorder.RAISED));
        queryArea = new JTextArea();
        queryArea.setOpaque(true);
        queryArea.setBackground(Color.BLUE);
        queryArea.setForeground(Color.WHITE);
        queryArea.setCaretColor(Color.YELLOW);
        queryArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        panel.add(BorderLayout.NORTH, lastQuery);
        panel.add(BorderLayout.SOUTH, result);
        panel.add(queryArea);
        //
        southPanel.add(panel);
        executeButton = new JButton("Go");
        executeButton.addActionListener(this);
        southPanel.add(BorderLayout.EAST, executeButton);
        return southPanel;
    }

    void destroy() {
        tempest.Engine.destroy();
        System.exit(0);
    }

    public void refreshResults(ResultsTableModel rtml) {
        this.rtm = rtml;
        resultsTable.setModel(rtm);
        tabbedPane.setSelectedIndex(0);
    }

    private void refreshTablesTab() {
        // maybe get information from 'INFORMATION_SCHEMA'?
        DefaultMutableTreeNode root = null;
        System.out.println("session=" + session);
        if(session != null) {
            root = new DefaultMutableTreeNode(session.getDatabase().getName() + "." + session
                .getCatalog() + "." + session.getSchema());
            Table[] tables = session.getSchema().getTables();
            for(int i = 0; tables != null && i < tables.length; i++) {
                DefaultMutableTreeNode tnode = new DefaultMutableTreeNode(tables[i].getName());
                root.add(tnode);
                Column[] cols = tables[i].getColumns();
                for(int c = 0; c < cols.length; c++) {
                    tnode.add(new DefaultMutableTreeNode(cols[c].getName() + " - " + cols[c].getType()));
                }
            }
        }
        tablesTree.setModel(new DefaultTreeModel(root));
    }

    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton)e.getSource();
        String text = b.getText();
        if("go".equalsIgnoreCase(text)) {
            String query = queryArea.getText();
            queryArea.setText("");
            if(session == null) {
                session = new Session();
                try {
                    Database db = Engine.getDatabase("test");
                    Catalog catalog = db.getCatalog(null);
                    Schema schema = catalog.getSchema(null);
                    session.setDatabase(db);
                    session.setCatalog(catalog);
                    session.setSchema(schema);
                } catch(Exception ex) {
                    result.setText("Exception: " + ex);
                }
            }
            //
            try {
                lastQuery.setText(query);
                SQL sql = SQL.getSQL(session, query);
                Table t = sql.execute();
                ResultsTableModel rtm2 = new ResultsTableModel(t);
                refreshResults(rtm2);
                result.setText("" + t.getCardinality() + " row" + (t.getCardinality() == 1 ? "" : "s") + " returned");
            } catch(Exception ex) {
                ex.printStackTrace(System.out);
                result.setText("" + ex);
            }
        }
    }

    public void stateChanged(ChangeEvent e) {
        JTabbedPane pane = (JTabbedPane)e.getSource();
        int index = pane.getSelectedIndex();
        if(index == 1) {
            //tables tab
            refreshTablesTab();
        } else if(index == 2) {

        }
    }

    //////////////////////////////////////////////////////////////////////////////
  /*private void assessMem() {
    System.gc();
    Runtime r  = Runtime.getRuntime();
    Object[] o = new Object[100000];
    long memused1 = r.totalMemory() - r.freeMemory();
    for(int i=0;i<o.length;i++) {
      o[i]=new tempest.data._FLOAT();
    }
    long memused2=r.totalMemory()-r.freeMemory();
    this.setTitle("memused="+((memused2-memused1)/o.length));
  }*/
    private void test() {

    }
}