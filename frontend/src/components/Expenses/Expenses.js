import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import { getSessionState } from '../../selectors/session';
import { getExpenses, getEditingFocus } from '../../selectors/expenses';
import thunks from '../../thunks';
import Expense from '../Expense';
import EditExpense from '../EditExpense';

class Expenses extends React.Component {

  componentDidMount() {
    this.fetch();
  }

  fetch = () => {
    this.props.fetchExpenses(this.props.match.params.userId, this.props.history);
  }

  render() {
    return (
      <div className="flex flex-row-l flex-column pa4">
        <div>
          {this.props.expenses.map(
            expense => <Expense 
              key={expense.id}
              edit={this.props.editingFocus === expense.id}
              onEdit={expenseId => this.props.setEditingFocus(this.props.match.params.userId, expenseId)}
              {...expense} 
            />)}
          <a
            className={`pa2 underline ${this.props.editingFocus ? 'pointer' : 'blue'}`}
            onClick={() => this.props.setEditingFocus(this.props.match.params.userId, null)}
          >
            New
          </a>
        </div>
        <EditExpense
          history={this.props.history} 
          match={this.props.match}
          submitTitle={this.props.editingFocus ? 'Update expense' : 'Add expense'}
        />
      </div>
    )
  }
}

Expenses.defaultProps = {
  expenses: [],
  editingFocus: null,
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
  editingFocus: PropTypes.number,
  expenses: PropTypes.array.isRequired,
  fetchExpenses: PropTypes.func,
  setEditingFocus: PropTypes.func,
};

const mapStateToProps = (state, ownProps) => ({
  session: getSessionState(state),
  expenses: getExpenses(state, ownProps.match.params.userId),
  editingFocus: getEditingFocus(state),
})

const mapDispatchToProps = {
  fetchExpenses: thunks.fetchExpenses,
  setEditingFocus: thunks.setEditingFocusExpense,
};

export default connect(mapStateToProps, mapDispatchToProps)(Expenses);
