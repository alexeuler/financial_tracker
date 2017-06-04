import { getToken, getDataFromJWT } from '../utils';

const SET_TOKEN = 'SESSION:SET_TOKEN';

const getStateFromToken = (token) => {
  let data = null;
  try {
    data = getDataFromJWT(token);
  } catch (e) {
    return { token };
  }
  return { token, ...data };
};

export const initialState = getStateFromToken(getToken());

const setToken = (state, action) => ({ ...state, ...getStateFromToken(action.payload) });

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
