package org.votebloke.blockchain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

class VotesTellerTest {
  VotesTeller teller;
  Elections elections = new Elections(null, "test questions", new String[] {"answer1", "answer2"});

  ArrayList<Vote> votesArray = new ArrayList<>();
  Vote vote1 = new Vote(null, elections, "answer2");

  HashMap<String, String[]> tallyResultOneVote;

  @BeforeEach
  void setUp() {
    votesArray.add(vote1);
    teller = new VotesTeller(elections, votesArray);
    this.tallyResultOneVote = teller.tallyVotes();
  }

  @Test
  void constructorDoesNotThrow() {
    Assertions.assertDoesNotThrow(() -> new VotesTeller(elections, votesArray));
  }

  @Test
  void tallyVotesHasExpectedKeys() {
    Assertions.assertArrayEquals(
        new String[] {"question", "voteCounts", "answers"},
        tallyResultOneVote.keySet().toArray(String[]::new));
  }

  @Test
  void tallyVotesHasExpectedQuestion() {
    Assertions.assertArrayEquals(
        new String[] {elections.getQuestion()}, tallyResultOneVote.get("question"));
  }

  @Test
  void tallyVotesHasExpectedAnswer() {
    Assertions.assertArrayEquals(elections.getAnswers(), tallyResultOneVote.get("answers"));
  }

  @Test
  void tallyVotesVoteCountsHaveExpectedLength() {
    Assertions.assertEquals(
        elections.getAnswers().length, tallyResultOneVote.get("voteCounts").length);
  }

  @Test
  void tallyVotesVoteCountsLengthMatchesAnswersLength() {
    Assertions.assertEquals(
        tallyResultOneVote.get("answers").length, tallyResultOneVote.get("voteCounts").length);
  }

  @Test
  void tallyVotesVoteCountsHaveExpectedCounts() {
    Assertions.assertArrayEquals(new String[]{"0", "1"}, tallyResultOneVote.get("voteCounts"));
  }

  @Test
  void tallyVotesNoSuchElementExceptionWhenAnswerIsNotAllowed() {
    Vote badVote = new Vote(null, elections, "bad answer");
    votesArray.add(badVote);

    VotesTeller poorVotesTeller = new VotesTeller(elections, votesArray);
    Assertions.assertThrows(NoSuchElementException.class, () -> poorVotesTeller.tallyVotes());
  }
}
