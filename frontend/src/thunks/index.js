import * as api from '../api';
import { actions as reduxActions } from '../reducers';

export const login = payload =>
  async function loginThunk(dispatch) {
    dispatch(reduxActions.setLoadingLoginForm(true));
    let response;
    try {
      response = await api.login(payload);
    } catch (e) {
      return dispatch(reduxActions.setErrorsLoginForm({ general: 'Could not connect to server' }));
    } finally {
      dispatch(reduxActions.setLoadingLoginForm(false));
    }
    return dispatch(reduxActions.setToken(response.result));
  };

export const updateLoginForm = reduxActions.updateLoginForm;
