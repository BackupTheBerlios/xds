package cz.xds;

import java.util.HashMap;

/**
 * Date: 22.12.2004
 * Time: 0:01:40
 */
public class IDFactory {
    private HashMap ids = new HashMap();

    public long createNewID(FileSystemItem i) {
        long key = findFreeID();

        ids.put(key, i);
        return key;
    }

    public void deleteID(long id) {
        ids.remove(id);
    }

    private long findFreeID() {
        long maxId = -1;

        // Tiger tamed :-)
        for (Object o: ids.keySet()) {
            long i = ((Long)o).longValue();

            if (i > maxId)
                maxId = i;
        }

        return maxId+1;
    }
}
