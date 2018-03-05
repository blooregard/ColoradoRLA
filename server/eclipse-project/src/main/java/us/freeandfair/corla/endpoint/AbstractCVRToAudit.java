/*
 * Free & Fair Colorado RLA System
 * 
 * @title colorado_rla
 * 
 * @created Mar 5, 2018
 * 
 * @copyright 2018 Free & Fair
 * 
 * @license GNU General Public License 3.0
 * 
 * @creator blooregard <ben.rector@gmail.com>
 * 
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import spark.Request;
import spark.Response;

import us.freeandfair.corla.Main;
import us.freeandfair.corla.controller.ComparisonAuditController;
import us.freeandfair.corla.json.CVRToAuditResponse;
import us.freeandfair.corla.json.CVRToAuditResponse.BallotOrderComparator;
import us.freeandfair.corla.model.CastVoteRecord;
import us.freeandfair.corla.model.County;
import us.freeandfair.corla.model.CountyDashboard;
import us.freeandfair.corla.model.PartyBallotType;
import us.freeandfair.corla.model.PoliticalParty;
import us.freeandfair.corla.persistence.Persistence;
import us.freeandfair.corla.query.BallotManifestInfoQueries;
import us.freeandfair.corla.query.PartyBallotTypeQueries;

/**
 * The CVR to audit abstract endpoint.
 * 
 * @author Ben Rector <ben.rector@sos.state.co.us>
 * @version 1.0.0
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public abstract class AbstractCVRToAudit extends AbstractEndpoint {
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
   * The "include audited" parameter.
   */
  public static final String INCLUDE_AUDITED = "include_audited";

  /**
   * The "round" parameter.
   */
  public static final String ROUND = "round";

  /**
   * The "county" parameter.
   */
  public static final String COUNTY = "county";

  /**
   * {@inheritDoc}
   */
  @Override
  public EndpointType endpointType() {
    return EndpointType.GET;
  }

  /**
   * This endpoint requires any kind of authentication.
   */
  @Override
  public AuthorizationType requiredAuthorization() {
    return AuthorizationType.EITHER;
  }

  /**
   * Validate the request parameters. In this case, the two parameters must
   * exist and both be non-negative integers.
   * 
   * @param the_request The request.
   */
  @Override
  protected boolean validateParameters(final Request the_request) {
    final String start = the_request.queryParams(START);
    final String ballot_count = the_request.queryParams(BALLOT_COUNT);
    final String round = the_request.queryParams(ROUND);
    final String county = the_request.queryParams(COUNTY);

    boolean result = start != null && ballot_count != null || round != null;

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
          result &= r > 0;
        }

        if (county == null && Main.authentication().authenticatedCounty(the_request) == null) {
          // it's a DoS user, but they didn't specify a county
          result = false;
        } else if (county != null) {
          Long.parseLong(county);
        }
      } catch (final NumberFormatException e) {
        result = false;
      }
    }

    return result;
  }

  /**
   * Verify authentication
   */
  protected County getAuthenticatedCounty(final Request the_request,
                                          final Response the_response) {
    // we know we have either state or county authentication; this will be null
    // for state authentication
    County county = Main.authentication().authenticatedCounty(the_request);

    if (county == null) {
      county =
          Persistence.getByID(Long.parseLong(the_request.queryParams(COUNTY)), County.class);
      if (county == null) {
        badDataContents(the_response,
                        "county " + the_request.queryParams(COUNTY) + " does not exist");
      }
      assert county != null; // makes FindBugs happy
    }

    return county;
  }

  /**
   * 
   */
  protected int getIndex(final Request the_request) {
    final String start_param = the_request.queryParams(START);

    int index = 0;
    if (start_param != null) {
      index = Integer.parseInt(start_param);
    }

    return index;
  }

  /**
   * 
   */
  protected int getCount(final Request the_request) {
    final String ballot_count_param = the_request.queryParams(BALLOT_COUNT);

    int ballot_count = 0;
    if (ballot_count_param != null) {
      ballot_count = Integer.parseInt(ballot_count_param);
    }

    return ballot_count;
  }

  /**
   * 
   */
  protected OptionalInt getRound(final County county, final CountyDashboard cdb,
                                 final Request the_request, final Response the_response) {
    // compute the round, if any
    OptionalInt round = OptionalInt.empty();
    final String round_param = the_request.queryParams(ROUND);

    if (round_param != null) {
      final int round_number = Integer.parseInt(round_param);
      if (0 < round_number && round_number <= cdb.rounds().size()) {
        round = OptionalInt.of(round_number);
      } else {
        badDataContents(the_response, "cvr list requested for invalid round " + round_param +
                                      " for county " + cdb.id());
      }
    }

    return round;
  }

  /**
   * 
   */
  protected List<CVRToAuditResponse> getListOfCVRs(final County county,
                                                   final Request the_request,
                                                   final Response the_response) {

    // get the request parameters
    final String duplicates_param = the_request.queryParams(INCLUDE_DUPLICATES);
    final String audited_param = the_request.queryParams(INCLUDE_AUDITED);

    final int ballot_count = getCount(the_request);
    final int index = getIndex(the_request);

    final boolean duplicates;
    if (duplicates_param == null) {
      duplicates = false;
    } else {
      duplicates = true;
    }
    final boolean audited;
    if (audited_param == null) {
      audited = false;
    } else {
      audited = true;
    }
    // get other things we need
    final CountyDashboard cdb = Persistence.getByID(county.id(), CountyDashboard.class);
    final List<CastVoteRecord> cvr_to_audit_list;
    final List<CVRToAuditResponse> response_list = new ArrayList<>();

    // compute the round, if any
    final OptionalInt round = getRound(county, cdb, the_request, the_response);

    if (round.isPresent()) {
      cvr_to_audit_list =
          ComparisonAuditController.ballotsToAudit(cdb, round.getAsInt(), audited);
    } else {
      cvr_to_audit_list = ComparisonAuditController
          .computeBallotOrder(cdb, index, ballot_count, duplicates, audited);
    }

    final Map<String, PoliticalParty> ballotTypeCache = getBallotTypeByPoliticalParty(county);
    for (int i = 0; i < cvr_to_audit_list.size(); i++) {
      final CastVoteRecord cvr = cvr_to_audit_list.get(i);
      final String location = BallotManifestInfoQueries.locationFor(cvr);
      response_list
          .add(new CVRToAuditResponse(i, cvr.scannerID(), cvr.batchID(), cvr.recordID(),
                                      cvr.imprintedID(), cvr.cvrNumber(), cvr.id(),
                                      cvr.ballotType(), location, cvr.auditFlag(),
                                      ballotTypeCache.get(cvr.ballotType())));
    }
    response_list.sort(new BallotOrderComparator());

    return response_list;
  }

  /**
   * 
   */
  private Map<String, PoliticalParty> getBallotTypeByPoliticalParty(final County county) {
    final Set<PartyBallotType> types = PartyBallotTypeQueries.assemble(county);
    return types.stream().collect(Collectors.toMap(PartyBallotType::ballotType,
                                                   PartyBallotType::politicalParty));
  }
}
