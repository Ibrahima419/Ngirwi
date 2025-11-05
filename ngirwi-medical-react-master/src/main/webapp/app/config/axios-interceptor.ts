import axios from 'axios';
import { Storage } from 'react-jhipster';

const TIMEOUT = 1 * 60 * 1000;
axios.defaults.timeout = TIMEOUT;
axios.defaults.baseURL = SERVER_API_URL;

const setupAxiosInterceptors = onUnauthenticated => {
  const onRequestSuccess = config => {
    const token = Storage.local.get('jhi-authenticationToken') || Storage.session.get('jhi-authenticationToken');
    console.log('[AXIOS] Request interceptor - URL:', config.url, 'Token found:', !!token);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
      console.log('[AXIOS] Authorization header set:', config.headers.Authorization?.substring(0, 20) + '...');
    } else {
      console.warn('[AXIOS] NO TOKEN FOUND - request will be unauthenticated!');
    }
    return config;
  };
  const onResponseSuccess = response => response;
  const onResponseError = err => {
    const status = err.status || (err.response ? err.response.status : 0);
    if (status === 403 || status === 401) {
      // Prevent redirect loop: don't trigger onUnauthenticated if:
      // 1. Already on login page
      // 2. Request is for /api/account (initial session check on app load)
      // 3. Request is for /api/authenticate (login attempt)
      const url = err.config?.url || '';
      const isOnLoginPage = window.location.pathname === '/login';
      const isAccountCheck = url.includes('/api/account');
      const isAuthenticateRequest = url.includes('/api/authenticate');

      if (!isOnLoginPage && !isAccountCheck && !isAuthenticateRequest) {
        onUnauthenticated();
      }
    }
    return Promise.reject(err);
  };
  axios.interceptors.request.use(onRequestSuccess);
  axios.interceptors.response.use(onResponseSuccess, onResponseError);
};

export default setupAxiosInterceptors;
