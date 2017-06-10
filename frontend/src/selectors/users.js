import { path, find, pipe, sortBy, prop, pick, drop, take } from 'ramda';

const getRawUsers = path(['usersStore', 'entities']);

export const getPage =
  path(['usersStore', 'page']);

export const getPageSize =
  path(['usersStore', 'pageSize']);

export const getTotalPages = state => Math.ceil(getRawUsers(state).length / getPageSize(state));

export const getUsers = (state) => {
  const page = getPage(state);
  const pageSize = getPageSize(state);
  return pipe(
    getRawUsers,
    users => sortBy(prop('id'))(users || []),
    drop((page - 1) * pageSize),
    take(pageSize),
  )(state);
};

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
