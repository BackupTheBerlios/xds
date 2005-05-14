package cz.vsb.pjp.project.grammar;

/**

 */
public class ExecuteStackItem {
    protected Object data, value;
    protected int type;

    public ExecuteStackItem(Object data, Object value) {
        this.data = data;
        this.value = value;
    }

    public String toString() {
        return "[" + data + " " + value + "]";
    }
}
