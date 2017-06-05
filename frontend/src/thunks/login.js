import { Validation } from '../utils';
import * as api from '../api';
import { actions as reduxActions } from '../reducers';
import { getSessionState } from '../selectors/session';

import { setToken } from './session';

const SERVER_FAILURE_MESSAGE = 'Could not connect to server. Please try again later.';

export const login = (payload, history) =>
  async function loginThunk(dispatch, getState) {
    const emailValidation = new Validation();
    emailValidation.email(payload.email);
    const passwordValidation = new Validation();
    passwordValidation.nonEmpty(payload.password);
    if (!emailValidation.isValid() || !passwordValidation.isValid()) {
      const errors = {
        email: emailValidation.errors,
        password: passwordValidation.errors,
      };
      return dispatch(reduxActions.setErrorsLoginForm(errors));
    }

    dispatch(reduxActions.setLoadingLoginForm(true));
    let response;
    try {
      response = await api.login(payload);
    } catch (e) {
      return dispatch(reduxActions.setErrorsLoginForm({ general: [SERVER_FAILURE_MESSAGE] }));
    } finally {
      dispatch(reduxActions.setLoadingLoginForm(false));
    }
    if (response.error) {
      switch (response.error.code) {
        case 300:
          return dispatch(reduxActions.setErrorsLoginForm({
            general: ['Invalid email and/or password.'],
          }));
        default:
          return dispatch(reduxActions.setErrorsLoginForm({
            general: [response.error.message],
          }));
      }
    }
    dispatch(setToken(response.result)).then(() => {
      const session = getSessionState(getState());
      history.push(`users/${session.id}/expenses`);
    });
    return dispatch(reduxActions.setErrorsLoginForm({}));
  };

export const updateLoginForm = reduxActions.updateLoginForm;
