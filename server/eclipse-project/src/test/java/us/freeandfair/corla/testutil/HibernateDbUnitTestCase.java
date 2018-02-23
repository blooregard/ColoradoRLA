package us.freeandfair.corla.testutil;

import java.io.FileReader;
import java.util.Properties;

import org.apache.commons.lang3.NotImplementedException;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.freeandfair.corla.persistence.Persistence;

/**
 * Base DbUnit Test case that initialises test database.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public abstract class HibernateDbUnitTestCase extends DBTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateDbUnitTestCase.class);

    private static SessionFactory sessionFactory;
    protected Session session;

    /**
     * system properties initializing constructor.
     */
    public HibernateDbUnitTestCase() {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.hsqldb.jdbcDriver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:hsqldb:mem:corla;sql.syntax_pgs=true");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "sa");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "");
    }

    /**
     * Start the server.
     * @throws Exception in case of startup failure.
     */
    @Before
    public void setUp() throws Exception {
        HSQLServerUtil.getInstance().start("DBNAME");

        LOG.info("Loading hibernate...");
        if (sessionFactory == null) {
            sessionFactory = HibernateUtils.newSessionFactory("hibernate.test.cfg.xml");
        }

       // session = sessionFactory.openSession();

        super.setUp();
       
        final Properties p = new Properties();
        p.load(new FileReader(getClass().getResource("/default_test.properties").getFile()));

        Persistence.setProperties(p);
        session = Persistence.openSession();
    }

    /**
     * shutdown the server.
     * @throws Exception in case of errors.
     */
    @After
    public void tearDown() throws Exception {
        session.close();
        super.tearDown();
        HSQLServerUtil.getInstance().stop();
    }

    /** {@inheritDoc} */
    protected IDataSet getDataSet() throws Exception {
        throw new NotImplementedException("Specify data set for test: " + this.getClass().getSimpleName());
    }

    /** {@inheritDoc} */
    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.REFRESH;
    }

    /** {@inheritDoc} */
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.NONE;
    }


}