import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import { getSessionState } from '../../selectors/session';
import { getExpenses } from '../../selectors/expenses';
import thunks from '../../thunks';
import Expense from '../Expense';
import AddExpense from '../AddExpense';

class Expenses extends React.Component {

  componentDidMount() {
    this.fetch()
  }

  fetch = () => {
    this.props.fetchExpenses(this.props.match.params.userId, this.props.history);
  }

  render() {
    return (
      <div>
        <AddExpense />
        {this.props.expenses.map(expense => <Expense {...expense} />)}
      </div>
    )
  }
}

Expenses.defaultProps = {
  expenses: [],
}

Expenses.propTypes = {
  session: PropTypes.shape({
    identity: PropTypes.string,
  }),
  match: PropTypes.shape({
    params: PropTypes.shape({
      userId: PropTypes.string.isRequired,
    }).isRequired,
  }).isRequired,
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
  expenses: PropTypes.array.isRequired,
  fetchExpenses: PropTypes.func,
};

const mapStateToProps = (state, ownProps) => ({
  session: getSessionState(state),
  expenses: getExpenses(state, ownProps.match.params.userId),
})

const mapDispatchToProps = {
  fetchExpenses: thunks.fetchExpenses,
};

export default connect(mapStateToProps, mapDispatchToProps)(Expenses);
