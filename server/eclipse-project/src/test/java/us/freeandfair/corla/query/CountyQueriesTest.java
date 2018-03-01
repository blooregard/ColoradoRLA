
package us.freeandfair.corla.query;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Ignore;
import org.junit.Test;

import us.freeandfair.corla.model.County;

/**
 * 
 * @description <description>
 * @explanation <explanation>
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class CountyQueriesTest {

  /**
   * 
   * <description>
   * <explanation>
   */
  protected IDataSet getDataSet() throws DataSetException {
    final InputStream stream = this.getClass().getResourceAsStream("/us/freeandfair/corla/query/county.xml");
    
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
  @Test
  @Ignore
  public void testFromString() {
    final County c = CountyQueries.fromString("1");
    assertEquals("Lookup county by name or id", "Adams", c.name());
  }
}
