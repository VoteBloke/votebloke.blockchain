package blockchain;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Represents a single vote in particular elections. */
public class Vote extends Entry {
  /** The public ECDSA key of the account which votes - the voter. */
  private final PublicKey voter;
  /** The id of Elections this Vote was cast in. */
  private Elections elections;
  /** The voter's answer. */
  private String answer;
  /** The voter's answer (as an index of the array of possible answer for the Elections). */
  private int answerInt;
  /** The id of this Vote. */
  private String id;

  /**
   * A constructor for Vote.
   *
   * @param voter the public ECDSA key identifying the agent who cast this Vote in elections
   * @param elections the elections this Vote was cast in
   * @param answerInt the number of an answer in elections
   */
  public Vote(PublicKey voter, Elections elections, int answerInt) {
    super();
    this.voter = voter;
    this.elections = elections;
    this.answerInt = answerInt;
    this.answer = null;
  }

  /**
   * A constructor for Vote.
   *
   * @param voter the public ECDSA key identifying the agent who cast this Vote in elections
   * @param elections the elections this Vote was cast in
   * @param answer the chosen answer to elections
   */
  public Vote(PublicKey voter, Elections elections, String answer) {
    super();
    this.voter = voter;
    this.elections = elections;
    this.answer = answer;
    answerInt = -1;
  }

  /**
   * A constructor for Vote.
   *
   * @param voter the public ECDSA key identifying the agent who cast this Vote in elections
   * @param elections the elections this Vote was cast in
   */
  public Vote(PublicKey voter, Elections elections) {
    this(voter, elections, null);
  }

  /**
   * A constructor for Vote.
   *
   * @param voter the public ECDSA key identifying the agent who cast this Vote in elections
   */
  public Vote(PublicKey voter) {
    this(voter, null, null);
  }

  /**
   * This method performs basic validation of `inputEntries` as well as the chosen answer to the
   * question in the Elections' object passed to this Vote. Also, it calculates the unique
   * identifier of this Vote. This method should be called before adding the Transaction that
   * carries it to a Block object.
   *
   * <p>The iterable passed to this method should be of length one and contain a single Elections
   * object corresponding to this Vote.
   *
   * @param inputEntries the iterable of Entry objects passed to this Vote
   * @throws IllegalArgumentException if either `inputEntries`, this Vote's answer are not valid
   * @return this Vote object
   */
  @Override
  public final List<TransactionOutput> processEntry(List<TransactionInput> inputEntries) throws IllegalArgumentException {
    if (inputEntries.size() != 1) {
      throw new IllegalArgumentException("inputEntries must be of length 1 in new Vote(...)");
    }

    if (inputEntries.get(0).transactionOut.data instanceof Elections) {
      this.elections = (Elections) inputEntries.get(0).transactionOut.data;
    } else {
      throw new IllegalArgumentException("Vote needs Elections to processEntry");
    }
    if (answerInt >= elections.getAnswers().length || answerInt < -1) {
      throw new IllegalArgumentException("answerInt was out of range of possible answers");
    }
    if (!Objects.equals(answer, null)) {
      boolean answerIsPossible = false;
      for (int i = 0; i < elections.getAnswers().length; i++) {
        if (elections.getAnswers()[i].equals(answer)) {
          answerIsPossible = true;
          this.answerInt = i;
          break;
        }
      }
      if (!answerIsPossible) {
        throw new IllegalArgumentException(
            "answer is not an element of possible Elections' answers");
      }
    } else {
      this.answer = elections.getAnswers()[answerInt];
    }

    id =
        StringUtils.hashString(
            StringUtils.keyToString(voter) + elections.getId() + answer + getTimeStamp());

    return new ArrayList<TransactionOutput>(

    );
  }

  public final List<TransactionOutput> processEntry(TransactionInput inputElections) {
    return processEntry(new ArrayList<>(List.of(inputElections)));
  }

  public final List<TransactionOutput> processEntry() {
    return (processEntry(new ArrayList<TransactionInput>(List.of(
            new TransactionInput(new TransactionOutput(elections.getElectionsCaller(), elections))))));
  }

  /**
   * Validates this Vote.
   *
   * @return true if this Vote's current id matches the recalculated id; false otherwise
   */
  @Override
  public boolean validateEntry() {
    return getId()
        .equals(
            StringUtils.hashString(
                StringUtils.keyToString(voter) + elections.getId() + answer + getTimeStamp()));
  }

  @Override
  public final String getId() {
    return id;
  }

  public final String getElectionsId() {
    return elections.getId();
  }

  public final String getAnswer() {
    return answer;
  }

  @Override
  public final String toString() {
    return "VoteId: " + getId() + "\nElections: " + getElectionsId() + "\nAnswer: " + getAnswer();
  }
}
