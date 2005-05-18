package cz.vsb.pjp.project.recursivegrammar;

import cz.vsb.pjp.project.recursivegrammar.StringValue;
import cz.vsb.pjp.project.recursivegrammar.RealValue;
import cz.vsb.pjp.project.recursivegrammar.IntegerValue;
import cz.vsb.pjp.project.recursivegrammar.BooleanValue;

/**
 * @author Vladimir Schafer - 15.5.2005 - 14:07:10
 */
public abstract class Value {
    public abstract String getType();

    public abstract String getValue();

    public abstract Value performOperation(String operator, Value v) throws UnsupportedOperationException;

    public abstract Value performUnaryOperation(String operator) throws UnsupportedOperationException;

    public abstract void setValue(Value v) throws UnsupportedOperationException;

    public static Value getDefaultValue(String type) throws UnsupportedOperationException {
        if (type.equals("integer"))
            return new IntegerValue(0);
        else if (type.equals("real"))
            return new RealValue(0);
        else if (type.equals("boolean"))
            return new BooleanValue(false);
        else if (type.equals("string"))
            return new StringValue("");
        else
            throw new UnsupportedOperationException("Unsupported data type");
    }

    public String toString() {
        return getValue();
    }
}
