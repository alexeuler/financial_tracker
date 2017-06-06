import queryString from 'query-string';
import { pick, map, pipe, isEmpty } from 'ramda';

import { Validation } from '../utils';
import * as api from '../api';
import { actions as reduxActions } from '../reducers';
import { getToken } from '../selectors/session';
import { getUsersForm, getUser } from '../selectors/users';

const SERVER_FAILURE_MESSAGE = 'Could not connect to server. Please try again later.';

const validateForm = (form) => {
  const passwordValidation = new Validation(form.amount);
  passwordValidation.length(3);
  const roleValidation = new Validation(form.description);
  roleValidation.oneOf(['User', 'Manager', 'Admin']);
  const errors = {};
  if (!passwordValidation.isValid()) errors.password = passwordValidation.errors;
  if (!roleValidation.isValid()) errors.role = roleValidation.errors;
  return errors;
};

export const fetchUsers = (history) =>
  async function fetchUsersThunk(dispatch, getState) {
    dispatch(reduxActions.setLoadingUsers(true));
    let response;
    try {
      const token = getToken(getState());
      response = await api.fetchUsers(token);
    } catch (e) {
      return dispatch(reduxActions.setErrorsUsers({ general: [SERVER_FAILURE_MESSAGE] }));
    } finally {
      dispatch(reduxActions.setLoadingUsers(false));
    }
    if (response.error) {
      switch (response.error.code) {
        case 300:
          return dispatch(reduxActions.setErrorsUsers({
            general: ['You are unauthorized to view users'],
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
          return dispatch(reduxActions.setErrorsUsers({
            general: [response.error.message],
          }));
      }
    }
    return dispatch(reduxActions.addUsers(response.result));
  };

export const createUser = (history) =>
  async function createUserThunk(dispatch, getState) {
    const state = getState();
    const token = getToken(state);
    const form = getUsersForm(state);
    const errors = validateForm(form);
    if (!isEmpty(errors)) {
      return dispatch(reduxActions.setErrorsUsers(errors));
    }
    dispatch(reduxActions.setLoadingUsers(true));
    let response;
    try {
      response = await api.createUser(token)(form);
    } catch (e) {
      return dispatch(reduxActions.setErrorsUsers({ general: [SERVER_FAILURE_MESSAGE] }));
    } finally {
      dispatch(reduxActions.setLoadingUsers(false));
    }
    if (response.error) {
      switch (response.error.code) {
        case 300:
          return dispatch(reduxActions.setErrorsUsers({
            general: ['You are unauthorized to create users'],
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
          return dispatch(reduxActions.setErrorsUsers({
            general: [response.error.message],
          }));
      }
    }
    dispatch(reduxActions.addUsers([response.result]));
    return dispatch(reduxActions.resetFormUsers());
  };

export const updateUser = (history) =>
  async function updateUserThunk(dispatch, getState) {
    dispatch(reduxActions.setLoadingUsers(true));
    let response;
    try {
      const state = getState();
      const token = getToken(state);
      const form = getUsersForm(state);
      response = await api.updateUser(token)(userId, form);
    } catch (e) {
      return dispatch(reduxActions.setErrorsUsers({ general: [SERVER_FAILURE_MESSAGE] }));
    } finally {
      dispatch(reduxActions.setLoadingUsers(false));
    }
    if (response.error) {
      switch (response.error.code) {
        case 300:
          return dispatch(reduxActions.setErrorsUsers({
            general: ['You are unauthorized to create users'],
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
          return dispatch(reduxActions.setErrorsUsers({
            general: [response.error.message],
          }));
      }
    }
    return dispatch(reduxActions.updateUsers(userId, response.result));
  };

export const deleteUser = (userId, history) =>
  async function deleteUserThunk(dispatch, getState) {
    dispatch(reduxActions.setLoadingUsers(true));
    let response;
    try {
      const state = getState();
      const token = getToken(state);
      response = await api.deleteUser(token)(userId);
    } catch (e) {
      return dispatch(reduxActions.setErrorsUsers({ general: [SERVER_FAILURE_MESSAGE] }));
    } finally {
      dispatch(reduxActions.setLoadingUsers(false));
    }
    if (response.error) {
      switch (response.error.code) {
        case 300:
          return dispatch(reduxActions.setErrorsUsers({
            general: ['You are unauthorized to create users'],
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
          return dispatch(reduxActions.setErrorsUsers({
            general: [response.error.message],
          }));
      }
    }
    return dispatch(reduxActions.deleteUsers(userId));
  };


export const setEditingFocusUser = userId =>
  async function setEditingFocusUserThunk(dispatch, getState) {
    await dispatch(reduxActions.setEditingFocusUsers(userId));
    if (!userId) return dispatch(reduxActions.resetFormUsers());
    const user = getUser(getState(), userId);
    const form = pick(['password', 'role'])(user);
    return dispatch(reduxActions.updateFormUsers(form));
  };

export const updateFormUsers = reduxActions.updateFormUsers;
