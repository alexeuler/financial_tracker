import { path, find, pipe, sortBy, prop, pick } from 'ramda';

export const getUsers = state =>
  pipe(
    path(['usersStore', 'entities']),
    users => sortBy(prop('id'))(users || []),
  )(state);

export const getUser = (state, userId) => {
  const users = getUsers(state);
  return find(user => user.id === userId, users);
};

export const getEditingFocus = path(['usersStore', 'editingFocus']);

export const getUsersForm =
  path(['usersStore', 'form']);

export const getUsersCreateForm = pipe(
  getUsersForm,
  pick(['password', 'email']),
);

export const getUsersUpdateForm = pipe(
  getUsersForm,
  pick(['password', 'role']),
);

export const getUsersEditingFocus =
  path(['usersStore', 'editingFocus']);

export const getUsersErrors =
  path(['usersStore', 'meta', 'errors']);

export const getUsersLoading =
  path(['usersStore', 'meta', 'loading']);
