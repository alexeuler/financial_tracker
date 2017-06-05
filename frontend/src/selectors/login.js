import { path, pick, pipe } from 'ramda';

export const getLoginState = path(['loginStore']);
export const getLoginForm = pipe(
  getLoginState,
  pick(['email', 'password']),
);
export const getLoginErrors = pipe(
  getLoginState,
  path(['meta', 'errors']),
);

export const getLoginLoading = pipe(
  getLoginState,
  path(['meta', 'loading']),
);
