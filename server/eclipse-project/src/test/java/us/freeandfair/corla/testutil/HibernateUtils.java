package us.freeandfair.corla.testutil;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class that provides basics hibernate initialization.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */

public final class HibernateUtils {

    private static final Logger LOG = Logger.getLogger(HibernateUtils.class);

    /**
     * @param pathToHibernateCfgXml path to where hibernate.test.cfg.xml is located
     *        if file is in /my/dir/with/my.cfg.xml, where name of config file is my.cfg.xml
     *        then all all path with files name must be provided as parameter.
     * @return Hibernate session factory.
     */
    public static SessionFactory newSessionFactory(final String pathToHibernateCfgXml) {
        LOG.info("Loading Hibernate Session Factory with configurations from file "
                + pathToHibernateCfgXml + "...");
        Configuration hibernateConfiguration = new Configuration();
        hibernateConfiguration.configure(pathToHibernateCfgXml);
        return hibernateConfiguration.buildSessionFactory();
    }

}
