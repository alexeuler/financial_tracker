import { lensPath, set, over, filter, update as rUpdate, findIndex, pipe } from 'ramda';

const ADD = 'USERS:ADD';
const UPDATE = 'USERS:UPDATE';
const DELETE = 'USERS:DELETE';
const SET_EDITING_FOCUS = 'USERS:SET_EDITING_FOCUS';
const RESET_FORM = 'USERS:RESET_FORM';
const UPDATE_FORM = 'USERS:UPDATE_FORM';
const SET_LOADING = 'USERS:SET_LOADING';
const SET_ERRORS = 'USERS:SET_ERRORS';

const initialForm = () => ({
  password: '',
  role: 'User',
});

export const initialState = {
  meta: {
    loading: false,
    errors: {},
  },
  editingFocus: null,
  form: initialForm(),
  entities: {},
};

const loadingLens = lensPath(['meta', 'loading']);
const errorsLens = lensPath(['meta', 'errors']);
const formLens = lensPath(['form']);
const editingFocusLens = lensPath(['editingFocus']);
const usersLens = lensPath(['entities']);

const add = (state, action) =>
  over(
    usersLens,
    users => [...(users || []), ...action.payload],
    state,
  );

const update = (state, action) =>
  over(usersLens, (users) => {
    const index = findIndex(expense => expense.id === action.expenseId, users);
    return rUpdate(index, action.payload, users);
  }, state);

const delete1 = (state, action) =>
  over(usersLens, filter(expense => (expense.id !== action.expenseId)), state);

const setEditingFocus = (state, action) =>
  set(editingFocusLens, action.payload, state);

const resetForm = pipe(
  set(formLens, initialForm()),
  set(errorsLens, {}),
);

const updateForm = (state, action) =>
  over(formLens, form => ({ ...form, ...action.payload }), state);

const setLoading = (state, action) => set(loadingLens, action.payload, state);
const setErrors = (state, action) => set(errorsLens, action.payload, state);

export default (state = initialState, action) => {
  switch (action.type) {
    case ADD:
      return add(state, action);
    case UPDATE:
      return update(state, action);
    case DELETE:
      return delete1(state, action);
    case SET_EDITING_FOCUS:
      return setEditingFocus(state, action);
    case RESET_FORM:
      return resetForm(state);
    case UPDATE_FORM:
      return updateForm(state, action);
    case SET_LOADING:
      return setLoading(state, action);
    case SET_ERRORS:
      return setErrors(state, action);
    default:
      return state;
  }
};

const addUsers = payload => ({ type: ADD, payload });
const updateUsers = (expenseId, payload) =>
  ({ type: UPDATE, expenseId, payload });
const deleteUsers = expenseId => ({ type: DELETE, expenseId });
const setEditingFocusUsers = expenseId => ({ type: SET_EDITING_FOCUS, payload: expenseId });
const resetFormUsers = () => ({ type: RESET_FORM });
const updateFormUsers = payload => ({ type: UPDATE_FORM, payload });
const setLoadingUsers = payload => ({ type: SET_LOADING, payload });
const setErrorsUsers = payload => ({ type: SET_ERRORS, payload });

export const actions = {
  addUsers,
  updateUsers,
  deleteUsers,
  setEditingFocusUsers,
  resetFormUsers,
  updateFormUsers,
  setLoadingUsers,
  setErrorsUsers,
};
