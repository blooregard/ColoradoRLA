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

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
  final private CountyContestResult result = new CountyContestResult();

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
   * Use reflection to set internal state of the model
   * @throws SecurityException 
   * @throws NoSuchFieldException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  @BeforeClass
  public void oneTimeSetUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    final Field my_vote_totals = CountyContestResult.class.getDeclaredField("my_vote_totals");
    final Field my_winners_allowed = CountyContestResult.class.getDeclaredField("my_winners_allowed");
    final Field my_county_ballot_count = CountyContestResult.class.getDeclaredField("my_county_ballot_count");
    
    final boolean myVoteTotalAccessible = my_vote_totals.isAccessible();
    final boolean myWinnersAllowedAccessible = my_winners_allowed.isAccessible();
    final boolean myCountyBallotCount = my_county_ballot_count.isAccessible();
    
    my_vote_totals.setAccessible(true);
    my_vote_totals.set(result, VOTE_TOTALS);
    
    my_winners_allowed.setAccessible(true);
    my_winners_allowed.set(result, WINNERS_ALLOWED);
    
    my_county_ballot_count.setAccessible(true);
    my_county_ballot_count.set(result, BALLOTS_CAST);
    
    my_vote_totals.setAccessible(myVoteTotalAccessible);
    my_winners_allowed.setAccessible(myWinnersAllowedAccessible);
    my_county_ballot_count.setAccessible(myCountyBallotCount);
  }
  
  /**
   * Clean up after tests
   */
  @AfterClass
  @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
  public void oneTimeTeardown() {
 
  }
  
  /**
   * A single test case that verifies the functionality.
   */
  @Test(priority = 1)
  public void testRankedChoices()  {
    final List<String> list = result.rankedChoices();
    Assert.assertEquals(list, VOTE_RANKED_ORDER);
  }
  
  /**
   * A unit test that verifies the side effects of the updateResults method
   * This unit test is the poster child of White Box testing and shows the
   * need for a refactoring of updateResults.
   */
  @Test(priority = 2)
  public void testUpdateResults() {
    result.updateResults();
    
    Assert.assertEquals(result.minMargin(), SMALLEST_MARGIN);
    Assert.assertEquals(result.winners(), VOTE_RANKED_ORDER.subList(0, 1));
    Assert.assertEquals(result.losers(), new HashSet<String>(VOTE_RANKED_ORDER.subList(1, 5)));
  }
  
  /**
   * A unit test to validate the diluted margin calculation
   */
  @Test(priority = 3)
  public void testCountyDilutedMargin() {
    final BigDecimal margin = result.countyDilutedMargin();
    
    Assert.assertEquals(margin, new BigDecimal("0.2"));
  }

}
