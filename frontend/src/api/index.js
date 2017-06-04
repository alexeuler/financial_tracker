const API_URL = 'http://localhost:9000/api/v1';

const apiRequest = (url, method, payload) => async (authKey) => {
  let options = {
    method,
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
    },
  };
  if (payload) options = { ...options, body: JSON.stringify(payload) };
  if (authKey) options = { ...options, Authorization: `Bearer ${authKey}` };
  const response = await fetch(`${API_URL}${url}`, options);
  return response.json();
};

const getRequest = url => apiRequest(url, 'GET');
const postRequest = (url, payload) => apiRequest(url, 'POST', payload);

export const login = payload => postRequest('/sessions/', payload)();
export const signup = payload => postRequest('/users/', payload)();
