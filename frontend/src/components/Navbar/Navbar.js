import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Link, withRouter } from 'react-router-dom';

import { getSessionState } from '../../selectors/session';

const renderWithSession = (session) => (
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
      {<Link className="ph3 blue" to="/">Logout</Link>}
    </div>
  </div>
);

const renderWithoutSession = (
  <div>
    <Link to="/login">Login</Link>
    <Link to="/signup">Sign up</Link>
  </div>
);

const Navbar = props => (
  <div className="no-print">
    {props.session ? renderWithSession(props.session) : renderWithoutSession()}
  </div>
);

Navbar.defaultProps = {
  session: null,
};

Navbar.propTypes = {
  session: PropTypes.shape({
    id: PropTypes.number,
    role: PropTypes.string,
  }),
};

const mapStateToProps = state => ({
  session: getSessionState(state),
});

export default withRouter(connect(mapStateToProps)(Navbar));
