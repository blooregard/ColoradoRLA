/*
 * Free & Fair Colorado RLA System
 * 
 * @title ColoradoRLA
 * 
 * @created Jul 27, 2017
 * 
 * @copyright 2017 Colorado Department of State
 * 
 * @license SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * @creator Daniel M. Zimmerman <dmz@freeandfair.us>
 * 
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.endpoint;

import java.util.List;

import javax.persistence.PersistenceException;

import spark.Request;
import spark.Response;

import us.freeandfair.corla.Main;
import us.freeandfair.corla.json.CVRToAuditResponse;
import us.freeandfair.corla.model.County;

/**
 * The CVR to audit list endpoint.
 * 
 * @author Daniel M. Zimmerman <dmz@freeandfair.us>
 * @version 1.0.0
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class CVRToAuditList extends AbstractCVRToAudit {

  /**
   * {@inheritDoc}
   */
  @Override
  public String endpointName() {
    return "/cvr-to-audit-list";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("PMD.NPathComplexity")
  public String endpointBody(final Request the_request, final Response the_response) {
    // we know we have either state or county authentication; this will be null
    // for state authentication
    final County county = getAuthenticatedCounty(the_request, the_response);

    try {
      final List<CVRToAuditResponse> response_list =
          getListOfCVRs(county, the_request, the_response);
      okJSON(the_response, Main.GSON.toJson(response_list));
    } catch (final PersistenceException e) {
      serverError(the_response, "could not generate cvr list");
    }
    return my_endpoint_result.get();
  }
}
