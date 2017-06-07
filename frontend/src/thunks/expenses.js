import queryString from 'query-string';
import { pick, map, pipe, isEmpty } from 'ramda';

import { Validation } from '../utils';
import * as api from '../api';
import { actions as reduxActions } from '../reducers';
import { getToken } from '../selectors/session';
import { getExpensesForm, getExpense } from '../selectors/expenses';

const SERVER_FAILURE_MESSAGE = 'Could not connect to server. Please try again later.';

const validateForm = (form) => {
  const amountValidation = new Validation(form.amount);
  amountValidation.number();
  const descValidation = new Validation(form.description);
  descValidation.nonEmpty();
  const dateValidation = new Validation(form.occuredAt);
  dateValidation.nonEmpty();
  const errors = {};
  if (!amountValidation.isValid()) errors.amount = amountValidation.errors;
  if (!descValidation.isValid()) errors.description = descValidation.errors;
  if (!dateValidation.isValid()) errors.occuredAt = dateValidation.errors;
  return errors;
};

export const fetchExpenses = (userId, history) =>
  async function fetchExpensesThunk(dispatch, getState) {
    dispatch(reduxActions.setLoadingExpenses(true));
    let response;
    try {
      const token = getToken(getState());
      response = await api.fetchExpenses(token)(userId);
    } catch (e) {
      return dispatch(reduxActions.setErrorsExpenses({ general: [SERVER_FAILURE_MESSAGE] }));
    } finally {
      dispatch(reduxActions.setLoadingExpenses(false));
    }
    if (response.error) {
      switch (response.error.code) {
        case 300:
          return dispatch(reduxActions.setErrorsExpenses({
            general: ['You are unauthorized to view expenses for this user'],
          }));
        case 301: {
          const search = {
            message: 'Your session is outdated. Please relogin.',
          };
          const query = queryString.stringify(search);
          history.push(`/login?${query}`);
          return Promise.resolve(null);
        }
        default:
          return dispatch(reduxActions.setErrorsExpenses({
            general: [response.error.message],
          }));
      }
    }
    return dispatch(reduxActions.addExpenses(userId, response.result));
  };

export const createExpense = (userId, history) =>
  async function createExpenseThunk(dispatch, getState) {
    const state = getState();
    const token = getToken(state);
    const form = getExpensesForm(state);
    const errors = validateForm(form);
    if (!isEmpty(errors)) {
      return dispatch(reduxActions.setErrorsExpenses(errors));
    }

    dispatch(reduxActions.setLoadingExpenses(true));
    let response;
    try {
      response = await api.createExpense(token)(userId, form);
    } catch (e) {
      return dispatch(reduxActions.setErrorsExpenses({ general: [SERVER_FAILURE_MESSAGE] }));
    } finally {
      dispatch(reduxActions.setLoadingExpenses(false));
    }
    if (response.error) {
      switch (response.error.code) {
        case 300:
          return dispatch(reduxActions.setErrorsExpenses({
            general: ['You are unauthorized to create expenses for this user'],
          }));
        case 301: {
          const search = {
            message: 'Your session is outdated. Please relogin.',
          };
          const query = queryString.stringify(search);
          history.push(`/login?${query}`);
          return Promise.resolve(null);
        }
        default:
          return dispatch(reduxActions.setErrorsExpenses({
            general: [response.error.message],
          }));
      }
    }
    dispatch(reduxActions.addExpenses(userId, [response.result]));
    return dispatch(reduxActions.resetFormExpenses());
  };

export const updateExpense = (userId, expenseId, history) =>
  async function updateExpenseThunk(dispatch, getState) {
    const state = getState();
    const token = getToken(state);
    const form = getExpensesForm(state);
    const errors = validateForm(form);
    if (!isEmpty(errors)) {
      return dispatch(reduxActions.setErrorsExpenses(errors));
    }

    dispatch(reduxActions.setLoadingExpenses(true));
    let response;
    try {
      response = await api.updateExpense(token)(userId, expenseId, form);
    } catch (e) {
      return dispatch(reduxActions.setErrorsExpenses({ general: [SERVER_FAILURE_MESSAGE] }));
    } finally {
      dispatch(reduxActions.setLoadingExpenses(false));
    }
    if (response.error) {
      switch (response.error.code) {
        case 300:
          return dispatch(reduxActions.setErrorsExpenses({
            general: ['You are unauthorized to update expenses for this user'],
          }));
        case 301: {
          const search = {
            message: 'Your session is outdated. Please relogin.',
          };
          const query = queryString.stringify(search);
          history.push(`/login?${query}`);
          return Promise.resolve(null);
        }
        default:
          return dispatch(reduxActions.setErrorsExpenses({
            general: [response.error.message],
          }));
      }
    }
    return dispatch(reduxActions.updateExpenses(userId, expenseId, response.result));
  };

export const deleteExpense = (userId, expenseId, history) =>
  async function deleteExpenseThunk(dispatch, getState) {
    dispatch(reduxActions.setLoadingExpenses(true));
    let response;
    try {
      const state = getState();
      const token = getToken(state);
      response = await api.deleteExpense(token)(userId, expenseId);
    } catch (e) {
      return dispatch(reduxActions.setErrorsExpenses({ general: [SERVER_FAILURE_MESSAGE] }));
    } finally {
      dispatch(reduxActions.setLoadingExpenses(false));
    }
    if (response.error) {
      switch (response.error.code) {
        case 300:
          return dispatch(reduxActions.setErrorsExpenses({
            general: ['You are unauthorized to delete expenses for this user'],
          }));
        case 301: {
          const search = {
            message: 'Your session is outdated. Please relogin.',
          };
          const query = queryString.stringify(search);
          history.push(`/login?${query}`);
          return Promise.resolve(null);
        }
        default:
          return dispatch(reduxActions.setErrorsExpenses({
            general: [response.error.message],
          }));
      }
    }
    return dispatch(reduxActions.deleteExpenses(userId, expenseId));
  };


export const setEditingFocusExpense = (userId, expenseId) =>
  async function setEditingFocusExpenseThunk(dispatch, getState) {
    await dispatch(reduxActions.setEditingFocusExpenses(expenseId));
    if (!expenseId) return dispatch(reduxActions.resetFormExpenses());
    const expense = getExpense(getState(), userId, expenseId);
    const form = pipe(
      pick(['occuredAt', 'amount', 'comment', 'description']),
      map(x => x.toString()),
    )(expense);
    return dispatch(reduxActions.updateFormExpenses(form));
  };


export const updateFormExpenses = reduxActions.updateFormExpenses;
export const updateFiltersExpenses = reduxActions.updateFiltersExpenses;
export const resetFiltersExpenses = reduxActions.resetFiltersExpenses;
