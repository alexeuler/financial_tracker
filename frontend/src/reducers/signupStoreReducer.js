import { merge, lensPath, set } from 'ramda';

const UPDATE = 'SIGNUP:UPDATE';
const RESET = 'SIGNUP:RESET';
const SET_LOADING = 'SIGNUP:SET_LOADING';
const SET_ERRORS = 'SIGNUP:SET_ERRORS';

export const initialState = {
  meta: {
    loading: false,
    errors: {},
  },
  email: '',
  password: '',
  passwordConfirmation: '',
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

const updateSignupForm = payload => ({ type: UPDATE, payload });
const setLoadingSignupForm = payload => ({ type: SET_LOADING, payload });
const setErrorsSignupForm = payload => ({ type: SET_ERRORS, payload });
const resetSignupForm = () => ({ type: RESET });

export const actions = {
  updateSignupForm,
  setLoadingSignupForm,
  setErrorsSignupForm,
  resetSignupForm,
};
