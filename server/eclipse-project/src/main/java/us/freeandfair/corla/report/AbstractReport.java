/*
 * Free & Fair Colorado RLA System
 * 
 * @title colorado_rla
 * @created Mar 5, 2018
 * @copyright 2018 Free & Fair
 * @license GNU General Public License 3.0
 * @creator blooregard <ben.rector@gmail.com>
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.poi.ss.usermodel.Workbook;

import us.freeandfair.corla.model.CountyContestResult;
import us.freeandfair.corla.model.DoSDashboard;
import us.freeandfair.corla.model.ElectionType;
import us.freeandfair.corla.persistence.Persistence;

/**
 * 
 */
public abstract class AbstractReport {
  /**
   * The font size for Excel.
   */
  @SuppressWarnings("PMD.AvoidUsingShortType")
  public static final short FONT_SIZE = 12;
  
  /**
   * 
   */
  protected static final int PERCENTAGE = 100;
  
  /**
   * 
   */
  protected static final int TALL_BOX = 800;
  
  /**
   * The date formatter.
   */
  protected static final DateTimeFormatter DATE_FORMATTER = 
      DateTimeFormatter.ofPattern("MM/dd/yyyy");
  
  /**
   * The date/time formatter.
   */
  protected static final DateTimeFormatter DATE_TIME_FORMATTER = 
      DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
  
  /**
   * The DoS dashboard.
   */
  protected final DoSDashboard my_dosdb;
  
  /**
   * The election type
   */
  protected final ElectionType my_election_type;
  
  /**
   * The date and time this report was generated.
   */
  protected Instant my_timestamp;
 
  /**
   * Initialize a report object, timestamped at the current time.
   */
  public AbstractReport() {
    this(Instant.now());
  }
  
  /**
   * 
   * @param
   */
  public AbstractReport(final Instant the_timestamp) {
    my_timestamp = the_timestamp;
    my_dosdb = Persistence.getByID(DoSDashboard.ID, DoSDashboard.class);
    my_election_type = ElectionType.valueOf(my_dosdb.auditInfo().electionType());
  }
  
  /**
   * 
   */
  public abstract Workbook generateExcelWorkbook();
  
  /**
   * 
   */
  public abstract String filename();

  /**
   * @return the timestamp of this report.
   */
  public Instant timestamp() {
    return my_timestamp;
  }
  
  /**
   * @return the Excel representation of this report, as a byte array.
   * @exception IOException if the report cannot be generated.
   */
  public byte[] generateExcel() throws IOException {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final Workbook workbook = generateExcelWorkbook();
    workbook.write(baos);
    baos.flush();
    baos.close();
    workbook.close();
    return baos.toByteArray();
  }
  
  /**
   * @return the filename for the Excel version of this report.
   */
  public String filenameExcel() {
    // the file name should be constructed from the county name, election
    // type and date, and report generation time
    final LocalDateTime election_datetime = 
        LocalDateTime.ofInstant(my_dosdb.auditInfo().electionDate(), ZoneOffset.UTC);
    final LocalDateTime report_datetime = 
        LocalDateTime.ofInstant(my_timestamp, TimeZone.getDefault().toZoneId()).
        truncatedTo(ChronoUnit.SECONDS);
    final StringBuilder sb = new StringBuilder(32);
    
    sb.append(filename());
    sb.append('-');
    sb.append(my_dosdb.auditInfo().electionType().
              toLowerCase(Locale.getDefault()).replace(" ", "_"));
    sb.append('-');
    sb.append(DATE_FORMATTER.format(election_datetime).replace("/", "-"));
    sb.append("-report-");
    sb.append(DATE_TIME_FORMATTER.format(report_datetime).replace("/", "-").replace(":", "_"));
    sb.append(".xlsx");
    
    return sb.toString();
  }
  
  /**
   * 
   */
  protected BigDecimal getDilutedMargin(final CountyContestResult ccr, final String choice) {
    BigDecimal result;
    
    if (my_election_type == ElectionType.primary) {
      result = ccr.partyDilutedMarginToNearestLoser(choice);
    } else {
      result = ccr.countyDilutedMarginToNearestLoser(choice);
    }
    
    return result;
  }
  
  /**
   * @return the PDF representation of this report, as a byte array.
   */
  public byte[] generatePDF() {
    return new byte[0];
  }
  
  /**
   * @return the filename for the PDF version of this report.
   */
  public String filenamePDF() {
    return filenameExcel().replaceAll(".xlsx$", ".pdf");
  }
}
