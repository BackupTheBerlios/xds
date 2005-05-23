package cz.vsb.pjp.project.grammar;

import java.util.Stack;

/**
 * Client stack code processor. Its methods are called immediately after the call stack has been determined.
 */
public abstract class ClientStackCodeProcessor {
    /**
     * Compare two stack items. Only called when internal code doesn't know how to determine them, OR if it
     * is not sure about type compatibility.
     *
     * @param a Stack item
     * @param b Stack item
     * @return Value just like compareTo()
     */
    public static int compareTwoStackItems(ExecuteStackItem a, ExecuteStackItem b) {
        return 0;
    }

    public abstract ExecuteStackItem translate(cz.vsb.pjp.project.Symbol n);

    public abstract boolean isSymbolAccepted(Symbol n);

    public abstract void processCode(Stack<ExecuteStackItem> s);
}
