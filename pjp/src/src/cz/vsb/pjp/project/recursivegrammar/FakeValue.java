package cz.vsb.pjp.project.recursivegrammar;

/**
 * Pouziva se pro zotaveni, jako univerzalni hodnota. Lze ji kombinovat s jakymkoliv operatorem a typem
 *
 * @author Vladimir Schafer - 18.5.2005 - 20:29:02
 */
public class FakeValue extends Value {
    public String getType() {
        return "fake";
    }

    public String getValue() {
        return "fake";
    }

    public Value performOperation(String operator, Value v) throws UnsupportedOperationException {
        return this;
    }

    public Value performUnaryOperation(String operator) throws UnsupportedOperationException {
        return this;
    }

    public void setValue(String value) {
        return;
    }

    public void setValue(Value v) throws UnsupportedOperationException {
        return;
    }
}
