/*
 * Free & Fair Colorado RLA System
 * 
 * @title ColoradoRLA
 * @created Jul 27, 2017
 * @copyright 2017 Colorado Department of State
 * @license SPDX-License-Identifier: AGPL-3.0-or-later
 * @creator Daniel M. Zimmerman <dmz@freeandfair.us>
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.endpoint;

import static us.freeandfair.corla.util.PrettyPrinter.booleanYesNo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;

import javax.persistence.PersistenceException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.cxf.attachment.Rfc5987Util;

import spark.Request;
import spark.Response;

import us.freeandfair.corla.json.CVRToAuditResponse;
import us.freeandfair.corla.model.County;
import us.freeandfair.corla.model.CountyDashboard;
import us.freeandfair.corla.persistence.Persistence;
import us.freeandfair.corla.util.SparkHelper;

/**
 * The CVR to audit download endpoint.
 * 
 * @author Daniel M. Zimmerman <dmz@freeandfair.us>
 * @version 1.0.0
 */
@SuppressWarnings({"PMD.AtLeastOneConstructor"})
public class CVRToAuditDownload extends AbstractCVRToAudit {

  /**
   * The CSV headers for formatting the response.
   */
  private static final String[] CSV_HEADERS = {
      "scanner_id", "batch_id", "record_id", "imprinted_id", "ballot_type",
      "storage_location", "cvr_number", "audited", "party"
  };
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String endpointName() {
    return "/cvr-to-audit-download";
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String endpointBody(final Request the_request, final Response the_response) {

    final County county = getAuthenticatedCounty(the_request, the_response);
    
    try {
      final List<CVRToAuditResponse> response_list = getListOfCVRs(county, the_request, the_response);
      
      //
      final int index = getIndex(the_request);
      final CountyDashboard cdb = Persistence.getByID(county.id(), CountyDashboard.class);
      final int ballot_count = getCount(the_request);
      
      // compute the round, if any
      final OptionalInt round = getRound(county, cdb, the_request, the_response);
      
      // generate a CSV file from the response list
      the_response.type("text/csv");
      
      // the file name should be constructed from the county name and round
      // or start/count
      final StringBuilder sb = new StringBuilder(32);
      sb.append("ballot-list-");
      sb.append(county.name().toLowerCase(Locale.getDefault()).replace(" ", "_"));
      sb.append('-');
      if (round.isPresent()) {
        sb.append("round-");
        sb.append(round.getAsInt());
      } else {
        sb.append("start-");
        sb.append(index);
        sb.append("-count-");
        sb.append(ballot_count);
      }
      sb.append(".csv");
      
      try {
        the_response.raw().setHeader("Content-Disposition", "attachment; filename=\"" + 
                                     Rfc5987Util.encode(sb.toString(), "UTF-8") + "\"");
      } catch (final UnsupportedEncodingException e) {
        serverError(the_response, "UTF-8 is unsupported (this should never happen)");
      }
      
      try (OutputStream os = SparkHelper.getRaw(the_response).getOutputStream();
           BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
        writeCSV(response_list, bw);
        ok(the_response);
      } catch (final IOException e) {
        serverError(the_response, "Unable to stream response");
      }
    } catch (final PersistenceException e) {
      serverError(the_response, "could not generate cvr list");
    }
    return my_endpoint_result.get();
  }
  
  /**
   * Writes the specified list of CVRToAuditResponse objects as CSV.
   * 
   * @param the_cvrs The list of objects.
   * @param the_writer The writer to write to.
   * @exception IOException if there is a problem writing the CSV file.
   */
  private void writeCSV(final List<CVRToAuditResponse> the_cvrs, final Writer the_writer) 
      throws IOException {
    try (CSVPrinter csvp = new CSVPrinter(the_writer, 
                                          CSVFormat.DEFAULT.withHeader(CSV_HEADERS).
                                          withQuoteMode(QuoteMode.NON_NUMERIC))) {
      for (final CVRToAuditResponse cvr : the_cvrs) {
        csvp.printRecord(cvr.scannerID(), cvr.batchID(), cvr.recordID(), cvr.imprintedID(),
                         cvr.ballotType(), cvr.storageLocation(), cvr.cvrNumber(), 
                         booleanYesNo(cvr.audited()), cvr.party().prettyString());
      }
    } 
  }
}
