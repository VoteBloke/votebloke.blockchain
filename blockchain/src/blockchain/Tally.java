package blockchain;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/** A representation of a tally of votes in particular elections. */
public class Tally extends Entry {
  /** The unique identifier of this Tally. */
  private String id;

  /** The elections that are tallied in this Tally. */
  private Elections elections;

  /** The Vote objects getting tallied in this Tally. */
  private List<Vote> votes;

  /** The public ECDSA key of Account that tallies the votes in the elections. */
  private final PublicKey teller;

  /**
   * A constructor for Tally.
   *
   * @param teller the public ECDSA key of Account that tallies the votes in the elections
   * @param inputEntries the list comprised of a single Elections object at the first position and
   *     Vote objects getting tallied in this Tally in the following positions
   * @throws IllegalArgumentException if any of the Vote objects don't match the Elections object or
   *     are itself not validated
   */
  public Tally(PublicKey teller, List<Entry> inputEntries) throws IllegalArgumentException {
    super();
    this.teller = teller;
    setElections(inputEntries.get(0));
    setVotes(inputEntries.subList(1, inputEntries.size() - 1));
  }

  /**
   * A constructor for Tally.
   *
   * @param teller the public ECDSA key of Account that tallies the votes in the elections
   * @param elections the Elections object representing the elections that get tallied in this Tally
   * @param votes the list of Vote objects to tally
   * @throws IllegalArgumentException if any of the Vote objects don't match the Elections object or
   *     are itself not validated
   */
  public Tally(PublicKey teller, Elections elections, List<Vote> votes) {
    super();
    this.teller = teller;
    this.elections = elections;
    this.votes = votes;
  }

  /**
   * A constructor for Tally.
   *
   * @param teller the public ECDSA key of Account that tallies the votes in the elections
   */
  public Tally(PublicKey teller) {
    this(teller, null, null);
  }

  /**
   * Processes this Tally. Performs basic validation checks and sets up the id of this Tally.
   *
   * @param inputEntries the list comprised of a single Elections object at the first position and
   *     Vote objects getting tallied in this Tally in the following positions
   * @throws IllegalArgumentException if any of the Vote objects don't match the Elections object or
   *     are itself not validated
   * @return
   */
  @Override
  public final List<TransactionOutput> processEntry(List<TransactionInput> inputEntries) throws IllegalArgumentException {
    ArrayList<Entry> votes = new ArrayList<>();
    for(TransactionInput transactionInput : inputEntries.subList(1, inputEntries.size() - 1)) {
      votes.add(transactionInput.transactionOut.data);
    }
    return (processEntry(inputEntries.get(0).transactionOut, votes));
  }

  /**
   * Processes this Tally. Performs basic validation checks and sets up the id of this Tally.
   *
   * @param elections the Elections object representing the elections that get tallied in this Tally
   * @param votes the list of Vote objects to tally
   * @throws IllegalArgumentException if any of the Vote objects don't match the Elections object or
   *     are itself not validated
   * @return
   */
  public final ArrayList<TransactionOutput> processEntry(Object elections, List<Entry> votes)
      throws IllegalArgumentException {
    setElections(elections);
    setVotes(votes);
    return (processEntry());
  }

  /**
   * Processes this Tally. Performs basic validation checks and sets up the id of this Tally.
   *
   * @throws IllegalArgumentException if any of the Vote objects don't match the Elections object or
   *     are itself not validated
   * @return
   */
  public final ArrayList<TransactionOutput> processEntry() throws RuntimeException {
    if (!elections.validateEntry()) {
      throw new RuntimeException(
          "Entry elections: "
              + elections.getId()
              + " does not validate - its hash does not match its contents");
    }

    for (Vote vote : votes) {
      if (!vote.getElectionsId().equals(elections.getId())) {
        throw new RuntimeException(
            "Vote: "
                + vote.getId()
                + " was cast in elections: "
                + vote.getElectionsId()
                + " ,but should be cast in: "
                + elections.getId());
      }
      if (!vote.validateEntry()) {
        throw new RuntimeException(
            "Entry: " + vote.getId() + " does not validate - its hash does not match its contents");
      }
    }
    updateId();

    return new ArrayList<TransactionOutput>(List.of(new TransactionOutput(teller, this)));
  }

  /**
   * Validates this Tally.
   *
   * @return true if the recalculated hash (id) matches the current if of this Tally; false
   *     otherwise
   */
  @Override
  public final boolean validateEntry() {
    return getId()
        .equals(
            StringUtils.hashString(
                StringUtils.keyToString(teller)
                    + elections.getId()
                    + votes.toString()
                    + getTimeStamp()));
  }

  /**
   * Casts `entry` to Elections and sets it as the Elections object of this Tally.
   *
   * @param entry the Entry object, which can be cast to Elections
   */
  public final void setElections(Object entry) {
    Elections elections;
    try {
      elections = (Elections) entry;
    } catch (ClassCastException | NullPointerException exception) {
      throw new IllegalArgumentException("entry must be of type Elections");
    }

    this.elections = elections;
  }

  /**
   * Sets Elections of this Tally.
   *
   * @param elections the Elections object that determines what Vote objects this Tally wil count
   */
  public final void setElections(Elections elections) {
    this.elections = elections;
  }

  /**
   * Casts each of the Entry objects in the passed list to Vote, then sets them as the Vote objects
   * to be tallied by this Tally.
   *
   * @param entries the list of Entry objects
   */
  public final void setVotes(List<Entry> entries) {
    ArrayList<Vote> newVotes = new ArrayList<>();
    for (Object entry : entries) {
      try {
        votes.add((Vote) entry);
      } catch (ClassCastException | NullPointerException exception) {
        throw new IllegalArgumentException("All elements of entries must be of type Vote");
      }
    }
    this.votes = newVotes;
  }

  /**
   * The unique identifier of this Tally is a 64-digit hash of its contents.
   *
   * @return the unique identifier of this Tally
   */
  @Override
  public final String getId() {
    return id;
  }

  /** Recalculates the unique identifier of this Tally. */
  private void updateId() {
    this.id =
        StringUtils.hashString(
            StringUtils.keyToString(teller)
                + elections.getId()
                + votes.toString()
                + getTimeStamp());
  }
}
