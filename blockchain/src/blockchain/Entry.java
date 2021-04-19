package blockchain;

import java.util.Date;
import java.util.List;

/** The abstraction of data stored in a single Transaction. */
public abstract class Entry {
  /** The time of creation of this Entry. */
  private Date timeStamp;

  public Entry() {
    this.timeStamp = new Date(System.currentTimeMillis());
  }

  public abstract void processEntry(List<Object> inputEntries) throws IllegalArgumentException;

  public abstract boolean validateEntry();

  /**
   * Returns the id of this Entry.
   *
   * @return the id of this Entry
   */
  public abstract String getId();

  /**
   * Returns the time stamp of creation of this Entry.
   *
   * @return the time stamp of creation of this Entry
   */
  public final Date getTimeStamp() {
    return timeStamp;
  }
}
