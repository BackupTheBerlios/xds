package cz.vsb.pjp.project.recursivegrammar;

import java.util.Vector;
import java.util.AbstractList;

/**
 * @author Vladimir Schafer - 18.5.2005 - 13:54:56
 */
public abstract class Function extends Value {
    public abstract Object ExecuteFunction(AbstractList<Value> values);

    public String getType() {
        return "function";
    }

    public void setValue(String value) {
        return;
    }

    public String getValue() {
        return null;
    }

    public Value performOperation(String operator, Value v) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Functions can't be used with operators");
    }

    public Value performUnaryOperation(String operator) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Function can't be used with operators");
    }

    public void setValue(Value v) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Can't set value to function");
    }
}
