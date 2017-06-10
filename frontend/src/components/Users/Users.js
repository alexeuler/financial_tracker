import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import { getSessionState } from '../../selectors/session';
import { getUsers, getEditingFocus, getPage, getTotalPages } from '../../selectors/users';
import thunks from '../../thunks';
import User from '../User';
import EditUser from '../EditUser';
import Pagination from '../Pagination';

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
        <div className="ba b--light-gray pa2">
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
          <Pagination
            selectedPage={this.props.page}
            paginatorWidth={7}
            maxPage={this.props.totalPages}
            onPageSelected={this.props.setPageUsers}
          />
        </div>
        <EditUser
          history={this.props.history}
          edit={!!this.props.editingFocus}
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
  page: PropTypes.number.isRequired,
  totalPages: PropTypes.number.isRequired,
  users: PropTypes.array.isRequired,
  setPageUsers: PropTypes.func.isRequired,
  fetchUsers: PropTypes.func,
  deleteUser: PropTypes.func,
  setEditingFocus: PropTypes.func,
};

const mapStateToProps = (state, ownProps) => ({
  session: getSessionState(state),
  users: getUsers(state),
  editingFocus: getEditingFocus(state),
  page: getPage(state),
  totalPages: getTotalPages(state),
})

const mapDispatchToProps = {
  fetchUsers: thunks.fetchUsers,
  deleteUser: thunks.deleteUser,
  setEditingFocus: thunks.setEditingFocusUser,
  setPageUsers: thunks.setPageUsers,
};

export default connect(mapStateToProps, mapDispatchToProps)(Users);
