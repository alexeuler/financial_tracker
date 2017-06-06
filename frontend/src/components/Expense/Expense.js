import React from 'react';
import PropTypes from 'prop-types';

const Expense = props => (
  <div className={`flex flex-row ${props.edit ? 'bg-light-gray' : ''}`}>
    <div className="pa2 w4-l w5">
      <div>{props.occuredAt.split(' ')[0]}</div>
      <div>{props.occuredAt.split(' ')[1]}</div>
    </div>
    <div className="pa2 w5">
      <div className="b">{props.description}</div>
      <div className="gray">{props.comment}</div>
    </div>
    <div className="pa2 w3 flex items-center">
      {props.amount}
    </div>
    <div className="pa2 flex items-center">
      <a className={`pa2 underline ${props.edit ? 'blue' : 'pointer'}`} onClick={() => props.onEdit(props.id)}>Edit</a>
      <a className="pa2 underline pointer" onClick={() => props.onDelete(props.id)}>Delete</a>
    </div>
  </div>
);

Expense.defaultProps = {
  comment: null,
  edit: false,
};

Expense.propTypes = {
  id: PropTypes.number.isRequired,
  amount: PropTypes.number.isRequired,
  description: PropTypes.string.isRequired,
  comment: PropTypes.string,
  occuredAt: PropTypes.string.isRequired,
  userId: PropTypes.number.isRequired,
  edit: PropTypes.bool,
  onEdit: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
};

export default Expense;
