package org.example.Utils;
import org.bson.Document;

public class Entry implements Comparable<Entry> {
    private Double key;
    private Document value;
    
    public Entry(Double key, Document value) {
        this.key = key;
        this.value = value;
    }

    public Double getKey() {
        return key;
    }

    public Document getValue() {
        return value;
    }

    @Override
    public int compareTo(Entry o) {
        return this.getKey().compareTo(o.getKey());
    }

    public String toString() {
        return "[" + key + ", " + value + "]";
    }
}
