package cz.xds;

import java.util.HashMap;

/**
 * Tøída generujeunikatní identifikaèní èísla nových objektù FileSystemItem
 */
public class IDFactory {
    // Mapa id->FileSystemItem (pro perzistenci)
    private HashMap ids = new HashMap();

    /**
     * Najde slot pro nove id a vrati jej
     *
     * @param i Nove vytvarena polozka
     * @return
     */
    public long createNewID(FileSystemItem i) {
        long key = findFreeID();

        ids.put(new Long(key), i);
        return key;
    }

    /**
     * Vymazani daneho id z mapy
     *
     * @param id id pro vymaz
     */
    public void deleteID(long id) {
        ids.remove(new Long(id));
    }

    /**
     * Nalezne nejnizsi volne id a vrati jej
     *
     * @return Nejnizsi volny slot (id)
     */
    protected long findFreeID() {
        long maxId = -1;

        // Tiger tamed :-)
        java.util.Iterator it = ids.keySet().iterator();
        while (it.hasNext()) {
            long i = ((Long) it.next()).longValue();

            if (i > maxId)
                maxId = i;
        }

        return maxId + 1;
    }
}
