import { path } from 'ramda';

export const getExpenses = (state, userId) =>
  path(['expensesStore', userId], state);

export const getExpensesForm =
  path(['expensesStore', 'form']);

export const getExpensesErrors =
  path(['expensesStore', 'meta', 'errors']);

export const getExpensesLoading =
  path(['expensesStore', 'meta', 'loading']);
