package cz.vsb.pjp.project.grammar;

/**
* Generic pair implementation
 * @author luk117
 */
public class Pair <T1 extends Comparable, T2 extends Comparable> implements Comparable {
    protected T1 first;
    protected T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public int compareTo(Object o) {
        Pair p = (Pair) o;

        //if (p.first() instanceof  && p.second() instanceof T2)
        int cmp = p.first().compareTo(first);

        if (cmp == 0)
            cmp = p.second().compareTo(second);

        return cmp;
    }

    public T1 first() {
        return first;
    }

    public T2 second() {
        return second;
    }

    public String toString() {
        return "{" + first() + ", " + second() + "}";
    }

}
