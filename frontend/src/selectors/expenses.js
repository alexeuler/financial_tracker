import { path } from 'ramda';

export const getExpenses = (state, userId) =>
  path(['expensesStore', userId], state);
