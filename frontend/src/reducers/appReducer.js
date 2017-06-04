import { combineReducers } from 'redux';

import loginStoreReducer, {
  initialState as loginStoreInitialState,
  actions as loginStoreActions,
} from './loginStoreReducer';

import sessionStoreReducer, {
  initialState as sessionStoreInitialState,
  actions as sessionStoreActions,
} from './sessionStoreReducer';

export default combineReducers({
  loginStore: loginStoreReducer,
  sessionStore: sessionStoreReducer,
});

export const initialState = ({
  loginStore: loginStoreInitialState,
  sessionStore: sessionStoreInitialState,
});

export const actions = {
  ...loginStoreActions,
  ...sessionStoreActions,
};
