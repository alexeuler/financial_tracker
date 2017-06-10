import React from 'react';
import PropTypes from 'prop-types';

import { moneyToString } from '../../utils';

const Expense = props => (
  <div className={`flex flex-row-l flex-column ${props.edit ? 'bg-light-gray' : ''}`}>
    <div className="flex flex-row">
      <div className="pa2 w4">
        <div>{props.occuredAt.split(' ')[0]}</div>
        <div>{props.occuredAt.split(' ')[1]}</div>
      </div>
      <div className="pa2 w5 flex flex-column justify-center">
        <div className="b">{props.description}</div>
        <div className="gray">{props.comment}</div>
      </div>
    </div>
    <div className="pa2 flex items-center">
      <div className="pa2-l pv2 w4 flex items-center">
        {moneyToString(props.amount)}
      </div>
      <a className={`pv2 underline ${props.edit ? 'blue' : 'pointer'}`} onClick={() => props.onEdit(props.id)}>Edit</a>
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
