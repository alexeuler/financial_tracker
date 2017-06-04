import { path, pick, pipe } from 'ramda';

export const getSignupState = path(['signupStore']);
export const getSignupForm = pipe(
  getSignupState,
  pick(['email', 'password', 'passwordConfirmation']),
);
export const getSignupErrors = pipe(
  getSignupState,
  path(['meta', 'errors']),
);

export const getLoading = pipe(
  getSignupState,
  path(['meta', 'loading']),
);
