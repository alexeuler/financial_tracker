import { path, find, pipe, sortBy, prop, reverse } from 'ramda';

export const getExpenses = (state, userId) =>
  pipe(
    path(['expensesStore', 'entities', userId]),
    expenses => sortBy(prop('occuredAt'))(expenses || []),
    reverse,
  )(state);

export const getExpense = (state, userId, expenseId) => {
  const expenses = getExpenses(state, userId);
  return find(expense => expense.id === expenseId, expenses);
};

export const getEditingFocus = path(['expensesStore', 'editingFocus']);

export const getExpensesForm =
  path(['expensesStore', 'form']);

export const getExpensesEditingFocus =
  path(['expensesStore', 'editingFocus']);

export const getExpensesErrors =
  path(['expensesStore', 'meta', 'errors']);

export const getExpensesLoading =
  path(['expensesStore', 'meta', 'loading']);
