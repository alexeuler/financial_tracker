import { path, find, pipe, sortBy, prop, reverse, filter, T } from 'ramda';

export const getFilters =
  path(['expensesStore', 'filters']);

const getRawExpenses = userId => state => path(['expensesStore', 'entities', userId])(state) || [];

const addFilter = (leftFilter, rightFilter) => x => leftFilter(x) && rightFilter(x);

export const getExpenses = (state, userId) => {
  const filters = getFilters(state);
  let predicate = T;
  if (!isNaN(parseInt(filters.amountFrom, 0))) {
    predicate = addFilter(predicate, x => x.amount >= parseInt(filters.amountFrom, 0));
  }
  if (!isNaN(parseInt(filters.amountTo, 0))) {
    predicate = addFilter(predicate, x => x.amount <= parseInt(filters.amountTo, 0));
  }
  if (filters.text) {
    predicate = addFilter(predicate,
      x => (
        (x.description.toLowerCase().indexOf(filters.text.toLowerCase()) >= 0) ||
        (x.comment && (x.comment.toLowerCase().indexOf(filters.text.toLowerCase()) >= 0))),
    );
  }
  if (filters.dateFrom) predicate = addFilter(predicate, x => x.occuredAt >= filters.dateFrom);
  if (filters.dateTo) predicate = addFilter(predicate, x => x.occuredAt <= filters.dateTo);
  // const exp = getRawExpenses(userId)(state);
  // const f = filter(predicate);
  return pipe(
    getRawExpenses(userId),
    filter(predicate),
    expenses => sortBy(prop('occuredAt'))(expenses || []),
    reverse,
  )(state);
};

export const getExpense = (state, userId, expenseId) => {
  const expenses = getExpenses(state, userId);
  return find(expense => expense.id === expenseId, expenses);
};

export const getReport = state => {
  return {};
}

export const getEditingFocus = path(['expensesStore', 'editingFocus']);

export const getExpensesForm =
  path(['expensesStore', 'form']);

export const getExpensesEditingFocus =
  path(['expensesStore', 'editingFocus']);

export const getExpensesErrors =
  path(['expensesStore', 'meta', 'errors']);

export const getExpensesLoading =
  path(['expensesStore', 'meta', 'loading']);
