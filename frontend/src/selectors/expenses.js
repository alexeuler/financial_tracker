import { path, find, pipe, sortBy, prop, reverse, filter, T, head, last, groupBy, reduce } from 'ramda';
import moment from 'moment';

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

const weekBounds = (mDate) => {
  const weekStart = moment(mDate).set({ day: 0, hour: 0, minute: 0, second: 0, millisecond: 0 });
  const weekEnd = moment(mDate).set({ day: 6, hour: 23, minute: 59, second: 59, millisecond: 999 });
  return { weekStart, weekEnd };
};

export const getReport = (state, userId) => {
  const format = 'YYYY-MM-DD HH:mm';
  const dateFormat = 'YYYY-MM-DD';
  const expenses = pipe(
    getRawExpenses(userId),
    exp => sortBy(prop('occuredAt'))(exp || []),
  )(state);
  const minDateExp = head(expenses);
  const maxDateExp = last(expenses);
  if (!minDateExp || !maxDateExp) return { entries: [], sum: null, avg: null };
  const minDate = moment(minDateExp.occuredAt, format);
  const maxDate = moment(maxDateExp.occuredAt, format);

  const start = moment(minDate);
  const index = {};
  const weeks = [];
  while (weekBounds(start).weekEnd <= weekBounds(maxDate).weekEnd) {
    const bounds = weekBounds(start);
    weeks.push(bounds);
    index[bounds.weekStart.format(dateFormat)] = weeks.length - 1;
    start.add(7, 'days');
  }

  const expensesGroups = groupBy(
    expense => index[weekBounds(expense.occuredAt).weekStart.format(dateFormat)],
    expenses,
  );

  const entries = weeks.map((entity, i) => {
    const sum = reduce((acc, elem) => acc + elem.amount, 0)(expensesGroups[i]);
    const avg = sum / 7;
    return {
      start: entity.weekStart.format(dateFormat),
      end: entity.weekEnd.format(dateFormat),
      sum,
      avg,
    };
  });

  const periodStart = moment(minDate).set({ hours: 0, minutes: 0, seconds: 0, milliseconds: 0 });
  const periodEnd = moment(maxDate).set({ hours: 0, minutes: 0, seconds: 0, milliseconds: 0 });
  const totalDays = moment.duration((periodEnd - periodStart)).asDays() + 1;
  const totalSum = reduce((acc, elem) => acc + elem.amount, 0)(expenses);
  return { entries, sum: totalSum, avg: totalSum / totalDays };
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
