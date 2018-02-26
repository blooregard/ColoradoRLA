package us.freeandfair.corla.query;

import us.freeandfair.corla.model.CastVoteRecord;
import us.freeandfair.corla.model.CastVoteRecord.RecordType;
import us.freeandfair.corla.model.PartyBallotType;
import us.freeandfair.corla.model.PoliticalParty;
import us.freeandfair.corla.persistence.Persistence;
import us.freeandfair.corla.testutil.HibernateDbUnitTestCase;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.Assert;

public class CastVoteRecordQueriesTest extends HibernateDbUnitTestCase {

  protected IDataSet getDataSet() throws DataSetException {
    final InputStream stream = this.getClass().getResourceAsStream("/us/freeandfair/corla/query/cast_vote_record.xml");
    
    return new FlatXmlDataSetBuilder().build(stream);
  }

  protected DatabaseOperation getSetUpOperation() throws Exception {
    return DatabaseOperation.REFRESH;
  }

  protected DatabaseOperation getTearDownOperation() throws Exception {
    return DatabaseOperation.NONE;
  }
  
  @Test
  public void testGetDemBallotTypes() {
    List<CastVoteRecord> cvrs = CastVoteRecordQueries.get(1L, Arrays.asList(new String[]{"District Style 1"}));
    
   Assert.assertEquals(1, cvrs.size());
    
  }
  
}
