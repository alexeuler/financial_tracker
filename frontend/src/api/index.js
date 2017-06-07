import { map, over, lensPath, isNil } from 'ramda';

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
  if (authKey) options = { ...options, headers: { ...options.headers, Authorization: `Bearer ${authKey}` } };
  const response = await fetch(`${API_URL}${url}`, options);
  return response.json();
};

const getRequest = url => apiRequest(url, 'GET');
const postRequest = (url, payload) => apiRequest(url, 'POST', payload);
const patchRequest = (url, payload) => apiRequest(url, 'PATCH', payload);
const deleteRequest = url => apiRequest(url, 'DELETE');

export const login = payload => postRequest('/sessions/', payload)();
export const signup = payload => postRequest('/users/', payload)();

export const fetchUsers = token => getRequest('/users')(token);
export const createUser = token => payload => postRequest('/users', payload)(token);
export const updateUser = token => (userId, payload) => patchRequest(`/users/${userId}`, payload)(token);
export const deleteUser = token => userId => deleteRequest(`/users/${userId}`)(token);

const dollarsToCents = x => Math.round(x * 100);
const centsToDollars = x => x / 100;
const amountLens = lensPath(['amount']);
const resultLens = lensPath(['result']);
const convertExpenseToServer = over(amountLens, dollarsToCents);
const convertExpenseFromServer = over(amountLens, centsToDollars);
const convertExpenseResponse = response =>
  (isNil(response.result) ? response : over(resultLens, convertExpenseFromServer, response));
const convertExpensesResponse = response =>
  (isNil(response.result) ? response : over(resultLens, map(convertExpenseFromServer), response));

export const fetchExpenses = token => userId =>
  getRequest(`/users/${userId}/expenses`)(token).then(convertExpensesResponse);
export const createExpense = token => (userId, payload) =>
  postRequest(`/users/${userId}/expenses`, convertExpenseToServer(payload))(token).then(convertExpenseResponse);
export const updateExpense = token => (userId, expenseId, payload) =>
  patchRequest(`/users/${userId}/expenses/${expenseId}`, convertExpenseToServer(payload))(token).then(convertExpenseResponse);
export const deleteExpense = token => (userId, expenseId) => deleteRequest(`/users/${userId}/expenses/${expenseId}`)(token);
