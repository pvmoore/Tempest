package tempest.collections;

import tempest.data._ROW;

public class RowList {
    private int size = 0;
    private int expand = 8;
    private _ROW[] rows = new _ROW[expand];

    ///////////////////////////////////////////////////////////////// constructors
    public RowList() {}

    public RowList(_ROW[] r) {
        rows = r;
        size = r.length;
    }

    /////////////////////////////////////////////////////////////// public methods
    public String toString() { return ""; }

    public int size() { return size; }

    public void add(_ROW r) {
        ensureCapacity(1);
        rows[size++] = r;
    }

    public void add(_ROW[] r) {
        if(r == null) return;
        ensureCapacity(r.length);
        System.arraycopy(r, 0, rows, size, r.length);
        size += r.length;
    }

    public void add(RowList r) {
        if(r == null) return;
        add(r.rows);
    }

    public _ROW get(int i) {
        return rows[i];
    }

    ////////////////////////////////////////////////////////////// private methods
    private void ensureCapacity(int required) {
        if(rows.length - size < required) {
            _ROW[] r = new _ROW[size + required + expand];
            System.arraycopy(rows, 0, r, 0, size);
            rows = r;
        }
    }
}