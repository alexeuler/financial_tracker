import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import ErrorMessage from '../ErrorMessage';
import InputGroup from '../InputGroup';
import Button from '../Button';

import thunks from '../../thunks';
import { getExpensesForm, getExpensesErrors, getExpensesLoading } from '../../selectors/expenses';

const AddExpense = props => (
  <div className="w5 flex flex-column">
    {props.errors.general && props.errors.general.map(error =>
      <ErrorMessage key={error} message={error} />,
    )}
    <InputGroup
      label="Amount"
      value={props.form.amount}
      errors={props.errors.amount}
      onChange={text => props.updateFormExpenses({ amount: text })}
    />
    <InputGroup
      label="Description"
      value={props.form.description}
      errors={props.errors.description}
      onChange={text => props.updateFormExpenses({ description: text })}
    />
    <InputGroup
      label="Date"
      value={props.form.occuredAt}
      type="date"
      errors={props.errors.occuredAt}
      onChange={text => props.updateFormExpenses({ occuredAt: text })}
    />
    <InputGroup
      label="Comment"
      value={props.form.comment}
      errors={props.errors.comment}
      onChange={text => props.updateFormExpenses({ comment: text })}
    />
    <Button
      title="Add expense"
      className="mb3 w-100"
      color="green"
      disabled={props.loading}
      onClick={() => props.createExpense(props.match.params.userId, props.history)}
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
  match: PropTypes.shape({
    params: PropTypes.shape({
      userId: PropTypes.string.isRequired,
    }).isRequired,
  }).isRequired,
  errors: PropTypes.object.isRequired,
  loading: PropTypes.bool.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
  updateFormExpenses: PropTypes.func.isRequired,
  createExpense: PropTypes.func.isRequired,
};

const mapStateToProps = state => ({
  form: getExpensesForm(state),
  errors: getExpensesErrors(state),
  loading: getExpensesLoading(state),
});

const mapDispatchToProps = {
  updateFormExpenses: thunks.updateFormExpenses,
  createExpense: thunks.createExpense,
};

export default connect(mapStateToProps, mapDispatchToProps)(AddExpense);
