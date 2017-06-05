import { lensPath, set, over, filter } from 'ramda';

const ADD = 'EXPENSES:ADD';
const UPDATE = 'EXPENSES:UPDATE';
const DELETE = 'EXPENSES:DELETE';
const SET_LOADING = 'EXPENSES:SET_LOADING';
const SET_ERRORS = 'EXPENSES:SET_ERRORS';

export const initialState = {
  meta: {
    loading: false,
    errors: {},
  },
  entities: {},
};

const loadingLens = lensPath(['meta', 'loading']);
const errorsLens = lensPath(['meta', 'errors']);
const expensesLens = userId => lensPath(['entities', userId]);
const expenseLens = (userId, expenseId) => lensPath(['entities', userId, expenseId]);

const add = (state, action) =>
  over(expensesLens(action.userId), users => ({ ...users, ...action.payload }), state);

const update = (state, action) =>
  set(expenseLens(action.userId, action.expenseId), action.payload, state);

const delete1 = (state, action) =>
  over(expensesLens(action.userId), filter(expense => (expense.id !== action.expenseId)), state);

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
    case SET_LOADING:
      return setLoading(state, action);
    case SET_ERRORS:
      return setErrors(state, action);
    default:
      return state;
  }
};

const addExpenses = (userId, payload) => ({ type: ADD, userId, payload });
const setLoadingExpenses = payload => ({ type: SET_LOADING, payload });
const setErrorsExpenses = payload => ({ type: SET_ERRORS, payload });

export const actions = {
  addExpenses,
  setLoadingExpenses,
  setErrorsExpenses,
};
