package us.freeandfair.corla.model;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collections;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test cases for the CountyContestComparisonAudit class
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class CountyContestComparisonAuditTest {
  /**
   * Expected number of samples to audit
   */
  private static final int SAMPLES_TO_AUDIT = 24;

  /**
   * Risk limit to use for testing
   */
  private final static BigDecimal riskLimit = new BigDecimal("0.10");
  
  /**
   * The object of the tests
   */
  private CountyContestComparisonAudit audit;
  
  /**
   * A test contest
   */  
 private final Contest contest = new Contest("Contest A", null, null, Collections.emptyList(), 0, 0, 0);
  
 /**
   * Test setup method
   */
  @BeforeClass
  public void beforeClass() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    CountyContestResult result;
    try {
      result = new CountyContestResultTest().createTestResult();
      result.updateResults();
     
      audit = new CountyContestComparisonAudit(null, result, riskLimit, AuditReason.COUNTY_WIDE_CONTEST);
    
      final Field my_contest = CountyContestComparisonAudit.class.getDeclaredField("my_contest");
      
      final boolean myContestAccessible = my_contest.isAccessible();
      
      my_contest.setAccessible(true);
      my_contest.set(audit, contest);
        
      my_contest.setAccessible(myContestAccessible);
      
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
        | IllegalAccessException e) {
      assert false;
    }
    
  }

  /**
   * Run through the audit and verify that the sampling number is correct
   */
  @Test
  public void initialSamplesToAudit() {
    Assert.assertEquals(audit.initialSamplesToAudit(), SAMPLES_TO_AUDIT);
  }
}
