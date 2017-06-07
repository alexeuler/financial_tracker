import * as loginThunks from './login';
import * as signupThunks from './signup';
import * as expensesThunks from './expenses';
import * as usersThunks from './users';
import * as sessionThunks from './session';

export default {
  ...loginThunks,
  ...signupThunks,
  ...expensesThunks,
  ...usersThunks,
  ...sessionThunks,
};
