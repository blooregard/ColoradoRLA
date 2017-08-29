import * as _ from 'lodash';

import * as cookies from 'js-cookie';

import counties from '../data/counties';


type AuditStage
    = 'PRE_AUDIT'
    | 'AUDIT_READY_TO_START'
    | 'AUDIT_ONGOING'
    | 'AUDIT_COMPLETE'
    | 'AUDIT_RESULTS_PUBLISHED';

const sosInitialState = (): any => ({
    asm: {},
    auditStage: 'PRE_AUDIT',
    auditedContests: {},
    countyStatus: {},
});


export default (state: any) => ({
    ...state,
    dashboard: 'sos',
    loggedIn: true,
    session: cookies.get('JSESSIONID'),
    sos: sosInitialState(),
});
