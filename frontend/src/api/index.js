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
const patchRequest = (url, payload) => apiRequest(url, 'PATCH', payload);
const deleteRequest = url => apiRequest(url, 'DELETE');

export const login = payload => postRequest('/sessions/', payload)();
export const signup = payload => postRequest('/users/', payload)();
export const fetchExpenses = token => userId => getRequest(`/users/${userId}/expenses/`)(token);
export const createExpense = token => (userId, payload) => postRequest(`/users/${userId}/expenses/`, payload)(token);
export const updateExpense = token => (expenseId, userId, payload) => patchRequest(`/users/${userId}/expenses/${expenseId}`, payload)(token);
export const deleteExpense = token => (expenseId, userId) => deleteRequest(`/users/${userId}/expenses/${expenseId}`)(token);

