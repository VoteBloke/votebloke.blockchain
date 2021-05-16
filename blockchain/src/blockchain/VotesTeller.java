package blockchain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import org.springframework.lang.NonNull;

/**
 * Performs the tallying of the votes.
 *
 * <p>Does not perform any validations.
 */
public class VotesTeller {
  /** The elections this VotesTeller tallies. */
  private final Elections elections;

  /** The votes this VotesTeller tallies. */
  private final ArrayList<Vote> votes;

  public VotesTeller(@NonNull Elections elections, @NonNull ArrayList<Vote> votes) {
    this.elections = elections;
    this.votes = votes;
  }

  /**
   * Returns a summary of the elections. Counts the votes.
   *
   * @return the hash map containing the summary
   */
  public HashMap<String, String[]> tallyVotes() {
    HashMap<String, String[]> electionsSummary = new HashMap<>();

    electionsSummary.put("question", new String[] {elections.getQuestion()});
    electionsSummary.put("answers", elections.getAnswers());
    int[] votesCounts = new int[elections.getAnswers().length];
    this.votes.forEach(
        vote -> votesCounts[findIndexOf(vote.getAnswer(), elections.getAnswers())]++);
    electionsSummary.put(
        "voteCounts", Arrays.stream(votesCounts).mapToObj(String::valueOf).toArray(String[]::new));

    return electionsSummary;
  }

  /**
   * Returns the index of the element in the array.
   *
   * @param value the value to look for in the array
   * @param array the searched array
   * @return the index of the value in the array
   * @throws NoSuchElementException when value is not found in the array
   */
  private int findIndexOf(@NonNull String value, @NonNull String[] array)
      throws NoSuchElementException {
    try {
      return IntStream.range(0, array.length)
          .filter(i -> array[i].equals(value))
          .findFirst()
          .getAsInt();
    } catch (NoSuchElementException e) {
      throw new NoSuchElementException(
          "The answer in a Vote object"
              + " did not match the array of answers in the Elections object.");
    }
  }
}
