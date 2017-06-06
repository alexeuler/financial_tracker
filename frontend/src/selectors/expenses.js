import { path, find } from 'ramda';

export const getExpenses = (state, userId) =>
  path(['expensesStore', 'entities', userId], state);

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
