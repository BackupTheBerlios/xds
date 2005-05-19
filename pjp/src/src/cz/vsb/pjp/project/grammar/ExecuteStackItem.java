package cz.vsb.pjp.project.grammar;

public class ExecuteStackItem {
    public String data;
    public Object value;
    public int type;

    public ExecuteStackItem(int type, String data, Object value) {
        this.type = type;
        this.data = data;
        this.value = value;
    }

    public String toString() {
        return data + " " + value;
    }
}
