import { merge } from 'ramda';
import { getToken } from '../utils';

const SET_TOKEN = 'SESSION:SET_TOKEN';

export const initialState = {
  token: getToken(),
};

const setToken = (state, action) => merge(state, { token: action.payload });

export default (state = initialState, action) => {
  switch (action.type) {
    case SET_TOKEN:
      return setToken(state, action);
    default:
      return state;
  }
};

const setTokenSession = payload => ({ type: SET_TOKEN, payload });

export const actions = {
  setTokenSession,
};
