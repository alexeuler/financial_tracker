import * as api from '../api';
import { actions as reduxActions } from '../reducers';

const SERVER_FAILURE_MESSAGE = 'Could not connect to server. Please try again later.';

export const login = payload =>
  async function loginThunk(dispatch) {
    dispatch(reduxActions.setLoadingLoginForm(true));
    let response;
    try {
      response = await api.login(payload);
    } catch (e) {
      return dispatch(reduxActions.setErrorsLoginForm({ general: SERVER_FAILURE_MESSAGE }));
    } finally {
      dispatch(reduxActions.setLoadingLoginForm(false));
    }
    if (response.error) {
      switch (response.error.code) {
        case 300:
          return dispatch(reduxActions.setErrorsLoginForm({
            general: 'Invalid email and/or password.',
          }));
        default:
          return dispatch(reduxActions.setErrorsLoginForm({
            general: response.error.message,
          }));
      }
    }
    return dispatch(reduxActions.setTokenSession(response.result));
  };

export const signup = payload =>
  async function signupThunk(dispatch) {
    dispatch(reduxActions.setLoadingSignupForm(true));
    let response;
    try {
      response = await api.signup(payload);
    } catch (e) {
      return dispatch(reduxActions.setErrorsSignupForm({ general: SERVER_FAILURE_MESSAGE }));
    } finally {
      dispatch(reduxActions.setLoadingSignupForm(false));
    }
    if (response.error) {
      switch (response.error.code) {
        default:
          return dispatch(reduxActions.setErrorsLoginForm({
            general: response.error.message,
          }));
      }
    }
    return null;
    // return dispatch(reduxActions.setTokenSession(response.result));
  };

export const updateLoginForm = reduxActions.updateLoginForm;
export const updateSignupForm = reduxActions.updateSignupForm;
