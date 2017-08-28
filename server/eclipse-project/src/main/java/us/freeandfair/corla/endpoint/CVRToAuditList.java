/*
 * Free & Fair Colorado RLA System
 * 
 * @title ColoradoRLA
 * @created Jul 27, 2017
 * @copyright 2017 Free & Fair
 * @license GNU General Public License 3.0
 * @author Daniel M. Zimmerman <dmz@freeandfair.us>
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.endpoint;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import spark.Request;
import spark.Response;

import us.freeandfair.corla.Main;
import us.freeandfair.corla.controller.ComparisonAuditController;
import us.freeandfair.corla.json.CVRToAuditResponse;
import us.freeandfair.corla.json.CVRToAuditResponse.BallotOrderComparator;
import us.freeandfair.corla.model.CastVoteRecord;
import us.freeandfair.corla.model.County;
import us.freeandfair.corla.model.CountyDashboard;
import us.freeandfair.corla.model.Round;
import us.freeandfair.corla.persistence.Persistence;
import us.freeandfair.corla.query.BallotManifestInfoQueries;

/**
 * The CVR to audit list endpoint.
 * 
 * @author Daniel M. Zimmerman
 * @version 0.0.1
 */
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.CyclomaticComplexity",
    "PMD.ModifiedCyclomaticComplexity", "PMD.StdCyclomaticComplexity"})
public class CVRToAuditList extends AbstractEndpoint {
  /**
   * The "start" parameter.
   */
  public static final String START = "start";
  
  /**
   * The "ballot_count" parameter.
   */
  public static final String BALLOT_COUNT = "ballot_count";
  
  /**
   * The "include duplicates" parameter.
   */
  public static final String INCLUDE_DUPLICATES = "include_duplicates";
  
  /**
   * The "round" parameter.
   */
  public static final String ROUND = "round";
  
  /**
   * {@inheritDoc}
   */
  @Override
  public EndpointType endpointType() {
    return EndpointType.GET;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String endpointName() {
    return "/cvr-to-audit-list";
  }
  
  /**
   * This endpoint requires any kind of authentication.
   */
  @Override
  public AuthorizationType requiredAuthorization() {
    return AuthorizationType.COUNTY;
  }

  /**
   * Validate the request parameters. In this case, the two parameters
   * must exist and both be non-negative integers.
   * 
   * @param the_request The request.
   */
  @Override
  protected boolean validateParameters(final Request the_request) {
    final String start = the_request.queryParams(START);
    final String ballot_count = the_request.queryParams(BALLOT_COUNT);
    final String round = the_request.queryParams(ROUND);
    
    boolean result = start != null && ballot_count != null ||
                     round != null;
    
    if (result) {
      try {
        if (start != null) {
          final int s = Integer.parseInt(start);
          result &= s >= 0;
          final int b = Integer.parseInt(ballot_count);
          result &= b >= 0;
        }
        
        if (round != null) {
          final int r = Integer.parseInt(round);
          result &= r >= 0;
        }
      } catch (final NumberFormatException e) {
        result = false;
      }
    }
    
    return result;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("PMD.NPathComplexity")
  public String endpoint(final Request the_request, final Response the_response) {
    try {
      // get the request parameters
      final String start_param = the_request.queryParams(START);
      final String ballot_count_param = the_request.queryParams(BALLOT_COUNT);
      final String duplicates_param = the_request.queryParams(INCLUDE_DUPLICATES);
      final String round_param = the_request.queryParams(ROUND);

      int ballot_count = 0;
      if (ballot_count_param != null) {
        ballot_count = Integer.parseInt(ballot_count_param);
      }
      int index = 0;
      if (start_param != null) {
        index = Integer.parseInt(start_param);
      }
      boolean duplicates;
      if (duplicates_param == null) {
        duplicates = false;
      } else {
        duplicates = true;
      }
      
      // get other things we need
      final County county = Authentication.authenticatedCounty(the_request);
      final CountyDashboard cdb = Persistence.getByID(county.id(), CountyDashboard.class);
      final List<CastVoteRecord> cvr_to_audit_list = 
          ComparisonAuditController.computeBallotOrder(cdb, index, ballot_count, duplicates);
      final List<CVRToAuditResponse> response_list = new ArrayList<>();
      
      for (int i = 0; i < cvr_to_audit_list.size(); i++) {
        final CastVoteRecord cvr = cvr_to_audit_list.get(i);
        final String location = BallotManifestInfoQueries.locationFor(cvr);
        response_list.add(new CVRToAuditResponse(i, cvr.scannerID(), 
                                                 cvr.batchID(), cvr.recordID(), 
                                                 cvr.imprintedID(), 
                                                 cvr.cvrNumber(), cvr.id(),
                                                 cvr.ballotType(), location));
      }
      response_list.sort(new BallotOrderComparator());
      okJSON(the_response, Main.GSON.toJson(response_list));
    } catch (final PersistenceException e) {
      serverError(the_response, "could not generate cvr list");
    }
    return my_endpoint_result.get();
  }
}
