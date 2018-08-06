package tempest;

public class IntArray {
    public static final int NULL = Integer.MIN_VALUE;

    private int length = 0;
    private int[] array = null;
    private int growth = 8;
    private int def = NULL;

    public IntArray() {
        array = new int[growth];
    }

    public IntArray(int initialCapacity, int growth) {
        this.growth = growth;
        array = new int[initialCapacity];
    }

    public synchronized void remove(int index) {
        if(index >= length || index < 0) throw new ArrayIndexOutOfBoundsException("" + index + " / " + length);
        //int value=array[index];
        for(int i = index; i < length - 1; i++) {
            array[i] = array[i + 1];
        }
        length--;
    }

    public int get(int index) {
        if(index >= length || index < 0) throw new ArrayIndexOutOfBoundsException("" + index + " / " + length);
        return array[index];
    }

    public Object getObject(int index) {
        if(index >= length || index < 0) throw new ArrayIndexOutOfBoundsException("" + index + " / " + length);
        return new Integer(array[index]);
    }

    public void add(Object o) {
        add(((Integer)o).intValue());
    }

    public synchronized void add(int value) {
        ensureCapacity(length);
        array[length++] = value;
    }

    public synchronized void insert(int index, Object value) {
        insert(index, ((Integer)value).intValue());
    }

    public synchronized void insert(int index, int value) {
        if(index < 0) throw new ArrayIndexOutOfBoundsException("" + index + " / " + length);
        ensureCapacity(index);
        for(int i = index; i < length - 1; i++) {
            array[i + 1] = array[i];
        }
        array[index] = value;
    }

    public synchronized void set(int index, int value) {
        if(index < 0) throw new ArrayIndexOutOfBoundsException("" + index + " / " + length);
        ensureCapacity(index);
        array[index] = value;
    }

    public synchronized void setLength(int len) {
        if(len < 0) throw new IllegalArgumentException("Cannot set a length of less than zero");
        if(len == length) return;
        ensureCapacity(len - 1);
        length = len;
    }

    public void setDefault(int value) { def = value; }

    public boolean contains(int value) {
        for(int i = 0; i < length; i++) {
            if(array[i] == value) return true;
        }
        return false;
    }

    public synchronized void decrementLength() {
        if(length > 0) {
            length--;
        }
    }

    public int length() { return length; }

    public int[] toArray() {
        int[] temp = new int[length];
        System.arraycopy(array, 0, temp, 0, length);
        return temp;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("INTARRAY " + length + "[ ");
        for(int i = 0; i < length; i++) {
            if(i > 0) buf.append(",");
            buf.append(String.valueOf(array[i]));
        }
        buf.append(" ]");
        return buf.toString();
    }

    public boolean equals(IntArray o) {
        if(o.length() != length) return false;
        for(int i = 0; i < length; i++) {
            if(o.get(i) != array[i]) return false;
        }
        return true;
    }

    public boolean equals(Object o) {
        if(o instanceof IntArray) return equals((IntArray)o);
        return false;
    }

    ////////////////////////////////////////////////////////////////////// private
    private void ensureCapacity(int index) {
        if(array.length > index) return;
        int[] temp = null;
        if(array.length + growth > index) {
            temp = new int[array.length + growth];
        } else {
            temp = new int[index + growth];
        }
        System.arraycopy(array, 0, temp, 0, length);
        array = temp;
    }
}