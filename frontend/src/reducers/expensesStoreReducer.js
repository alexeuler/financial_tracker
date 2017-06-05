import { lensPath, set, over, filter } from 'ramda';

const ADD = 'EXPENSES:ADD';
const UPDATE = 'EXPENSES:UPDATE';
const DELETE = 'EXPENSES:DELETE';
const RESET_FORM = 'EXPENSES:RESET_FORM';
const UPDATE_FORM = 'EXPENSES:UPDATE_FORM';
const SET_LOADING = 'EXPENSES:SET_LOADING';
const SET_ERRORS = 'EXPENSES:SET_ERRORS';

const initialForm = {
  occuredAt: null,
  amount: '',
  description: '',
  comment: '',
};

export const initialState = {
  meta: {
    loading: false,
    errors: {},
  },
  form: initialForm,
  entities: {},
};

const loadingLens = lensPath(['meta', 'loading']);
const errorsLens = lensPath(['meta', 'errors']);
const formLens = lensPath(['form']);
const expensesLens = userId => lensPath(['entities', userId]);
const expenseLens = (userId, expenseId) => lensPath(['entities', userId, expenseId]);

const add = (state, action) =>
  over(
    expensesLens(action.userId),
    expenses => [...(expenses || []), ...action.payload],
    state,
  );

const update = (state, action) =>
  set(expenseLens(action.userId, action.expenseId), action.payload, state);

const delete1 = (state, action) =>
  over(expensesLens(action.userId), filter(expense => (expense.id !== action.expenseId)), state);

const resetForm = state =>
  set(formLens, initialState, state);

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

const addExpenses = (userId, payload) => ({ type: ADD, userId, payload });
const updateExpenses = (userId, expenseId, payload) =>
  ({ type: UPDATE, userId, expenseId, payload });
const deleteExpenses = (userId, expenseId) => ({ type: DELETE, userId, expenseId });
const resetFormExpenses = () => ({ type: RESET_FORM });
const updateFormExpenses = payload => ({ type: UPDATE_FORM, payload });
const setLoadingExpenses = payload => ({ type: SET_LOADING, payload });
const setErrorsExpenses = payload => ({ type: SET_ERRORS, payload });

export const actions = {
  addExpenses,
  updateExpenses,
  deleteExpenses,
  resetFormExpenses,
  updateFormExpenses,
  setLoadingExpenses,
  setErrorsExpenses,
};
