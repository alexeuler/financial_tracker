import { combineReducers } from 'redux';

import loginStoreReducer, {
  initialState as loginStoreInitialState,
  actions as loginStoreActions,
} from './loginStoreReducer';

import signupStoreReducer, {
  initialState as signupStoreInitialState,
  actions as signupStoreActions,
} from './signupStoreReducer';

import sessionStoreReducer, {
  initialState as sessionStoreInitialState,
  actions as sessionStoreActions,
} from './sessionStoreReducer';

export default combineReducers({
  loginStore: loginStoreReducer,
  signupStore: signupStoreReducer,
  sessionStore: sessionStoreReducer,
});

export const initialState = ({
  loginStore: loginStoreInitialState,
  signupStore: signupStoreInitialState,
  sessionStore: sessionStoreInitialState,
});

export const actions = {
  ...loginStoreActions,
  ...signupStoreActions,
  ...sessionStoreActions,
};
