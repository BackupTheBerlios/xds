package cz.vsb.pjp.project.grammar.client;

public class Variable implements Comparable {
    protected String name;
    protected Object value;
    protected int type;

    protected final static int TYPE_STRING = 0;
    protected final static int TYPE_INTEGER = 1;
    protected final static int TYPE_REAL = 2;
    protected final static int TYPE_BOOLEAN = 3;

    private final static String[] types = { "string", "integer", "real", "boolean" };

    public Variable(String type, String name, Object value) {
        for (int i=0; i<types.length; i++)
            if (types[i].equalsIgnoreCase(type)) {
                this.type = i;
                break;
            }
        this.name = name;
        this.value = value;

        if (value == null) {
            switch (this.type) {
                case TYPE_STRING: this.value = new String(""); break;
                case TYPE_INTEGER: this.value = new Integer(0); break;
                case TYPE_REAL: this.value = new Double(0.0); break;
                case TYPE_BOOLEAN: this.value = new Boolean(false); break;
            }
        }
    }

    public int hashCode() {
        return name.hashCode();
    }

    protected boolean sameTypeAs(Variable v) {
        return type == v.type;
    }

    public int compareTo(Object o) {
        return ((Variable)o).name.compareToIgnoreCase(name);
    }

    public int toConstType() {
        switch (type) {
            case TYPE_STRING: return StackCodeProcessor.CONST_STRING;
            case TYPE_BOOLEAN: return StackCodeProcessor.CONST_BOOL;
            case TYPE_INTEGER: return StackCodeProcessor.CONST_INTEGER;
            case TYPE_REAL: return StackCodeProcessor.CONST_DOUBLE;
        }

        return -1;
    }
}
