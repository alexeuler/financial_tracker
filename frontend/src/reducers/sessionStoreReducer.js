import { merge } from 'ramda';

const SET_TOKEN = 'SESSION:SET_TOKEN';

export const initialState = {
  token: null,
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
