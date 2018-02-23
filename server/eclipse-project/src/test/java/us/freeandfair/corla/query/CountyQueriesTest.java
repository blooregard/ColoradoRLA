
package us.freeandfair.corla.query;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import us.freeandfair.corla.testutil.HibernateDbUnitTestCase;

import us.freeandfair.corla.model.County;
import us.freeandfair.corla.persistence.Persistence;

public class CountyQueriesTest extends HibernateDbUnitTestCase {
  
  public CountyQueriesTest() throws ClassNotFoundException {
  }

  @BeforeClass
  public void before() throws FileNotFoundException, IOException {

  }

  @AfterClass
  public void after() {

  }

  protected IDataSet getDataSet() throws DataSetException {
    final InputStream stream = this.getClass().getResourceAsStream("/us/freeandfair/corla/query/county.xml");
    
    return new FlatXmlDataSetBuilder().build(stream);
  }

  protected DatabaseOperation getSetUpOperation() throws Exception {
    return DatabaseOperation.REFRESH;
  }

  protected DatabaseOperation getTearDownOperation() throws Exception {
    return DatabaseOperation.NONE;
  }

  @Test
  public void testFromString() {
    final County c = CountyQueries.fromString("1");
    assertEquals("Lookup county by name or id", "Adams", c.name());
  }
}
