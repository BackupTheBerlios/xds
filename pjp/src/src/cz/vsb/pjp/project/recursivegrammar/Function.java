package cz.vsb.pjp.project.recursivegrammar;

import java.util.Vector;
import java.util.AbstractList;

/**
 * Trida reprezentuje funkci v tabulce symbolu. Vola se metodou executefunction, ktere se predaji parametry
 *
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

    public Value performOperation(String operator, Value v) throws OperatorNotSupportedException {
        throw new OperatorNotSupportedException("Functions can't be used with operators");
    }

    public Value performUnaryOperation(String operator) throws OperatorNotSupportedException {
        throw new OperatorNotSupportedException("Function can't be used with operators");
    }

    public void setValue(Value v) throws OperatorNotSupportedException {
        throw new OperatorNotSupportedException("Can't set value to function");
    }
}
