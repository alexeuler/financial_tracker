import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import { getSessionState } from '../../selectors/session';
import { getUsers, getEditingFocus } from '../../selectors/users';
import thunks from '../../thunks';
import User from '../User';
import EditUser from '../EditUser';

class Users extends React.Component {

  componentDidMount() {
    this.fetch();
  }

  fetch = () => {
    this.props.fetchUsers(this.props.history);
  }

  render() {
    return (
      <div className="flex flex-row-l flex-column pa4">
        <div>
          {this.props.users.map(
            user => <User 
              key={user.id}
              edit={this.props.editingFocus === user.id}
              onEdit={this.props.setEditingFocus}
              onDelete={this.props.deleteUser}
              {...user} 
            />)}
          {this.props.editingFocus && <a
            className="pa2 underline pointer b"
            onClick={() => this.props.setEditingFocus(null)}
          >
            New
          </a>}
        </div>
        <EditUser
          history={this.props.history} 
          submitTitle={this.props.editingFocus ? 'Update user' : 'Add user'}
        />
      </div>
    )
  }
}

Users.defaultProps = {
  users: [],
  editingFocus: null,
}

Users.propTypes = {
  session: PropTypes.shape({
    identity: PropTypes.string,
  }),
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
  editingFocus: PropTypes.number,
  users: PropTypes.array.isRequired,
  fetchUsers: PropTypes.func,
  deleteUser: PropTypes.func,
  setEditingFocus: PropTypes.func,
};

const mapStateToProps = (state, ownProps) => ({
  session: getSessionState(state),
  users: getUsers(state),
  editingFocus: getEditingFocus(state),
})

const mapDispatchToProps = {
  fetchUsers: thunks.fetchUsers,
  deleteUser: thunks.deleteUser,
  setEditingFocus: thunks.setEditingFocusUser,
};

export default connect(mapStateToProps, mapDispatchToProps)(Users);
