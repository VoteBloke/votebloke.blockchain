package blockchain;

import java.util.Date;
import java.util.List;

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

    public abstract void processEntry(List<Object> inputEntries) throws IllegalArgumentException;
    public abstract boolean validateEntry();
    public abstract String getId();

    final public Date getTimeStamp() {
        return timeStamp;
    }
}
