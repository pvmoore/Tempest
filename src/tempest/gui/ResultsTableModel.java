package tempest.gui;

import tempest.data.DataFactory;
import tempest.data._ROW;
import tempest.types.Column;
import tempest.types.MemoryTable;
import tempest.types.Table;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ResultsTableModel implements TableModel {
    private int numCols = 0;
    private int numRows = 0;
    private MemoryTable t = null;

    public ResultsTableModel() {
    }

    public ResultsTableModel(Table table) {
        t = (MemoryTable)table;
    }

    public void addTableModelListener(TableModelListener l) {}

    public void removeTableModelListener(TableModelListener l) {}

    public Class<String> getColumnClass(int i) { return java.lang.String.class; }

    public int getColumnCount() {
        if(t != null) return t.getDegree();
        return 0;
    }

    public int getRowCount() {
        if(t != null) return t.getCardinality();
        return 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            _ROW r = t.getRow(rowIndex);
            Object o = r.get(columnIndex);
            if(o == null) return "<null>";
            return o;
        } catch(Exception e) {

        }
        return null;
    }

    public boolean isCellEditable(int row, int col) { return false; }

    public String getColumnName(int col) {
        try {
            Column c = t.getColumn(col);
            return c.getName() + " - " + DataFactory.prettyTypeName(c.getType()).toLowerCase();
        } catch(Exception e) {
            System.out.println("getColumnName: " + e);
        }
        return "UNKNOWN";
    }

    public void setValueAt(Object value, int row, int col) {

    }
    //public void addColumn(String name) {
    //  numCols++;
    //}
    //public void addRow(Object[] values) {
    //  numRows++;
    //}
}