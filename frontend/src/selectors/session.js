import { path } from 'ramda';

export const getSessionState = path(['sessionStore']);
export const getToken = path(['sessionStore', 'token']);
