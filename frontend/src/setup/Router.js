import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';

import Login from '../components/Login';
import Signup from '../components/Signup';
import Expenses from '../components/Expenses';
import Users from '../components/Users';

const Router = () => (
  <BrowserRouter>
    <Switch>
      <Route exact path="/login" component={Login} />
      <Route exact path="/signup" component={Signup} />
      <Route exact path="/users" component={Users} />
      <Route exact path="/users/:userId/expenses" component={Expenses} />
    </Switch>
  </BrowserRouter>
);

export default Router;
