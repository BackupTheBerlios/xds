package cz.vsb.pjp.project.recursivegrammar;

/**
 * Retezcova hodnota
 *
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

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getRealValue() {
        return value;
    }

    public Value performUnaryOperation(String operator) throws OperatorNotSupportedException {
        throw new OperatorNotSupportedException("Unary operator: " + operator + " isn't supported");
    }

    public void setValue(Value v) throws OperatorNotSupportedException {
        if (v instanceof StringValue) {
            this.value = ((StringValue) v).value;
        } else if (v instanceof FakeValue) {
            return;
        } else
            throw new OperatorNotSupportedException("Can't assign " + v.getType() + " to " + type);
    }

    /**
     * Podporovane operace: <, >, <=, >=, <>, =. Vola na retezce compareTo.
     *
     * @param operator
     * @param v        jen Integer
     * @return
     * @throws UnsupportedOperationException
     */
    public Value performOperation(String operator, Value v) throws OperatorNotSupportedException {

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
            } else if (operator.equals("fake")) {
                return this;
            } else
                throw new OperatorNotSupportedException("Operator " + operator + " isn't supported with string type");
        } else if (v instanceof FakeValue)
            return this;
        else
            throw new OperatorNotSupportedException("Can't process string and " + v.getType());
    }
}
