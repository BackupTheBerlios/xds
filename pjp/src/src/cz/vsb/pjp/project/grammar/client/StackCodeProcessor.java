package cz.vsb.pjp.project.grammar.client;

import cz.vsb.pjp.project.grammar.ExecuteStackItem;
import cz.vsb.pjp.project.grammar.Symbol;

import java.util.Stack;
import java.util.TreeMap;
import java.io.*;

// TODO: Chybne parsovani vyrazu "=", "+" ve write
// TODO: Lex-komentare
// TODO: Uvozovky z retezcu!
// TODO: vsechno je malym??!!
// TODO: logicke operatory
// TODO: concat
public class StackCodeProcessor {
    Stack<ExecuteStackItem> pool = new Stack<ExecuteStackItem>();
    TreeMap<String, Variable> symTable = new TreeMap<String, Variable>();

    public static final int FUNCTION_CALL   = 0;
    public static final int IDENTIFIER      = 1;
    public static final int CONST_STRING    = 2;
    public static final int CONST_DOUBLE    = 3;
    public static final int CONST_INTEGER   = 4;
    public static final int CONST_BOOL      = 5;
    public static final int OPERATION       = 6;
    public static final int ASSIGNMENT      = 7;
    public static final int FUNCTION_PARAM  = 8;
    public static final int DECLARATION     = 9;

    public boolean isSymbolAccepted(Symbol n) {
        if (n.getName().compareTo(";") == 0)
            return false;

        return true;
    }

    public ExecuteStackItem translate(cz.vsb.pjp.project.Symbol n) {
        if (n.getAtt().equals("write") || n.getAtt().equals("read"))
            return new ExecuteStackItem(FUNCTION_CALL, n.getAtt(), null);
        if (n.getName().equals("ident"))
            return new ExecuteStackItem(IDENTIFIER, n.getAtt(), null);
        if (n.getName().equals("numberint"))
            return new ExecuteStackItem(CONST_INTEGER, null, new Double(n.getAtt()));
        if (n.getName().equals("numberdouble"))
            return new ExecuteStackItem(CONST_DOUBLE, null, new Double(n.getAtt()));
        if (n.getName().equals("string"))
            return new ExecuteStackItem(CONST_STRING, n.getName(), n.getAtt().replaceAll("\"", ""));
        if (n.getName().equals("arithmetical") || n.getAtt().equals(".") || n.getAtt().equals("relation") ||
            n.getName().equals("logic"))
            return new ExecuteStackItem(OPERATION, n.getName(), n.getAtt());
        if (n.getName().equals("colon"))
            return new ExecuteStackItem(ASSIGNMENT, null, null);
        if (n.getAtt().equals(","))
            return new ExecuteStackItem(FUNCTION_PARAM, null, null);
        if (n.getName().equals("var"))
            return new ExecuteStackItem(FUNCTION_CALL, "declare", null);
        if (n.getName().equals("type"))
            return new ExecuteStackItem(CONST_STRING, n.getAtt(), null);
        if (n.getName().equals("true") || n.getName().equals("false"))
            return new ExecuteStackItem(CONST_BOOL, n.getAtt(), n.getName().equals("true"));
        if (n.getName().equals("not"))
            return new ExecuteStackItem(OPERATION, n.getName(), n.getAtt());
        return null;
    }

    public void processCode(Stack<ExecuteStackItem> s) {
        System.out.println("PROCESSING: " + s);

        while (true) {
            if (s.isEmpty())
                break;

            while(s.peek().type != OPERATION && s.peek().type != ASSIGNMENT
                    && s.peek().type != FUNCTION_PARAM && s.peek().type != FUNCTION_CALL) {
                pool.push(s.pop());
            }

            if (s.peek().type == ASSIGNMENT) {
                s.pop();
                setVariable(s.pop());
            }
            else if (s.peek().type == FUNCTION_PARAM) {
                s.pop();
                continue;
            }
            else if (s.peek().type == FUNCTION_CALL)
                callFunction(s.pop());
            else
                processFunction(s.pop());
        }
    }

    protected void setVariable(ExecuteStackItem var) {
        Variable varObj = getSafeVariable(var.data);
        if (varObj == null) {
            runtimeError("Undeclared identifier", false);
            pool.pop();
        }
        else {
            if (pool.isEmpty()) {
                runtimeError("Identifier expected on top of the code stack", false);
                return;
            }

            ExecuteStackItem p = pool.pop();

            if (p.type == IDENTIFIER) {
                // assignment identifier := identifier
                Variable source = symTable.get(p.data);
                if (source != null) {
                    if (varObj.sameTypeAs(source))
                        varObj.value = source.value;
                    else if (varObj.type == Variable.TYPE_REAL && p.type == Variable.TYPE_INTEGER) {
                        // automatical conversion int->double
                        varObj.value = new Double(((Integer)p.value).intValue());
                    } else
                        runtimeError("Identifiers " + varObj + " and " + p + " don't have compatible types", false);
                }
            } else if (p.type == CONST_DOUBLE) {
                if (varObj.type == Variable.TYPE_INTEGER)
                    varObj.value = new Integer(((Double)p.value).intValue());
                else
                    varObj.value = p.value;
            } else if (p.type == CONST_INTEGER) {
                if (varObj.type == Variable.TYPE_INTEGER)
                    varObj.value = new Integer(((Double)p.value).intValue());
                else if (varObj.type == Variable.TYPE_REAL)
                    varObj.value = new Double(((Double)p.value).intValue());
                else
                    runtimeError("Incompatible types", false);
            } else if (p.type == CONST_STRING) {
                if (varObj.type == Variable.TYPE_STRING)
                    varObj.value = p.value;
                else
                    runtimeError("Incompatible types", false);
            } else if (p.type == CONST_BOOL) {
                if (varObj.type == Variable.TYPE_BOOLEAN)
                    varObj.value = p.value;
                else
                    runtimeError("Incompatible types (boolean)", false);
            } else {
                runtimeError("Strange assignment - not implemented", false);
            }
        }
    }

    protected void processFunction(ExecuteStackItem function) {
        if (function.type == OPERATION) {
            // unary operations
            if (function.value.equals("not") || function.value.equals("unary")) {
                ExecuteStackItem a = pool.pop();

                fillValue(a);

                if (a.value == null) {
                    runtimeError("Operation '" + function.value + " " + a.data + "' not valid for that data type", false);
                    return;
                }

                if (function.value.equals("not")) {
                    if (expectType(a, CONST_BOOL)) {
                        pool.push(new ExecuteStackItem(CONST_BOOL, null,
                            new Boolean(!((Boolean)a.value).booleanValue())));
                        return;
                    }
                } else if (function.value.equals("unary"))  {

                }

            }

            ExecuteStackItem a = pool.pop();
            ExecuteStackItem b = pool.pop();

            fillValue(a);
            fillValue(b);

            if (a.value == null || b.value == null) {
                runtimeError("Operation '" + a.data + " " + function.value + " " + b.data + "' not valid for that data type", false);
                return;
            }

            if (function.value.equals("mod")) {
                if (expectType(a, b, CONST_DOUBLE) || expectType(a, b, CONST_INTEGER)) {
                    pool.push(new ExecuteStackItem(getResultType(a.type, b.type), null,
                        new Double(((Double)b.value).doubleValue() % ((Double)a.value).doubleValue())));
                    return;
                }
            } else if (function.value.equals("/")) {
                if (expectType(a, b, CONST_DOUBLE) || expectType(a, b, CONST_INTEGER)) {
                    pool.push(new ExecuteStackItem(getResultType(a.type, b.type), null,
                        new Double(((Double)b.value).doubleValue() / ((Double)a.value).doubleValue())));
                    return;
                }
            } else if (function.value.equals("+")) {
                if (expectType(a, b, CONST_DOUBLE) || expectType(a, b, CONST_INTEGER)) {
                    pool.push(new ExecuteStackItem(getResultType(a.type, b.type), null,
                        new Double(((Double)b.value).doubleValue() + ((Double)a.value).doubleValue())));
                    return;
                }
            } else if (function.value.equals("-")) {
                if (expectType(a, b, CONST_DOUBLE) || expectType(a, b, CONST_INTEGER)) {
                    pool.push(new ExecuteStackItem(getResultType(a.type, b.type), null,
                        new Double(((Double)b.value).doubleValue() - ((Double)a.value).doubleValue())));
                    return;
                }
            } else if (function.value.equals("*")) {
                if (expectType(a, b, CONST_DOUBLE) || expectType(a, b, CONST_INTEGER)) {
                    pool.push(new ExecuteStackItem(getResultType(a.type, b.type), null,
                        new Double(((Double)b.value).doubleValue() * ((Double)a.value).doubleValue())));
                    return;
                }
            } else if (function.value.equals(".")) {
                if (expectType(a, b, CONST_STRING)) {
                    pool.push(new ExecuteStackItem(CONST_STRING, null,
                        new String( (String)b.value + (String)a.value )));
                    return;
                }
                //else
                //    runtimeError("Can't concatenate variables that aren't both strings", false);
            } else if (function.value.equals("and")) {
                if (expectType(a, b, CONST_BOOL)) {
                    pool.push(new ExecuteStackItem(CONST_BOOL, null,
                        new Boolean(((Boolean)b.value).booleanValue() && ((Boolean)a.value).booleanValue())));
                    return;
                }
            } else if (function.value.equals("or")) {
                if (expectType(a, b, CONST_BOOL)) {
                    pool.push(new ExecuteStackItem(CONST_BOOL, null,
                        new Boolean(((Boolean)b.value).booleanValue() || ((Boolean)a.value).booleanValue())));
                    return;
                }
            } else {
                runtimeError("Unknown binary operation: " + function, false);
                return;
            }

            runtimeError("Operation '" + function + "' not allowed to this data type", false);
        }
    }

    private int getResultType(int a, int b) {
        if (a == CONST_INTEGER && b==CONST_INTEGER)
            return CONST_INTEGER;
        return CONST_DOUBLE;
    }

    private void callFunction(ExecuteStackItem func) {
        if (func.data.equalsIgnoreCase("write")) {
            while (!pool.isEmpty()) {
                ExecuteStackItem param = pool.pop();

                if (param.type == IDENTIFIER) {
                    Variable v = getSafeVariable(param.data);
                    if (v != null)
                        System.out.print(v.value);
                } else {
                    if (param.type == CONST_INTEGER)
                        System.out.print(((Double)param.value).intValue());
                    else
                        System.out.print(param.value);

                }
            }
            System.out.println();
        } else if (func.data.equalsIgnoreCase("declare")) {
            String type = (String)pool.pop().data;

            while (pool.size() > 0) {
                Variable newVar = new Variable(type, pool.pop().data, null);
                if (symTable.containsKey(newVar.name))
                    runtimeError("Variable " + newVar.name + " already defined. Second definition ignored", false);
                else {
                    symTable.put(newVar.name, newVar);
                }
            }
        } else if (func.data.equalsIgnoreCase("read")) {
            while (!pool.isEmpty()) {
                ExecuteStackItem param = pool.pop();

                if (param.type == IDENTIFIER) {
                    Variable v = getSafeVariable(param.data);
                    if (v != null) {
                        try {
                            String str = (new LineNumberReader(new InputStreamReader(System.in))).readLine();

                            if (v.type == Variable.TYPE_STRING)
                                v.value = str;
                            else if (v.type == Variable.TYPE_REAL)
                                v.value = new Double(str);
                            else
                                v.value = new Integer(str);
                        } catch (IOException e) {
                            v.value = "";
                            runtimeError("I/O error occured: " + e.getMessage(), false);
                        } catch (NumberFormatException e) {
                            runtimeError("Invalid number format", false);
                        }
                    }
                } else {
                    runtimeError("Identifier expected", false);
                    pool.empty();
                }
            }
        } else if (func.data.equalsIgnoreCase("write")) {
            while (!pool.isEmpty()) {

            }
        } else {
            runtimeError("Unknown function: '" + func.data +"'", false);
            pool.empty();
        }
    }

    private void runtimeError(String err, boolean critical) {
        if (critical)
            System.err.println("*** CRITICAL ERROR: " + err);
        else
            System.err.println("*** RUNTIME ERROR: " + err);
    }

    private void fillValue(ExecuteStackItem a) {
        if (a.type == IDENTIFIER) {
            Variable v = getSafeVariable(a.data);

            if (v == null)
                return;

            if (v.type == Variable.TYPE_REAL)
                a.value = v.value;
            else if (v.type == Variable.TYPE_INTEGER) {
                if (v.value instanceof Double)
                    a.value = new Double(((Double)v.value).intValue());
                else
                    a.value = new Double(((Integer)v.value).intValue());

                a.type = CONST_INTEGER;
            } else if (v.type == Variable.TYPE_BOOLEAN)
                a.value = v.value;
        }
    }

    private Variable getSafeVariable(String name) {
        Variable v=symTable.get(name);
        if (v == null)
            runtimeError("Variable '" + name + "' not declared", false);
        return v;
    }

    private boolean expectType(ExecuteStackItem a, int type) {
        int t = (a.type == IDENTIFIER) ? -1 : a.type;

        if (t == -1) {
            Variable v=getSafeVariable(a.data);
            if (v != null) {
                t = v.toConstType();
            }
        }

        return t==type;
    }

    private boolean expectType(ExecuteStackItem a, ExecuteStackItem b, int type) {
        return expectType(a, type) && expectType(b, type);
    }
}