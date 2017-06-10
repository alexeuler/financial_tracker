import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { BrowserRouter, Switch, Route, Redirect } from 'react-router-dom';

import { getSessionState } from '../selectors/session';
import Login from '../components/Login';
import Signup from '../components/Signup';
import Expenses from '../components/Expenses';
import Users from '../components/Users';
import Report from '../components/Report';
import withNavbar from '../components/Navbar';

const Router = (props) => (
  <BrowserRouter>
    <Switch>
      <Route
        exact
        path="/"
        render={() => (
          props.session.id ? (
            <Redirect to={`/users/${props.session.id}/expenses`} />
          ) : (
            <Redirect to="/login" />
          )
        )}
      />
      <Route exact path="/login" component={withNavbar(Login)} />
      <Route exact path="/signup" component={withNavbar(Signup)} />
      <Route exact path="/users" component={withNavbar(Users)} />
      <Route exact path="/users/:userId/expenses" component={withNavbar(Expenses)} />
      <Route exact path="/users/:userId/expenses/report" component={withNavbar(Report)} />
    </Switch>
  </BrowserRouter>
);

Router.propTypes = {
  session: PropTypes.shape({
    id: PropTypes.number,
  }).isRequired,
};

const mapStateToProps = state => ({
  session: getSessionState(state),
});

export default connect(mapStateToProps, null)(Router);
