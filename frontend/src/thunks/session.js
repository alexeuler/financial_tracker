import { setToken as localStorageSetToken } from '../utils';
import { actions as reduxActions } from '../reducers';

export const setToken = payload =>
  async function setTokenThunk(dispatch) {
    dispatch(reduxActions.setTokenSession(payload));
    localStorageSetToken(payload);
    return Promise.resolve(null);
  };

export const resetSession = history =>
  async function resetSessionThunk(dispatch) {
    dispatch(reduxActions.resetSession());
    localStorageSetToken(null);
    history.push('/login');
    return Promise.resolve(null);
  };
