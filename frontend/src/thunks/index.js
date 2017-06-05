import * as loginThunks from './login';
import * as signupThunks from './signup';
import * as expensesThunks from './expenses';

export default {
  ...loginThunks,
  ...signupThunks,
  ...expensesThunks,
};
