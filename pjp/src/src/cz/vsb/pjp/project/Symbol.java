package cz.vsb.pjp.project;

/**
 * Class represents lexical analysis symbol
 */
public class Symbol {
    protected String name;
    protected String att;

    public Symbol() {
        name = "default";
    }

    public Symbol(String type) {
        name = type;
    }

    public String getName() {
        return name;
    }

    public String getAtt() {
        return att;
    }

    public void setAtt(String att) {
        this.att = att;
    }

    public String toString() {
        return name + " - " + att;
    }

    protected Symbol clone() throws CloneNotSupportedException {
        Symbol n = new Symbol(name);
        n.setAtt(att);
        return n;
    }
}