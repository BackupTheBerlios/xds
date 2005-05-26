package cz.vsb.pjp.project.recursivegrammar;

/**
 * Hodnota realneho cisla
 *
 * @author Vladimir Schafer - 15.5.2005 - 14:25:22
 */
public class RealValue extends Value {
    protected static final String type = "real";
    protected double value;

    public RealValue(double value) {
        this.value = value;
    }

    public RealValue(String value) {
        this.value = Double.parseDouble(value);
    }

    public void setValue(String value) {
        this.value = Double.parseDouble(value);
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return Double.toString(value);
    }

    public double getRealValue() {
        return value;
    }

    /**
     * Podporovane operace: -
     *
     * @param operator
     * @return
     * @throws OperatorNotSupportedException
     */
    public Value performUnaryOperation(String operator) throws OperatorNotSupportedException {
        if (operator.equals("-")) {
            value = -value;
        } else
            throw new OperatorNotSupportedException("Unary operator: " + operator + " isn't supported");

        return new RealValue(value);
    }

    public void setValue(Value v) throws OperatorNotSupportedException {
        if (v instanceof RealValue) {
            this.value = ((RealValue) v).value;
        } else if (v instanceof IntegerValue) {
            this.value = ((IntegerValue) v).value;
        } else if (v instanceof FakeValue) {
            return;
        } else
            throw new OperatorNotSupportedException("Can't assign " + v.getType() + " to " + type);
    }


    /**
     * Podporovane operace: -, *, /, mod, <, >, <=, >=, <>, =
     *
     * @param operator
     * @param v        jen Integer
     * @return
     * @throws OperatorNotSupportedException
     */
    public Value performOperation(String operator, Value v) throws OperatorNotSupportedException {
        if (v instanceof IntegerValue) {
            v = new RealValue(((IntegerValue) v).getRealValue());
        }

        if (v instanceof RealValue) {
            RealValue val = (RealValue) v;
            if (operator.equals("+")) {
                return new RealValue(val.getRealValue() + value);
            } else if (operator.equals("-")) {
                return new RealValue(value - val.getRealValue());
            } else if (operator.equals("*")) {
                return new RealValue(value * val.getRealValue());
            } else if (operator.equals("/")) {
                return new RealValue(value / val.getRealValue());
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
            } else if (operator.equals("fake")) {
                return this;
            } else
                throw new OperatorNotSupportedException("Operator " + operator + " isn't supported with real type");
        } else if (v instanceof FakeValue)
            return this;
        else
            throw new OperatorNotSupportedException("Can't process real and " + v.getType());
    }
}
