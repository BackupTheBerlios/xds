package cz.vsb.pjp.project.recursivegrammar;

/**
 * Hodnota prirozeneho cisla
 *
 * @author Vladimir Schafer - 15.5.2005 - 14:10:04
 */
public class IntegerValue extends Value {
    protected static final String type = "integer";
    protected int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    public IntegerValue(String value) {
        this.value = Integer.parseInt(value);
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return Integer.toString(value);
    }

    public void setValue(String value) {
        this.value = Integer.parseInt(value);
    }

    public int getRealValue() {
        return value;
    }

    /**
     * Podporovane operace: -
     *
     * @param operator
     * @return
     * @throws UnsupportedOperationException
     */
    public Value performUnaryOperation(String operator) throws UnsupportedOperationException {
        if (operator.equals("-")) {
            value = -value;
        } else
            throw new UnsupportedOperationException("Unary operator: " + operator + " isn't supported");

        return new IntegerValue(value);
    }

    public void setValue(Value v) throws UnsupportedOperationException {
        if (v instanceof IntegerValue) {
            this.value = ((IntegerValue) v).value;
        } else if (v instanceof FakeValue) {
            return;
        } else
            throw new UnsupportedOperationException("Can't assign " + v.getType() + " to " + type);
    }

    /**
     * Podporovane operace: -, *, /, mod, <, >, <=, >=, <>, =
     *
     * @param operator
     * @param v        jen Integer
     * @return
     * @throws UnsupportedOperationException
     */
    public Value performOperation(String operator, Value v) throws UnsupportedOperationException {
        if (v instanceof RealValue) {
            RealValue r = new RealValue(this.getValue());
            return r.performOperation(operator, v);
        }

        if (v instanceof IntegerValue) {
            IntegerValue val = (IntegerValue) v;
            if (operator.equals("+")) {
                return new IntegerValue(val.getRealValue() + value);
            } else if (operator.equals("-")) {
                return new IntegerValue(value - val.getRealValue());
            } else if (operator.equals("*")) {
                return new IntegerValue(value * val.getRealValue());
            } else if (operator.equals("/")) {
                return new IntegerValue(value / val.getRealValue());
            } else if (operator.equals("mod")) {
                return new IntegerValue(value % val.getRealValue());
            } else if (operator.equals("<")) {
                return new BooleanValue(value < val.getRealValue());
            } else if (operator.equals(">")) {
                return new BooleanValue(value > val.getRealValue());
            } else if (operator.equals("<=")) {
                return new BooleanValue(value <= val.getRealValue());
            } else if (operator.equals(">=")) {
                return new BooleanValue(value >= val.getRealValue());
            } else if (operator.equals("<>")) {
                return new BooleanValue(value != val.getRealValue());
            } else if (operator.equals("=")) {
                return new BooleanValue(value == val.getRealValue());
            } else
                throw new UnsupportedOperationException("Operator " + operator + " isn't supported with integer type");
        } else if (v instanceof FakeValue)
            return this;
        else
            throw new UnsupportedOperationException("Can't process integer and " + v.getType());
    }
}
