import queryString from 'query-string';

import { Validation } from '../utils';
import * as api from '../api';
import { actions as reduxActions } from '../reducers';
import { getToken } from '../selectors/session';

const SERVER_FAILURE_MESSAGE = 'Could not connect to server. Please try again later.';

export const fetchExpenses = (userId, history) =>
  async function fetchExpensesThunk(dispatch, getState) {
    dispatch(reduxActions.setLoadingExpenses(true));
    let response;
    try {
      const token = getToken(getState());
      response = await api.fetchExpenses(token)(userId);
    } catch (e) {
      return dispatch(reduxActions.setErrorsExpenses({ general: [SERVER_FAILURE_MESSAGE] }));
    } finally {
      dispatch(reduxActions.setLoadingExpenses(false));
    }
    if (response.error) {
      switch (response.error.code) {
        case 300:
          return dispatch(reduxActions.setErrorsExpenses({
            general: ['You are unauthorized to view expenses for this user'],
          }));
        case 301: {
          const search = {
            message: 'Your session is outdated. Please relogin.',
          };
          const query = queryString.stringify(search);
          history.push(`/login?${query}`);
          return Promise.resolve(null);
        }
        default:
          return dispatch(reduxActions.setErrorsExpenses({
            general: [response.error.message],
          }));
      }
    }
    return dispatch(reduxActions.addExpenses(userId, response.result));
  };
