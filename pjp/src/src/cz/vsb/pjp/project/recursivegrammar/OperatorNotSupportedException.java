package cz.vsb.pjp.project.recursivegrammar;

/**
 * Created by IntelliJ IDEA.
 * User: sch110
 * Date: 26.5.2005
 * Time: 23:13:17
 * To change this template use File | Settings | File Templates.
 */
public class OperatorNotSupportedException extends Exception {
    public OperatorNotSupportedException(String s) {
        super(s);
    }

    public OperatorNotSupportedException() {
        super();
    }
}
