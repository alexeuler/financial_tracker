import { path, pick, pipe } from 'ramda';

export const getLoginState = path(['loginStore']);
export const getLoginForm = pipe(
  getLoginState,
  pick(['login', 'password']),
);
export const getLoginErrors = pipe(
  getLoginState,
  path(['meta', 'errors']),
);
