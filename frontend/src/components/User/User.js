import React from 'react';
import PropTypes from 'prop-types';

const User = props => (
  <div className={`flex flex-row-l flex-column ${props.edit ? 'bg-light-gray' : ''}`}>
    <div className="flex flex-row">
      <div className="pa2 w4">
        <div>{props.identity}</div>
      </div>
      <div className="pa2 w5 flex flex-column justify-center">
        <div className="b">{props.password}</div>
      </div>
      <div className="pa2 w3 flex items-center">
        {props.role}
      </div>
    </div>
    <div className="pa2 flex items-center">
      <a className={`pv2 underline ${props.edit ? 'blue' : 'pointer'}`} onClick={() => props.onEdit(props.id)}>Edit</a>
      <a className="pa2 underline pointer" onClick={() => props.onDelete(props.id)}>Delete</a>
    </div>
  </div>
);

User.defaultProps = {
  comment: null,
  edit: false,
};

User.propTypes = {
  id: PropTypes.number.isRequired,
  identity: PropTypes.string.isRequired,
  password: PropTypes.string.isRequired,
  role: PropTypes.string.isRequired,
  edit: PropTypes.bool,
  onEdit: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
};

export default User;
