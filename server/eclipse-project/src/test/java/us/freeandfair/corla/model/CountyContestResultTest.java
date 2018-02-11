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
import java.util.ArrayList;
import java.util.HashMap;
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
    final Field field = CountyContestResult.class.getDeclaredField("my_vote_totals");
    
    final boolean myVoteTotalAccessible = field.isAccessible();
    
    field.setAccessible(true);
    field.set(result, VOTE_TOTALS);
    
    field.setAccessible(myVoteTotalAccessible);
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
  @Test()
  public void testRankedChoices()  {
    final List<String> list = result.rankedChoices();
    Assert.assertEquals(list, VOTE_RANKED_ORDER);
  }

}
