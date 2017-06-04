import { setToken as localStorageSetToken } from '../utils';
import { actions as reduxActions } from '../reducers';

export const setToken = payload =>
  async function setTokenThunk(dispatch) {
    dispatch(reduxActions.setTokenSession(payload));
    localStorageSetToken(payload);
    return Promise.resolve(null);
  };
