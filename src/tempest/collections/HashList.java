package tempest.collections;

import java.util.ArrayList;
import java.util.Hashtable;

public class HashList {
    private Hashtable hash = new Hashtable();
    private ArrayList list = new ArrayList();

    public HashList() {}

    public Object get(Object o) {
        if(o == null) return null;
        return hash.get(o);
    }

    public Object get(int index) {
        return list.get(index);
    }

    public int size() { return list.size(); }

    public void put(Object name, Object value) {
        if(hash.containsKey(name)) return;
        hash.put(name, value);
        list.add(value);
    }

    public Object remove(Object o) {
        if(!hash.containsKey(o)) return null;
        list.remove(o);
        return hash.remove(o);
    }

    public boolean containsKey(Object o) {
        return hash.containsKey(o);
    }

    public boolean containsValue(Object o) {
        return list.contains(o);
    }

    public ArrayList getArrayList() { return list; }

    public Hashtable getHashtable() { return hash; }

    public String toString() {
        return hash.toString() + "\n" + list.toString();
    }
}