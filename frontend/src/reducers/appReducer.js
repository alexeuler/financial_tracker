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

import expensesStoreReducer, {
  initialState as expensesStoreInitialState,
  actions as expensesStoreActions,
} from './expensesStoreReducer';

import usersStoreReducer, {
  initialState as usersStoreInitialState,
  actions as usersStoreActions,
} from './usersStoreReducer';


export default combineReducers({
  loginStore: loginStoreReducer,
  signupStore: signupStoreReducer,
  sessionStore: sessionStoreReducer,
  expensesStore: expensesStoreReducer,
  usersStore: usersStoreReducer,
});

export const initialState = ({
  loginStore: loginStoreInitialState,
  signupStore: signupStoreInitialState,
  sessionStore: sessionStoreInitialState,
  expensesStore: expensesStoreInitialState,
  usersStore: usersStoreInitialState,
});

export const actions = {
  ...loginStoreActions,
  ...signupStoreActions,
  ...sessionStoreActions,
  ...expensesStoreActions,
  ...usersStoreActions,
};
