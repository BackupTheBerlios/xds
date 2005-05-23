package cz.vsb.pjp.project.grammar;

import cz.vsb.pjp.project.grammar.client.StackCodeProcessor;

/**
 * One item popped from execute stack
 * 
 * @author luk117
 */
public class ExecuteStackItem {
    public String data;
    public Comparable value;
    public int type;

    public ExecuteStackItem(int type, String data, Comparable value) {
        this.type = type;
        this.data = data;
        this.value = value;
    }

    public String toString() {
        return data + " " + value;
    }

    public int compareTo(ExecuteStackItem i) {
        if( type == i.type)
            return value.compareTo(i.value);

        return StackCodeProcessor.compareTwoStackItems(this, i);
    }

}
