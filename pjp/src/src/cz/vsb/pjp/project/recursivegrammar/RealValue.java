package cz.vsb.pjp.project.recursivegrammar;

/**
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

    public String getType() {
        return type;
    }

    public String getValue() {
        return Double.toString(value);
    }

    public double getRealValue() {
        return value;
    }

    public Value performUnaryOperation(String operator) throws UnsupportedOperationException {
        if (operator.equals("-")) {
            value = -value;
        } else
            throw new UnsupportedOperationException("Unary operator: " + operator + " isn't supported");

        return new RealValue(value);
    }

    public Value performOperation(String operator, Value v) throws UnsupportedOperationException {
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
            } else
                throw new UnsupportedOperationException("Operator " + operator + " isn't supported with real type");
        } else
            throw new UnsupportedOperationException("Can't process Real and " + v.getType());
    }
}