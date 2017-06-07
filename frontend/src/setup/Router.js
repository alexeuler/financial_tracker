import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';

import Login from '../components/Login';
import Signup from '../components/Signup';
import Expenses from '../components/Expenses';
import Users from '../components/Users';
import Report from '../components/Report';
import withNavbar from '../components/Navbar';

const Router = () => (
  <BrowserRouter>
    <Switch>
      <Route exact path="/login" component={withNavbar(Login)} />
      <Route exact path="/signup" component={withNavbar(Signup)} />
      <Route exact path="/users" component={withNavbar(Users)} />
      <Route exact path="/users/:userId/expenses" component={withNavbar(Expenses)} />
      <Route exact path="/users/:userId/expenses/report" component={withNavbar(Report)} />
    </Switch>
  </BrowserRouter>
);

export default Router;
