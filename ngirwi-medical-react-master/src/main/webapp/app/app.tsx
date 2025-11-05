import 'react-toastify/dist/ReactToastify.css';
import './app.scss';
import 'app/config/dayjs';

import SideBar from './shared/layout/sidebar/sidebar';

import React, { useEffect } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { getProfile } from 'app/shared/reducers/application-profile';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import ErrorBoundary from 'app/shared/error/error-boundary';
import { AUTHORITIES } from 'app/config/constants';
import AppRoutes from 'app/routes';
import Login from 'app/modules/login/login';

const baseHref = document.querySelector('base').getAttribute('href').replace(/\/$/, '');

export const App = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getSession());
    dispatch(getProfile());
  }, []);
  const account = useAppSelector(state => state.authentication.account);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const ribbonEnv = useAppSelector(state => state.applicationProfile.ribbonEnv);
  const isInProduction = useAppSelector(state => state.applicationProfile.inProduction);
  const isOpenAPIEnabled = useAppSelector(state => state.applicationProfile.isOpenAPIEnabled);

  return (
    <BrowserRouter basename={baseHref}>
      <div className="app-container">
        <ToastContainer position={toast.POSITION.TOP_LEFT} className="toastify-container" toastClassName="toastify-toast" />
        <ErrorBoundary>
          {account?.login ? (
            <SideBar
              isAuthenticated={isAuthenticated}
              isAdmin={isAdmin}
              ribbonEnv={ribbonEnv}
              isInProduction={isInProduction}
              isOpenAPIEnabled={isOpenAPIEnabled}
            />
          ) : null}

          <AppRoutes />
        </ErrorBoundary>

        {/* <ErrorBoundary> */}
        {/* </ErrorBoundary> */}
      </div>
    </BrowserRouter>
  );
};

export default App;
