import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Link, withRouter } from 'react-router-dom';

import { getSessionState } from '../../selectors/session';
import thunks from '../../thunks';

const renderWithSession = (session, logout) => (
  <div className="pa3 flex-l">
    <div className="pv2">
      {session.role === 'Admin' && <Link className="ph3 blue" to="/users">Manage users and their expenses</Link>}
      {session.role === 'Manager' && <Link className="ph3 blue" to="/users">Manage users</Link>}
    </div>
    <div className="pv2">
      {<Link className="ph3 blue" to={`/users/${session.id}/expenses`}>Personal expenses</Link>}
    </div>
    <div className="pv2">
      {<Link className="ph3 blue" to={`/users/${session.id}/expenses/report`}>Expenses report</Link>}
    </div>
    <div className="pv2">
      {<a className="ph3 blue pointer underline" onClick={logout}>Logout</a>}
    </div>
  </div>
);

const renderWithoutSession = () => (
  <div className="pa3">
    <Link className="ph3 blue" to="/login">Login</Link>
    <Link className="ph3 blue" to="/signup">Sign up</Link>
  </div>
);

const Navbar = props => (
  <div className="no-print">
    {props.session.token ?
      renderWithSession(props.session, () => props.logout(props.history)) :
      renderWithoutSession()}
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
};

const mapStateToProps = state => ({
  session: getSessionState(state),
});

const mapDispatchToProps = {
  logout: thunks.resetSession,
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Navbar));
