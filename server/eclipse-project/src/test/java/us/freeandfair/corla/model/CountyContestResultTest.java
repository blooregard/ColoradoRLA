/*
 * Free & Fair Colorado RLA System
 * 
 * @title colorado_rla
 * 
 * @created Feb 11, 2018
 * 
 * @copyright 2018 Free & Fair
 * 
 * @license GNU General Public License 3.0
 * 
 * @creator blooregard <3136181+blooregard@users.noreply.github.com>
 * 
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.model;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A test case for the CountyContestResultTest.
 * 
 * @author Ben Rector <ben.rector@sos.state.co.us>
 * @version 1.0.0
 */
@SuppressWarnings({"serial", "PMD.NonStaticInitializer", "PMD.AtLeastOneConstructor"})
public class CountyContestResultTest {

  /**
   * Sample set of vote totals for testing
   */
  private static final Map<String, Integer> VOTE_TOTALS;

  /**
   * Sample set for vote ranked order
   */
  private static final List<String> VOTE_RANKED_ORDER;

  /**
   * Test for only 1 winner
   */
  private static final Integer WINNERS_ALLOWED = 1;

  /**
   * Smallest margin test sample
   */
  private static final Integer SMALLEST_MARGIN = 4;

  /**
   * Number of ballots cast
   */
  private static final Integer BALLOTS_CAST = 20;

  /**
   * The object under test
   */
  private CountyContestResult result = new CountyContestResult();

  static {
    VOTE_TOTALS = new HashMap<String, Integer>() {
      {
        put("Choice 1", 3);
        put("Choice 2", 2);
        put("Choice 3", 5);
        put("Choice 4", 9);
        put("Choice 5", 1);
      }
    };

    VOTE_RANKED_ORDER = new ArrayList<String>() {
      {
        add("Choice 4");
        add("Choice 3");
        add("Choice 1");
        add("Choice 2");
        add("Choice 5");
      }
    };
  }

  /**
   * Populate a CountyContestResult to use for testing
   */
  public CountyContestResult createTestResult() throws NoSuchFieldException, SecurityException,
      IllegalArgumentException, IllegalAccessException {
    final CountyContestResult testResult = new CountyContestResult();

    final Field my_vote_totals = CountyContestResult.class.getDeclaredField("my_vote_totals");
    final Field my_winners_allowed =
        CountyContestResult.class.getDeclaredField("my_winners_allowed");
    final Field my_county_ballot_count =
        CountyContestResult.class.getDeclaredField("my_county_ballot_count");
    final Field my_min_margin =
        CountyContestResult.class.getDeclaredField("my_min_margin");
    final Field my_losers =
        CountyContestResult.class.getDeclaredField("my_losers");

    final boolean myVoteTotalAccessible = my_vote_totals.isAccessible();
    final boolean myWinnersAllowedAccessible = my_winners_allowed.isAccessible();
    final boolean myCountyBallotCount = my_county_ballot_count.isAccessible();
    final boolean myMinMarginAccessible = my_min_margin.isAccessible();
    final boolean myLosersAccessible = my_losers.isAccessible();

    my_vote_totals.setAccessible(true);
    my_vote_totals.set(testResult, VOTE_TOTALS);

    my_winners_allowed.setAccessible(true);
    my_winners_allowed.set(testResult, WINNERS_ALLOWED);

    my_county_ballot_count.setAccessible(true);
    my_county_ballot_count.set(testResult, BALLOTS_CAST);
    
    my_min_margin.setAccessible(true);
    my_min_margin.set(testResult, SMALLEST_MARGIN);
    
    my_losers.setAccessible(true);
    my_losers.set(testResult, new HashSet<String>(VOTE_RANKED_ORDER.subList(1, 5)));

    my_vote_totals.setAccessible(myVoteTotalAccessible);
    my_winners_allowed.setAccessible(myWinnersAllowedAccessible);
    my_county_ballot_count.setAccessible(myCountyBallotCount);
    my_min_margin.setAccessible(myMinMarginAccessible);
    my_losers.setAccessible(myLosersAccessible);
    
    return testResult;
  }

  /**
   * Use reflection to set internal state of the model
   * 
   * @throws SecurityException
   * @throws NoSuchFieldException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   */
  @Before
  public void oneTimeSetUp() throws NoSuchFieldException, SecurityException,
      IllegalArgumentException, IllegalAccessException {
    result = createTestResult();
  }

  /**
   * Clean up after tests
   */
  @After
  @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
  public void oneTimeTeardown() {

  }

  /**
   * A single test case that verifies the functionality.
   */
  @Test
  public void testRankedChoices() {
    final List<String> list = result.rankedChoices();
    assertEquals(list, VOTE_RANKED_ORDER);
  }

  /**
   * A unit test that verifies the side effects of the updateResults method This
   * unit test is the poster child of White Box testing and shows the need for a
   * refactoring of updateResults.
   */
  @Test
  public void testUpdateResults() {
    result.updateResults();
    assertEquals(result.winners(), new HashSet<String>(VOTE_RANKED_ORDER.subList(0, 1)));
    assertEquals(result.losers(), new HashSet<String>(VOTE_RANKED_ORDER.subList(1, 5)));
  }

  /**
   * A unit test to validate the diluted margin calculation
   */
  @Test
  public void testCountyDilutedMargin() {
    final BigDecimal margin = result.countyDilutedMargin();

    assertEquals(margin, new BigDecimal("0.2"));
  }

}
