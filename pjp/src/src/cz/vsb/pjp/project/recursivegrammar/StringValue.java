package cz.vsb.pjp.project.recursivegrammar;

/**
 * @author Vladimir Schafer - 15.5.2005 - 14:10:04
 */
public class StringValue extends Value {
    protected static final String type = "string";
    protected String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getRealValue() {
        return value;
    }

    public Value performUnaryOperation(String operator) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Unary operator: " + operator + " isn't supported");
    }

    public void setValue(Value v) throws UnsupportedOperationException {
        if (v instanceof StringValue) {
            this.value = ((StringValue) v).value;
        } else
            throw new UnsupportedOperationException("Can't assign " + v.getType() + " to " + type);
    }

    public Value performOperation(String operator, Value v) throws UnsupportedOperationException {
        if (v instanceof RealValue) {
            return v.performOperation(operator, this);
        }

        if (v instanceof StringValue) {
            StringValue val = (StringValue) v;
            if (operator.equals(".")) {
                return new StringValue(value + val.getRealValue());
            } else if (operator.equals("<")) {
                return new BooleanValue((value.compareTo(val.getRealValue()) < 0) ? true : false);
            } else if (operator.equals(">")) {
                return new BooleanValue((value.compareTo(val.getRealValue()) > 0) ? true : false);
            } else if (operator.equals("<=")) {
                return new BooleanValue((value.compareTo(val.getRealValue()) <= 0) ? true : false);
            } else if (operator.equals(">=")) {
                return new BooleanValue((value.compareTo(val.getRealValue()) >= 0) ? true : false);
            } else if (operator.equals("<>")) {
                return new BooleanValue((value.compareTo(val.getRealValue()) != 0) ? true : false);
            } else if (operator.equals("=")) {
                return new BooleanValue((value.compareTo(val.getRealValue()) == 0) ? true : false);
            } else
                throw new UnsupportedOperationException("Operator " + operator + " isn't supported with integer type");
        } else
            throw new UnsupportedOperationException("Can't process String and " + v.getType());
    }
}
