import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

const User = props => (
  <div className={`flex flex-row-l flex-column ${props.edit ? 'bg-light-gray' : ''}`}>
    <div className="flex flex-row">
      <div className="pa2 w5 flex items-center">
        <div>{props.identity}</div>
      </div>
      <div className="pa2 w4 flex items-center">
        {props.role}
      </div>
    </div>
    <div className="pa2 flex items-center">
      <a className={`pv2 underline ${props.edit ? 'blue' : 'pointer'}`} onClick={() => props.onEdit(props.id)}>Edit</a>
      <a className="pa2 underline pointer" onClick={() => props.onDelete(props.id)}>Delete</a>
      {(props.session.role === 'Admin') && <Link
        className="pv2 underline pointer black"
        to={`/users/${props.id}/expenses`}
      >
        Expenses
      </Link>
      }
    </div>
  </div>
);

User.defaultProps = {
  comment: null,
  edit: false,
  password: '',
};

User.propTypes = {
  id: PropTypes.number.isRequired,
  identity: PropTypes.string.isRequired,
  password: PropTypes.string,
  role: PropTypes.string.isRequired,
  edit: PropTypes.bool,
  onEdit: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
  session: PropTypes.shape({
    role: PropTypes.string.isRequired,
  }).isRequired,
};

export default User;
