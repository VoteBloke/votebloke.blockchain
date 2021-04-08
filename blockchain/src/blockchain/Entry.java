package blockchain;

import java.util.ArrayList;
import java.util.Date;

/** The abstraction of data stored in a single Transaction.
 *
 *
 */
public abstract class Entry {
    /**
     * The time of creation of this Entry.
     */
    private Date timeStamp;

    public Entry() {
        this.timeStamp = new Date(System.currentTimeMillis());
    }

    public abstract void processEntry(ArrayList<Entry> inputEntries) throws IllegalArgumentException;

    public Date getTimeStamp() {
        return timeStamp;
    }
}
