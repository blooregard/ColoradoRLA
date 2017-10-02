import * as React from 'react';

import { Popover, Position } from '@blueprintjs/core';
import { Link } from 'react-router-dom';

import * as config from 'corla/config';

import NavMenu from './NavMenu';

import resetDatabase from 'corla/action/dos/resetDatabase';
import logout from 'corla/action/logout';


const MenuButton = () =>
    <button className='pt-button pt-minimal pt-icon-menu' />;

const Heading = () =>
    <div className='pt-navbar-heading'>Colorado RLA</div>;

const Divider = () =>
    <span className='pt-navbar-divider' />;

const HomeButton = ({ path }: any) => (
    <Link to={ path }>
        <button className='pt-button pt-minimal pt-icon-home'>Home</button>
    </Link>
);

const UserButton = () =>
    <button className='pt-button pt-minimal pt-icon-user' />;

const SettingsButton = () =>
    <button className='pt-button pt-minimal pt-icon-cog' />;

const LogoutButton = ({ logout }: any) =>
    <button className='pt-button pt-minimal pt-icon-log-out' onClick={ logout } />;


const ResetDatabaseButton = ({ reset }: any) => (
    <button
        className='pt-button pt-intent-danger pt-icon-warning-sign'
        onClick={ reset }>
        DANGER: Reset Database
    </button>
);


export default function withNav(Menu: any, path: any): any {
    const resetSection = path === '/sos' && config.debug
                       ? <ResetDatabaseButton reset={ resetDatabase } />
                       : <div />;

    return () => (
        <nav className='pt-navbar'>
            <div className='pt-navbar-group pt-align-left'>
                <Popover content={ <Menu /> } position={ Position.RIGHT_TOP }>
                    <MenuButton />
                </Popover>
                <Heading />
            </div>
            <div className='pt-navbar-group pt-align-right'>
                { resetSection }
                <Divider />
                <HomeButton path={ path } />
                <Divider />
                <LogoutButton logout={ logout }/>
            </div>

        </nav>
    );
}
