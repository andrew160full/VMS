import java.util.*;

public class ArrayMap <K, V> extends AbstractMap <K, V>{
    private ArrayList <ArrayMapEntry <K, V>> list;
    private Set <Entry <K, V>> entries = null;

    public ArrayMap () {
        list = new ArrayList <ArrayMapEntry <K, V>>();
    }
    public Set <Entry <K, V>> entrySet() {
        if (entries != null)
            return entries;
        entries = new AbstractSet <Entry <K, V>> () {
            public int size() {
                return list.size();
            }
            public Iterator iterator() {
                return list.iterator();
            }

            public void clear() {
                list.clear();
            }
        };
        return entries;
    }

    public boolean containsKey (Object key) {
        for (ArrayMapEntry<K, V> kvArrayMapEntry : list) {
            if (kvArrayMapEntry.getKey() == key)
                return true;
        }
        return false;
    }

    public Iterator iterator() {
        return entrySet().iterator();
    }

    public int size() {
        return list.size();
    }

    public V get(Object key) {
        if (!containsKey(key))
            return null;
        for (ArrayMapEntry<K, V> myEntry : list) {
            if (myEntry.getKey() == key)
                return myEntry.getValue();
        }
        return null;
    }

    public V put (K key, V value) {
        if (containsKey(key))
            return get (key);
        ArrayMapEntry<K, V> newEntry = new ArrayMapEntry<>(key, value);
        list.add (newEntry);
        return null;
    }

    static class ArrayMapEntry <K, V> implements Map.Entry <K, V>{
        private K key;
        private V value;

        public ArrayMapEntry (K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
        public V setValue (V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        public String toString() {
            return "Key: " + key + "  " + "Value: " + value;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ArrayMapEntry))
                return false;

            return (((ArrayMapEntry <K, V>) obj).getKey() == this.getKey() &&
                    ((ArrayMapEntry<K, V>) obj).getValue() == this.getValue());
        }
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode())
                    ^ (this.value == null ? 0 : this.value.hashCode());
        }

    }
}
