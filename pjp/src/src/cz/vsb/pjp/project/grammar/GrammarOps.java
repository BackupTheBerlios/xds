package cz.vsb.pjp.project.grammar;

import cz.vsb.pjp.project.grammar.DecompositionTable;
import cz.vsb.pjp.project.grammar.Grammar;
import cz.vsb.pjp.project.grammar.GrammarImpl;

import java.util.*;

/**
 * Implementace výpoètù nad gramatikou
 */
public class GrammarOps {
    /**
     * Vytvori instanci objektu a provede vypocet
     */
    public GrammarOps(Grammar g) {
        this.g = g;
        compute_empty();
        compute_sets();
    }

    /**
     * Vrati mnozinu nonterminalu generujicich prazdne slovo
     *
     * @return Mnozina symbolu typu Nonterminal.
     */
    public Set getEmptyNonterminals() {
        return emptyNonterminals;
    }

    /**
     * Vypocet mnoziny nonterminalu generujicich prazdne slovo.
     */
    private void compute_empty() {
        emptyNonterminals = new TreeSet();

        boolean anyChange = true;
        while (anyChange) {
            anyChange = false;
            Iterator i = g.getRules();

            while (i.hasNext()) {
                boolean clear = true;
                Rule r = (Rule) i.next();

                Iterator sym_it = r.getRHS().iterator();

                if (!sym_it.hasNext())
                    clear = true;
                else
                    while (sym_it.hasNext())
                        if (!emptyNonterminals.contains(sym_it.next())) {
                            clear = false;
                            break;
                        }

                if (clear && !emptyNonterminals.contains(r.getLHS())) {
                    emptyNonterminals.add(r.getLHS());
                    anyChange = true;
                }
            }
        }
    }

    private void compute_sets() {
        first = new TreeMap<Nonterminal, TreeSet<Terminal>>();
        follow = new TreeMap<Nonterminal, TreeSet<Terminal>>();

        Iterator nonterminals = g.getNonterminals();

        while (nonterminals.hasNext()) {
            Nonterminal nt = (Nonterminal) nonterminals.next();

            System.out.println("first(" + nt + ")=" + first(nt));

            first.put(nt, first(nt));

            // pozue predvypocet
            follow.put(nt, follow(nt));
            //System.out.println("follow(" + nt + ")=" + follow(nt));
        }

        nonterminals = g.getNonterminals();

        while (nonterminals.hasNext()) {
            Nonterminal nt = (Nonterminal) nonterminals.next();

            // konecny vypocet
            follow.put(nt, follow(nt));
            System.out.println("follow(" + nt + ")=" + follow(nt));
        }

    }

    TreeSet<Terminal> first(Symbol symbol) {
        TreeSet<Terminal> temp = null;

        if (symbol instanceof Nonterminal && ((temp = first.get((Nonterminal) symbol)) != null))
            return temp;

        Collection a = new Vector();
        a.add(symbol);

        return first(a.iterator());
    }

    TreeSet<Terminal> first(Iterator i_sym) {
        TreeSet<Terminal> set = new TreeSet<Terminal>();

        // (i) pravidlo I
        // FIRST(e) = {e}
        if (!i_sym.hasNext()) {
            set.add(GrammarImpl.EMPTY_TERMINAL);
            return set;
        }

        while (i_sym.hasNext()) {
            Symbol currentSymbol = (Symbol) i_sym.next();

            // (ii) pravidlo II
            // FIRST(x) = {x}
            if (currentSymbol instanceof Terminal) {
                set.add((Terminal) currentSymbol);
                break;
            }

            Iterator rules = g.getRules();

            // (iii) pravidlo III
            // prolezt vsechna pravidla, jejichz leva strana je dany symbol
            while (rules.hasNext()) {
                Rule r = (Rule) rules.next();

                if (!r.getLHS().equals(currentSymbol))
                    continue;

                Iterator rh = r.getRHS().iterator();

                boolean all = true;
                while (rh.hasNext()) {
                    Set<Terminal> newSet = first(rh);

                    set.addAll(newSet);

                    if (!newSet.contains(GrammarImpl.EMPTY_TERMINAL)) {
                        all = false;
                        break;
                    }
                }

                if (all)
                    set.add(GrammarImpl.EMPTY_TERMINAL);
                else
                    set.remove(GrammarImpl.EMPTY_TERMINAL);
            }

            if (!emptyNonterminals.contains(currentSymbol))
                break;
        }

        return set;
    }

    TreeSet<Terminal> follow(Nonterminal nt) {
        TreeSet<Terminal> set = new TreeSet<Terminal>();

        // do follow(start) pridej {e}
        if (g.getStartNonterminal().compareTo(nt) == 0)
            set.add(GrammarImpl.EMPTY_TERMINAL);

        Iterator rules = g.getRules();

        // prohledat vsechna pravidla a najit nas neterminal. Potom:
        //       (i) je-li na konci nebo prepsatelny na {e}, pridat do mnoziny follow leve strany
        //      (ii) neni na konci, pridat first dalsiho symbolu. Pokud first obsahuje prazdny symbol,
        //           pridame follow nasledujiciho symbolu, v opacnem pripade pokracujeme dalsim pravidlem.
        while (rules.hasNext()) {
            Rule r = (Rule) rules.next();

            Iterator rh = r.getRHS().iterator();

            while (rh.hasNext()) {
                Symbol sym = (Symbol) rh.next();

                if (!(sym instanceof Nonterminal) || sym.compareTo(nt) != 0)
                    continue;

                if (!rh.hasNext()) {
                    // (i)
                    TreeSet<Terminal> s = follow.get(r.getLHS());

                    if (s != null)
                        set.addAll(s);
                } else {
                    // (ii)
                    Symbol what = (Symbol) rh.next();

                    // pridat first dalsiho pravidla
                    set.addAll(first(what));

                    // konec - mame platny follow
                    if (!set.contains(GrammarImpl.EMPTY_TERMINAL))
                        break;

                    // first obsahuje prazdny symbol, je nutne pridat follow nasledujiciho neterminalu
                    if (what instanceof Nonterminal) {
                        TreeSet<Terminal> s = follow.get(what);

                        if (s != null)
                            set.addAll(s);
                    }
                }
            }
        }

        return set;
    }

    public DecompositionTable getDecompositionTable() {
        DecompositionTable table = new DecompositionTable();

        Iterator rules = g.getRules();

        while (rules.hasNext()) {
            Rule rule = (Rule) rules.next();

            // vypocitat mnozinu SELECT
            TreeSet<Terminal> select = first(rule.getRHS().iterator());

            if (select.size() == 1 && select.first().compareTo(GrammarImpl.EMPTY_TERMINAL) == 0)
                select = follow(rule.getLHS());

            //System.out.println(select);

            // narvat to do tabulky
            for (Terminal trm : select) {
                Pair<Terminal, Nonterminal> key = new Pair<Terminal, Nonterminal>(trm, rule.getLHS());

                if (table.containsKey(key))
                    System.out.println("Ambiguous transition definition: " + key + " --- skipped.");
                else
                    table.put(key, rule);
            }
        }

        return table;
    }

    /**
     * Gramatika
     */
    Grammar g;

    /**
     * Mnozina nonterminalu generujicich prazdny retezec
     */
    Set emptyNonterminals;

    TreeMap<Nonterminal, TreeSet<Terminal>> follow;
    TreeMap<Nonterminal, TreeSet<Terminal>> first;
}
