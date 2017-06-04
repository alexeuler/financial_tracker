import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';

import Login from '../components/Login';
import Signup from '../components/Signup';

const Router = () => (
  <BrowserRouter>
    <Switch>
      <Route exact path="/login" component={Login} />
      <Route exact path="/signup" component={Signup} />
    </Switch>
  </BrowserRouter>
);

export default Router;
