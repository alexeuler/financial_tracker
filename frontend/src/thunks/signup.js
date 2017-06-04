import queryString from 'query-string';

import { Validation } from '../utils';
import * as api from '../api';
import { actions as reduxActions } from '../reducers';

import { login } from './login';

const SERVER_FAILURE_MESSAGE = 'Could not connect to server. Please try again later.';

export const signup = (payload, history) =>
  async function signupThunk(dispatch) {
    const emailValidation = new Validation();
    emailValidation.email(payload.email);

    const passwordValidation = new Validation();
    passwordValidation.length(payload.password, 3);
    passwordValidation.equalPasswords(payload.password, payload.passwordConfirmation);

    if (!emailValidation.isValid() || !passwordValidation.isValid()) {
      const errors = {
        email: emailValidation.errors,
        password: passwordValidation.errors,
      };
      return dispatch(reduxActions.setErrorsSignupForm(errors));
    }

    dispatch(reduxActions.setLoadingSignupForm(true));
    let response;
    try {
      response = await api.signup(payload);
    } catch (e) {
      return dispatch(reduxActions.setErrorsSignupForm({ general: [SERVER_FAILURE_MESSAGE] }));
    } finally {
      dispatch(reduxActions.setLoadingSignupForm(false));
    }
    if (response.error) {
      switch (response.error.code) {
        default:
          return dispatch(reduxActions.setErrorsSignupForm({
            general: response.error.message,
          }));
      }
    }
    dispatch(reduxActions.resetSignupForm());
    // Try to autologin, if fails (should never fail actually) send to login page
    dispatch(login({ email: payload.email, password: payload.password })).then(() =>
      history.push('/expenses'),
    ).catch(() => {
      const search = {
        message: 'Your account was successfully created. Please use your credentials to login.',
      };
      const query = queryString.stringify(search);
      history.push(`/login?${query}`);
    });
    return null;
  };

export const updateSignupForm = reduxActions.updateSignupForm;
