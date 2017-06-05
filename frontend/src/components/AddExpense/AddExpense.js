import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import ErrorMessage from '../ErrorMessage';
import InputGroup from '../InputGroup';
import Button from '../Button';

import thunks from '../../thunks';
import { getExpensesForm, getExpensesErrors, getExpensesLoading } from '../../selectors/expenses';

const AddExpense = props => (
  (() => console.log('-------------', thunks))() ||
  <div className="w5 flex flex-column">
    {props.errors.general && props.errors.general.map(error =>
      <ErrorMessage key={error} message={error} />,
    )}
    <InputGroup
      label="Date"
      value={props.form.occuredAt}
      errors={props.errors.occuredAt}
      onChange={text => props.updateFormExpenses({ occuredAt: text })}
    />
    <InputGroup
      label="Amount"
      value={props.form.amount}
      errors={props.errors.amount}
      type="password"
      onChange={text => props.updateFormExpenses({ amount: text })}
    />
    <InputGroup
      label="Description"
      value={props.form.description}
      errors={props.errors.description}
      type="password"
      onChange={text => props.updateFormExpenses({ description: text })}
    />
    <InputGroup
      label="Comment"
      value={props.form.comment}
      errors={props.errors.comment}
      type="password"
      onChange={text => props.updateFormExpenses({ comment: text })}
    />
    <Button
      title="Add expense"
      className="mb3 w-100"
      color="green"
      disabled={props.loading}
      onClick={console.log}
    />
  </div>
);

AddExpense.propTypes = {
  form: PropTypes.shape({
    occuredAt: PropTypes.string.isRequired,
    amount: PropTypes.string.isRequired,
    description: PropTypes.string.isRequired,
    comment: PropTypes.string.isRequired,
  }),
  errors: PropTypes.object.isRequired,
  loading: PropTypes.bool.isRequired,
  updateFormExpenses: PropTypes.func.isRequired,
};

const mapStateToProps = state => ({
  form: getExpensesForm(state),
  errors: getExpensesErrors(state),
  loading: getExpensesLoading(state),
});

const mapDispatchToProps = {
  updateFormExpenses: thunks.updateFormExpenses,
};

export default connect(mapStateToProps, mapDispatchToProps)(AddExpense);
