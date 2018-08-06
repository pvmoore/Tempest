package tempest.collections;

public class IntList {
    private int size = 0;
    private int expand = 8;
    private int[] rows = new int[expand];

    ///////////////////////////////////////////////////////////////// constructors
    public IntList() {}

    public IntList(IntList il) {
        add(il);
    }

    public IntList(IntList il, int start, int end) {
        add(il, start, end);
    }

    public IntList(int[] r) {
        add(r);
    }

    public IntList(int[] r, int start, int end) {
        add(r, start, end);
    }

    /////////////////////////////////////////////////////////////// public methods
    public String toString() { return ""; }

    public int size() { return size; }

    public void add(int r) {
        ensureCapacity(1);
        rows[size++] = r;
    }

    public void add(int[] r) {
        if(r == null || r.length == 0) return;
        add(r, 0, r.length);
    }

    public void add(int[] r, int start, int end) {
        if(r == null) return;
        int len = end - start;
        ensureCapacity(len);
        System.arraycopy(r, start, rows, size, len);
        size += len;
    }

    public void add(IntList r, int start, int end) {
        if(r == null) return;
        add(r.rows, start, end);
    }

    public void add(IntList r) {
        if(r == null) return;
        add(r.rows, 0, r.size());
    }

    public int get(int i) {
        return rows[i];
    }

    public void set(int index, int value) {
        rows[index] = value;
    }

    public boolean contains(int s) {
        for(int i = 0; i < rows.length; i++) {
            if(s == rows[i]) return true;
        }
        return false;
    }

    ////////////////////////////////////////////////////////////// private methods
    private void ensureCapacity(int required) {
        if(rows.length - size < required) {
            int[] r = new int[size + required + expand];
            System.arraycopy(rows, 0, r, 0, size);
            rows = r;
        }
    }
}
