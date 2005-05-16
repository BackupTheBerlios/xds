package cz.vsb.pjp.project.recursivegrammar;

import cz.vsb.uti.sch110.automata.AutomatException;
import cz.vsb.pjp.project.recursivegrammar.StringValue;
import cz.vsb.pjp.project.recursivegrammar.RealValue;
import cz.vsb.pjp.project.recursivegrammar.GrammarException;
import cz.vsb.pjp.project.LexicalAutomata;
import cz.vsb.pjp.project.Symbol;
import cz.vsb.pjp.project.NoMoreTokensException;
import cz.vsb.pjp.project.PJPLexicalAutomata;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author Vladimir Schafer - 15.5.2005 - 10:40:34
 */
public class Grammar {
    //TODO Support for not, -
    //TODO Konverze int do real pri prirazovani
    //TODO Funkce
    LexicalAutomata l;
    Symbol s;
    HashSet<Variable> symbolTable = new HashSet<Variable>();

    public Grammar(LexicalAutomata l) {
        this.l = l;
    }

    public void expect() throws AutomatException, IOException, NoMoreTokensException {
        s = l.getToken();
    }


    public void Statement() throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        if (l.hasTokens())
            s = l.getToken();
        else
            return;

        if (s.getName().equals("ident")) {
            String name = s.getAtt();
            B(name);
            if (!s.getName().equals("semicolon")) ; //error;
            Statement();
        } else if (s.getName().equals("var")) {
            Declaration();
            if (!s.getName().equals("semicolon")) ; //error;
            Statement();
        } else if (s.getName().equals("semicolon")) {
            Statement();
        }
    }

    public void Declaration() throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        expect(); //var
        C();
    }

    public String C() throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        Symbol tmp = s;
        expect(); //ident
        String type = D();


        Variable v = new Variable(tmp.getAtt(), Value.getDefaultValue(type));
        if (symbolTable.contains(v)) {
            throw new GrammarException("Variable is already defined");
        } else {
            symbolTable.add(v);
        }
        System.out.println(tmp.getAtt() + " - " + type);
        return type;
    }

    public String D() throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        if (s.getAtt().equals(",")) {
            expect();
            return C();
        } else if (s.getName().equals("colon")) {
            expect();
            String type;
            if (s.getName().equals("type")) {
                type = s.getAtt();
                expect();
            } else {
                throw new NoMoreTokensException();
            }
            return type;
        }

        throw new NoMoreTokensException();
    }

    public void B(String name) throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        expect();
        if (s.getAtt().equals(":")) {
            Assign(name);
        } else if (s.getName().equals("(")) {
            //Func;
        }
    }

    public void Assign(String prom) throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        expect(); //:
        expect();  //=
        //Assign
        Value value = Expr();
        Variable v = new Variable(prom, value);
        if (symbolTable.contains(v)) {
            Iterator<Variable> i = symbolTable.iterator();
            while (i.hasNext()) {
                v = i.next();
                if (v.name.equals(prom)) {
                    v.setValue(value);
                }
            }
        } else {
            throw new GrammarException("Variable " + prom + " is not defined");
        }
        System.out.println(prom + " - " + value);
    }

    public Value Expr() throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        Value data = E();
        if (s.getName().equals("arithmeticalb") || s.getName().equals("concat") || s.getName().equals("or") || s.getName().equals("relation")) {
            data = E1(data);
        }
        return data;
    }

    public Value E1(Value in) throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        if (s.getName().equals("arithmeticalb") || s.getName().equals("concat") || s.getName().equals("or")) {
            in = G(in);
        }
        if (s.getName().equals("relation")) {
            in = E2(in);
        }

        return in;
    }

    public Value E2(Value in) throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        Symbol tmp = s;
        if (s.getName().equals("relation")) {
            expect();
            Value value = E();
            //value = E1(value);
            value = G(value);
            in = in.performOperation(tmp.getAtt(), value);
            in = E2(in);
        }

        return in;
    }

    public Value G(Value in) throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        if (s.getName().equals("arithmeticalb") || s.getName().equals("concat") || s.getName().equals("or")) {
            String oper = s.getAtt();
            expect();
            Value value = E();
            // vyhodnotit
            in = in.performOperation(oper, value);
            in = G(in);
        }

        return in;
    }

    public Value E() throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        Value value = H();
        value = T1(value);
        return value;
    }

    public Value T1(Value in) throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        Symbol tmp = s;
        if (tmp.getName().equals("arithmeticala")) {
            expect(); //arithmeticala
            Value valueB = H();
            //Vynasobit in s valueB
            in = in.performOperation(tmp.getAtt(), valueB);
            in = T1(in);
        }
        return in;
    }

    public Value H() throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        Symbol tmp = s;
        expect(); //leftp, number, ...
        if (tmp.getName().equals("leftp")) {
            Value val = Expr();
            if (s.getName().equals("rightp")) expect();
            return val;
        } else if (tmp.getName().equals("ident")) {
            Iterator<Variable> i = symbolTable.iterator();
            Variable v;
            while (i.hasNext()) {
                v = i.next();
                if (v.name.equals(tmp.getAtt())) {
                    return v.getValue();
                }
            }
            throw new GrammarException("Variable " + tmp.getAtt() + " doesn't exist");
        } else if (tmp.getName().equals("true")) {
            return new BooleanValue(true);
        } else if (tmp.getName().equals("false")) {
            return new BooleanValue(false);
        } else if (tmp.getName().equals("numberint")) {
            return new IntegerValue(tmp.getAtt());
        } else if (tmp.getName().equals("numberdouble")) {
            return new RealValue(tmp.getAtt());
        } else if (tmp.getName().equals("string")) {
            return new StringValue(tmp.getAtt());
        } else {
            throw new NoMoreTokensException();
        }
    }

    public static void main(String[] args) {
        try {

            InputStream in = new FileInputStream("src//rules2.lex");
            InputStream data = new FileInputStream("src//vtest.txt");
            LexicalAutomata la = PJPLexicalAutomata.getPJPAutomata(in);
            la.setSource(data);

            Grammar g = new Grammar(la);
            g.Statement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
