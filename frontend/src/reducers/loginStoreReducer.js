import { merge, lensPath, set } from 'ramda';

const UPDATE = 'LOGIN:UPDATE';
const RESET = 'LOGIN:RESET';
const SET_LOADING = 'LOGIN:SET_LOADING';
const SET_ERRORS = 'LOGIN:SET_ERRORS';

export const initialState = {
  meta: {
    loading: false,
    errors: {},
  },
  email: '',
  password: '',
};

const loadingLens = lensPath(['meta', 'loading']);
const errorsLens = lensPath(['meta', 'errors']);

const update = (state, action) => merge(state, action.payload);
const setLoading = (state, action) => set(loadingLens, action.payload, state);
const setErrors = (state, action) => set(errorsLens, action.payload, state);

export default (state = initialState, action) => {
  switch (action.type) {
    case UPDATE:
      return update(state, action);
    case SET_LOADING:
      return setLoading(state, action);
    case SET_ERRORS:
      return setErrors(state, action);
    case RESET: return initialState;
    default:
      return state;
  }
};

const updateLoginForm = payload => ({ type: UPDATE, payload });
const setLoadingLoginForm = payload => ({ type: SET_LOADING, payload });
const setErrorsLoginForm = payload => ({ type: SET_ERRORS, payload });
const resetLoginForm = () => ({ type: RESET });

export const actions = {
  updateLoginForm,
  setLoadingLoginForm,
  setErrorsLoginForm,
  resetLoginForm,
};
