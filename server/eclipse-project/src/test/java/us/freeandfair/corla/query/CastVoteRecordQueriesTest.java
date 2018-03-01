package us.freeandfair.corla.query;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import us.freeandfair.corla.model.CastVoteRecord;

/**
 * 
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class CastVoteRecordQueriesTest {

  /**
   * 
   * <description>
   * <explanation>
   */
  protected IDataSet getDataSet() throws DataSetException {
    final InputStream stream = this.getClass().getResourceAsStream("/us/freeandfair/corla/query/cast_vote_record.xml");
    
    return new FlatXmlDataSetBuilder().build(stream);
  }

  /**
   * 
   * <description>
   * <explanation>
   */
  protected DatabaseOperation getSetUpOperation() {
    return DatabaseOperation.REFRESH;
  }

  /**
   * 
   * <description>
   * <explanation>
   */
  protected DatabaseOperation getTearDownOperation() {
    return DatabaseOperation.NONE;
  }
  
  /**
   * 
   * <description>
   * <explanation>
   */
  @Ignore
  @Test
  public void testGetDemBallotTypes() {
   final List<CastVoteRecord> cvrs = CastVoteRecordQueries.get(1L, Arrays.asList(new String[]{"District Style 1"}));
    
   Assert.assertEquals(1, cvrs.size());
    
  }
  
}
