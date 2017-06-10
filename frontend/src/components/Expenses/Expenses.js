import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

import { getSessionState } from '../../selectors/session';
import { getExpenses, getEditingFocus, getPage, getTotalPages } from '../../selectors/expenses';
import thunks from '../../thunks';
import Expense from '../Expense';
import EditExpense from '../EditExpense';
import Filters from '../Filters';
import Pagination from '../Pagination';

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
          <div className="mh2 ba b--light-gray w-100-m">
            {this.props.expenses.map(
              expense => <Expense 
                key={expense.id}
                edit={this.props.editingFocus === expense.id}
                onEdit={expenseId => this.props.setEditingFocus(this.props.match.params.userId, expenseId)}
                onDelete={expenseId => this.props.deleteExpense(this.props.match.params.userId, expenseId)}
                {...expense} 
              />)}
            {this.props.editingFocus && <a
              className="pa2 underline pointer b"
              onClick={() => this.props.setEditingFocus(this.props.match.params.userId, null)}
            >
              New
            </a>}
          </div>
          <Pagination
            selectedPage={this.props.page}
            paginatorWidth={7}
            maxPage={this.props.totalPages}
            onPageSelected={this.props.setPageExpenses}
          />
          <Filters />
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
  page: PropTypes.number.isRequired,
  totalPages: PropTypes.number.isRequired,
  setPageExpenses: PropTypes.func.isRequired,
  fetchExpenses: PropTypes.func,
  deleteExpense: PropTypes.func,
  setEditingFocus: PropTypes.func,
};

const mapStateToProps = (state, ownProps) => ({
  session: getSessionState(state),
  expenses: getExpenses(state, ownProps.match.params.userId),
  page: getPage(state),
  totalPages: getTotalPages(state, ownProps.match.params.userId),
  editingFocus: getEditingFocus(state),
})

const mapDispatchToProps = {
  fetchExpenses: thunks.fetchExpenses,
  deleteExpense: thunks.deleteExpense,
  setEditingFocus: thunks.setEditingFocusExpense,
  setPageExpenses: thunks.setPageExpenses,
};

export default connect(mapStateToProps, mapDispatchToProps)(Expenses);
