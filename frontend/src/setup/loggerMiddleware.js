/* eslint-disable no-console */
import { toUpper } from 'ramda';

export default function logger({ getState }) {
  return next => (action) => {
    if (typeof action === 'function') {
      console.info(`Executing thunk %c${toUpper(action.name)}`, 'color: #0C0CD6');
      return next(action);
    }
    console.info('Executing redux action', action);
    const result = next(action);
    const state = getState();
    console.info('State after dispatch', state);
    return result;
  };
}
