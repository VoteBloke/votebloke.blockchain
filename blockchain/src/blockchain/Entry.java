package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/** The abstraction of data stored in a single Transaction. */
public abstract class Entry extends Object {
  /** The time of creation of this Entry. */
  private Date timeStamp;

  public Entry() {
    this.timeStamp = new Date(System.currentTimeMillis());
  }

  public abstract ArrayList<TransactionOutput> processEntry(List<TransactionInput> inputEntries)
      throws IllegalArgumentException;

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

  public abstract String getEntryType();

  public abstract String getAuthor();

  /**
   * Returns the summary of this Entry.
   *
   * <p>This is a factory method generating a summary of this Entry in a form of a HashMap. This
   * summary can be used to create a simplified JSON representation of this Entry.
   *
   * @return the summary of this Entry
   */
  public abstract HashMap<String, String[]> getMetadata();
}
