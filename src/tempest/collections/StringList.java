package tempest.collections;

public class StringList {
    private int size = 0;
    private int expand = 8;
    private String[] rows = new String[expand];

    ///////////////////////////////////////////////////////////////// constructors
    public StringList() {}

    public StringList(StringList sl) {
        add(sl);
    }

    public StringList(StringList sl, int start, int end) {
        add(sl, start, end);
    }

    public StringList(String[] r) {
        add(r);
    }

    public StringList(String[] r, int start, int end) {
        add(r);
    }

    /////////////////////////////////////////////////////////////// public methods
    public String toString() { return ""; }

    public int size() { return size; }

    public void add(String r) {
        ensureCapacity(1);
        rows[size++] = r;
    }

    public void add(String[] r) {
        if(r == null || r.length == 0) return;
        add(r, 0, r.length);
    }

    public void add(String[] r, int start, int end) {
        if(r == null) return;
        int len = end - start;
        ensureCapacity(len);
        //System.out.println("start="+start+" end="+end+" rows.length="+rows.length+" size="+size+" len="+len);
        System.arraycopy(r, start, rows, size, len);
        size += len;
    }

    public void add(StringList r) {
        if(r == null || r.size() == 0) return;
        add(r.rows, 0, r.size());
    }

    public void add(StringList r, int start, int end) {
        if(r == null) return;
        add(r.rows, start, end);
    }

    public String get(int i) {
        return rows[i];
    }

    public void set(int index, String value) {
        rows[index] = value;
    }

    public boolean contains(String s) {
        if(s == null) return false;
        for(int i = 0; i < rows.length; i++) {
            if(s.equals(rows[i])) return true;
        }
        return false;
    }

    ////////////////////////////////////////////////////////////// private methods
    private void ensureCapacity(int required) {
        if(rows.length - size < required) {
            String[] r = new String[size + required + expand];
            System.arraycopy(rows, 0, r, 0, size);
            rows = r;
        }
    }
}
