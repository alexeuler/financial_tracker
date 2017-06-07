export const getToken = () => window && window.localStorage && window.localStorage.getItem('token');
export const setToken = token => window && window.localStorage &&
  (token ? window.localStorage.setItem('token', token) : window.localStorage.removeItem('token'));

