import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux'

import { getReport } from '../../selectors/expenses';
import thunks from '../../thunks';

class Report extends React.Component {

  componentDidMount() {
    this.fetch();
  }

  fetch = () => {
    this.props.fetchExpenses(this.props.match.params.userId, this.props.history);
  }

  render() {
    console.log('---------------', this.props);
    return (
      <div>
        Report
      </div>
    );
  }
}

Report.propTypes = {
  report: PropTypes.object.isRequired,
  fetchExpenses: PropTypes.func.isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      userId: PropTypes.string.isRequired,
    }).isRequired,
  }).isRequired,
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
};

const mapStateToProps = (state, ownProps) => ({
  report: getReport(state, ownProps.match.params.userId),
});

const mapDispatchToProps = {
  fetchExpenses: thunks.fetchExpenses,
}

export default connect(mapStateToProps, mapDispatchToProps)(Report);
