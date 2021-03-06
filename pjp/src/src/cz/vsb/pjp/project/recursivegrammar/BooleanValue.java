package cz.vsb.pjp.project.recursivegrammar;

/**
 * Hodnota bool
 *
 * @author Vladimir Schafer - 15.5.2005 - 14:46:33
 */
public class BooleanValue extends Value {
    protected static final String type = "boolean";
    protected boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    public BooleanValue(String value) {
        this.value = Boolean.parseBoolean(value);
    }

    public void setValue(String value) {
        this.value = Boolean.parseBoolean(value);
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return Boolean.toString(value);
    }

    public boolean getRealValue() {
        return value;
    }

    /**
     * Podporovane operace: not
     *
     * @param operator
     * @return
     * @throws OperatorNotSupportedException
     */
    public Value performUnaryOperation(String operator) throws OperatorNotSupportedException {
        if (operator.equals("not")) {
            return new BooleanValue(!value);
        }
        throw new OperatorNotSupportedException("Unary operator: " + operator + " isn't supported");
    }

    public void setValue(Value v) throws OperatorNotSupportedException {
        if (v instanceof BooleanValue) {
            this.value = ((BooleanValue) v).value;
        } else if (v instanceof FakeValue) {
            return;
        } else
            throw new OperatorNotSupportedException("Can't assign " + v.getType() + " to " + type);
    }

    /**
     * Podporovane operace: and, or, =, <, >, <=, >=, =, <>
     *
     * @param operator
     * @param v        jen Integer
     * @return
     * @throws OperatorNotSupportedException
     */
    public Value performOperation(String operator, Value v) throws OperatorNotSupportedException {
        if (v instanceof BooleanValue) {
            BooleanValue val = (BooleanValue) v;
            if (operator.equals("and")) {
                return new BooleanValue(val.getRealValue() && value);
            } else if (operator.equals("or")) {
                return new BooleanValue(value || val.getRealValue());
            } else if (operator.equals("=")) {
                if (value == val.getRealValue())
                    return new BooleanValue(true);
                else
                    return new BooleanValue(false);
            } else if (operator.equals(">")) {
                if (value && !val.getRealValue())
                    return new BooleanValue(true);
                else
                    return new BooleanValue(false);
            } else if (operator.equals("<>")) {
                if (value == val.getRealValue())
                    return new BooleanValue(false);
                else
                    return new BooleanValue(true);
            } else if (operator.equals("<")) {
                if (!value && val.getRealValue())
                    return new BooleanValue(true);
                else
                    return new BooleanValue(false);
            } else if (operator.equals(">=")) {
                if ((value && !val.getRealValue()) || (value == val.getRealValue()))
                    return new BooleanValue(true);
                else
                    return new BooleanValue(false);
            } else if (operator.equals("<=")) {
                if ((!value && val.getRealValue()) || (value == val.getRealValue()))
                    return new BooleanValue(true);
                else
                    return new BooleanValue(false);
            } else
                throw new OperatorNotSupportedException("Operator " + operator + " isn't supported with bool type");
        } else if (v instanceof FakeValue)
            return this;
        else
            throw new OperatorNotSupportedException("Can't process bool and " + v.getType());
    }
}