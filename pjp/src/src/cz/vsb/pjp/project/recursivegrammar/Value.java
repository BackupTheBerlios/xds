package cz.vsb.pjp.project.recursivegrammar;

import cz.vsb.pjp.project.recursivegrammar.StringValue;
import cz.vsb.pjp.project.recursivegrammar.RealValue;
import cz.vsb.pjp.project.recursivegrammar.IntegerValue;
import cz.vsb.pjp.project.recursivegrammar.BooleanValue;

/**
 * Abstraktni trida pro hodnoty ukladane do tabulky symbolu.
 * Ukolem tridy je resit typovodou kontrolu jednotlivych promennych, provadet operace na promennych, eventuelne automaticke konverze.
 *
 * @author Vladimir Schafer - 15.5.2005 - 14:07:10
 */
public abstract class Value {
    /**
     * Vraci typ promenne
     *
     * @return
     */
    public abstract String getType();

    /**
     * Vraci hodnotu promenne jako textovy retezec
     *
     * @return
     */
    public abstract String getValue();

    /**
     * Na aktualni hodnote provede binarni operaci
     *
     * @param operator operator binarni operace
     * @param v        pravy operand (aktualni objekt je levy operand)
     * @return vypoctena hodnota
     * @throws UnsupportedOperationException pokud operace neni definovana
     */
    public abstract Value performOperation(String operator, Value v) throws UnsupportedOperationException;

    /**
     * Na objektu provede unarni operaci
     *
     * @param operator operator
     * @return vraci vypoctenou hodnotu
     * @throws UnsupportedOperationException pokud operand neni definovan
     */
    public abstract Value performUnaryOperation(String operator) throws UnsupportedOperationException;

    /**
     * Retezec se pokusit prevest na adekvatni hodnotu a nastavit ji
     *
     * @param value nova hodnota
     */
    public abstract void setValue(String value);

    /**
     * Nastavi novou hodnotu
     *
     * @param v
     * @throws UnsupportedOperationException Pokud neni mozne hodntou nastavit (chybny typ)
     */
    public abstract void setValue(Value v) throws UnsupportedOperationException;

    /**
     * Vraci vychozi hodnotu pro dany typ. Definovany jsou: Integer, Real, Boolean, String
     *
     * @param type
     * @return Vraci vychozi hodnotu
     * @throws UnsupportedOperationException pokud neni typ definovan
     */
    public static Value getDefaultValue(String type) throws UnsupportedOperationException {
        if (type.equals("integer"))
            return new IntegerValue(0);
        else if (type.equals("real"))
            return new RealValue(0);
        else if (type.equals("boolean"))
            return new BooleanValue(false);
        else if (type.equals("string"))
            return new StringValue("");
        else if (type.equals("fake"))
            return new FakeValue();
        else
            throw new UnsupportedOperationException("Unsupported data type");
    }

    public String toString() {
        return getValue();
    }
}
