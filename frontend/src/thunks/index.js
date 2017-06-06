import * as loginThunks from './login';
import * as signupThunks from './signup';
import * as expensesThunks from './expenses';
import * as usersThunks from './users';

export default {
  ...loginThunks,
  ...signupThunks,
  ...expensesThunks,
  ...usersThunks,
};
