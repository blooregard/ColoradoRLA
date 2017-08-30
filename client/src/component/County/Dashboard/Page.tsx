import * as React from 'react';
import { Link } from 'react-router-dom';

import * as _ from 'lodash';

import CountyNav from '../Nav';

import FileUploadContainer from './FileUploadContainer';

import fetchReport from 'corla/action/county/fetchReport';


const AuditBoardInfo = ({ signedIn }: any) => {
    const icon = signedIn
               ? <span className='pt-icon pt-intent-success pt-icon-tick-circle' />
               : <span className='pt-icon pt-intent-danger pt-icon-error' />;

    const text = signedIn ? 'signed in' : 'not signed in';

    return (
        <div className='pt-card'>
            <span>{ icon } </span>
            Audit board is <strong>{ text }.</strong>
        </div>
    );
};

const Main = (props: any) => {
    const {
        auditBoardSignedIn,
        auditButtonDisabled,
        auditComplete,
        boardSignIn,
        canRenderReport,
        name,
        signInButtonDisabled,
        startAudit,
    } = props;

    let directions = 'Please upload your Ballot Manifest and Cast Vote Records.';

    if (auditBoardSignedIn) {
        if (auditButtonDisabled) {
            directions = 'Please stand by for the state to begin the audit.';
        } else {
            directions = 'You may proceed with the audit.';
        }
    } else {
        if (!signInButtonDisabled) {
            directions = 'Please have the audit board sign in.';
        }
    }

    if (auditComplete) {
        directions = 'The audit is complete.';
    }

    return (
        <div className='county-main pt-card'>
            <h1>Hello, { name } County!</h1>
            <div>
                <div className='pt-card'>{ directions }</div>
                <FileUploadContainer />
                <AuditBoardInfo signedIn={ auditBoardSignedIn } />
                <div className='pt-card'>
                    <div>Click to download intermediate audit report.</div>
                    <button
                        className='pt-button'
                        disabled={ !canRenderReport }
                        onClick={ fetchReport }>
                        Download
                    </button>
                </div>
                <button
                    className='pt-button pt-intent-primary'
                    disabled={ signInButtonDisabled }
                    onClick={ boardSignIn }>
                    <span className='pt-icon-standard pt-icon-people' />
                    <span> </span>
                    Audit Board Sign-In
                </button>
                <button
                    className='pt-button pt-intent-primary'
                    disabled={ auditButtonDisabled }
                    onClick={ startAudit }>
                    <span className='pt-icon-standard pt-icon-eye-open' />
                    <span> </span>
                    Start Audit
                </button>
            </div>
        </div>
    );
};

const ContestInfoTableRow = ({ choice }: any) => (
    <tr>
        <td>{ choice.name }</td>
        <td>{ choice.description }</td>
    </tr>
);

const ContestInfoTable = ({ contest }: any) => {
    const body = _.map(contest.choices, (c: any) => {
        return <ContestInfoTableRow key={ c.name } choice={ c } />;
    });

    return (
        <div className='pt-card'>
            <span>{ contest.name }</span>
            <table className='pt-table'>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                    </tr>
                </thead>
                <tbody>
                    { body }
                </tbody>
            </table>
        </div>
    );
};

const ContestInfo = ({ contests }: any): any => {
    const contestTables = _.map(contests, (c: any) => {
        if (!c) {
            return <div />;
        }

        return <ContestInfoTable key={ c.name } contest={ c } />;
    });

    return (
        <div className='contest-info pt-card'>
            <h3>Contest info</h3>
            <div>
                { contestTables }
            </div>
        </div>
    );
};

const CountyInfo = ({ county, info }: any) => {
    const { ballotsToAudit } = county;
    const unauditedBallotCount = ballotsToAudit ? ballotsToAudit.length : '';

    const rows = [
        ['County:', info.name],
        ['Status:', county.status],
        ['# Ballots to audit:', unauditedBallotCount],
        ['# Ballots audited:', county.auditedBallotCount],
        ['# Disagreements:', county.disagreementCount],
        ['# Discrepancies:', county.discrepancyCount],

    ].map(([k, v]: any) => (
        <tr key={ k }>
            <td><strong>{ k }</strong></td>
            <td>{ v }</td>
        </tr>
    ));

    return (
        <div className='county-info pt-card'>
            <h3>County info</h3>
            <div className='pt-card'>
                <table className='pt-table pt-condensed'>
                    <tbody>{ rows }</tbody>
                </table>
            </div>
        </div>
    );
};

const Info = ({ info, contests, county }: any) => (
    <div className='info pt-card'>
        <CountyInfo county={ county } info={ info } />
        <ContestInfo contests={ contests } />
    </div>
);

const CountyDashboardPage = (props: any) => {
    const {
        allRoundsComplete,
        auditBoardSignedIn,
        auditComplete,
        boardSignIn,
        canAudit,
        canRenderReport,
        canSignIn,
        contests,
        county,
        countyInfo,
        countyDashboardRefresh,
        finishAudit,
        startAudit,
    } = props;
    const {
        ballots,
        ballotManifestHash,
        cvrExportHash,
        startTimestamp,
        status,
    } = county;

    const info = { auditDate: startTimestamp };

    const auditButtonDisabled = !canAudit || auditComplete;
    const signInButtonDisabled = !canSignIn;

    return (
        <div className='county-root'>
            <CountyNav />
            <div>
                <Main auditComplete={ auditComplete }
                      auditBoardSignedIn={ auditBoardSignedIn }
                      boardSignIn={ boardSignIn }
                      canRenderReport={ canRenderReport }
                      auditButtonDisabled={ auditButtonDisabled }
                      name={ countyInfo.name }
                      signInButtonDisabled={ signInButtonDisabled }
                      startAudit={ startAudit } />
                <Info info={ countyInfo }
                      contests={ contests }
                      county={ county } />
            </div>
        </div>
    );
};

export default CountyDashboardPage;