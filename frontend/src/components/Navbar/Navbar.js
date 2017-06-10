import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Link, withRouter } from 'react-router-dom';

import { getSessionState } from '../../selectors/session';
import thunks from '../../thunks';

const matchedClass = 'dark-gray';
const unmatchedClass = 'blue';

const renderWithSession = (session, logout, path) => {
  const manageUsersClass = path === '/users' ? matchedClass : unmatchedClass;
  const manageExpensesClass = path === '/users/:userId/expenses' ? matchedClass : unmatchedClass;
  const reportClass = path === '/users/:userId/expenses/report' ? matchedClass : unmatchedClass;

  return (
    <div className="pa3 flex-l">
      <div className="pv2">
        {session.role === 'Admin' && <Link className={`ph3 no-underline ${manageUsersClass}`} to="/users">Manage users and their expenses</Link>}
        {session.role === 'Manager' && <Link className={`ph3 no-underline ${manageUsersClass}`} to="/users">Manage users</Link>}
      </div>
      <div className="pv2">
        {<Link className={`ph3 no-underline ${manageExpensesClass}`} to={`/users/${session.id}/expenses`}>Personal expenses</Link>}
      </div>
      <div className="pv2">
        {<Link className={`ph3 no-underline ${reportClass}`} to={`/users/${session.id}/expenses/report`}>Expenses report</Link>}
      </div>
      <div className="pv2">
        {<a className="ph3 no-underline blue pointer" onClick={logout}>Logout</a>}
      </div>
    </div>
  );
}

const renderWithoutSession = (path) => {
  const loginClass = path === '/login' ? matchedClass : unmatchedClass;
  const signupClass = path === '/signup' ? matchedClass : unmatchedClass;

  return (
    <div className="pa3">
      <Link className={`ph3 no-underline ${loginClass}`} to="/login">Login</Link>
      <Link className={`ph3 no-underline ${signupClass}`} to="/signup">Sign up</Link>
    </div>
  );
}

const Navbar = props => (
  <div className="no-print bb b--gray">
    {props.session.token ?
      renderWithSession(props.session, () => props.logout(props.history), props.match.path) :
      renderWithoutSession(props.match.path)}
  </div>
);

Navbar.defaultProps = {
  session: null,
};

Navbar.propTypes = {
  session: PropTypes.shape({
    id: PropTypes.number,
    role: PropTypes.string,
    token: PropTypes.string,
  }),
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
  logout: PropTypes.func.isRequired,
  match: PropTypes.shape({
    path: PropTypes.string.isRequired,
  }).isRequired,
};

const mapStateToProps = state => ({
  session: getSessionState(state),
});

const mapDispatchToProps = {
  logout: thunks.resetSession,
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Navbar));
