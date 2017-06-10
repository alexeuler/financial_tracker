import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux'

import { getReport, getExpensesLoading } from '../../selectors/expenses';
import thunks from '../../thunks';
import { moneyToString } from '../../utils';

class Report extends React.Component {

  componentDidMount() {
    this.fetch();
  }

  fetch = () => {
    this.props.fetchExpenses(this.props.match.params.userId, this.props.history);
  }

  renderLoading = () => {
    if (!this.props.loading) return null;
    return (
      <div className="flex items-center justify-start flicker pv3-l ph4-l pv1 ph2">Loading...</div>
    )
  }

  renderReport = () => {
    if ((!this.props.report || !this.props.report.entries || !this.props.report.entries.length)) {
      return (
        <tbody>
          <td className="w5 pv3-l ph4-l pv1 ph2">No items</td>
          <td className="w5"></td>
          <td className="w4"></td>
        </tbody>
      )
    }

    return (
      <tbody>
        {this.props.report.entries.map(entry =>
          <tr className="striped--light-gray" key={entry.start}>
            <td className="pv3-l ph4-l pv1 ph2">
              <span style={{'white-space': 'nowrap'}}>{entry.start}</span>
              <span>{' - '}</span>
              <span style={{'white-space': 'nowrap'}}>{entry.end}</span>
            </td>
            <td className="pv3-l ph4-l pv1 ph2 tr">{moneyToString(entry.sum.toFixed(2))}</td>
            <td className="pv3-l ph4-l pv1 ph2 tr">{moneyToString(entry.avg.toFixed(2))}</td>
          </tr>
        )}
        {this.props.report.entries.length && <tr className="fw6 bt b--black">
          <td className="pv3-l ph4-l pv1 ph2">For all time from first to last expense</td>
          <td className="pv3-l ph4-l pv1 ph2 tr">{moneyToString(this.props.report.sum.toFixed(2))}</td>
          <td className="pv3-l ph4-l pv1 ph2 tr">{moneyToString(this.props.report.avg.toFixed(2))}</td>
        </tr>}
      </tbody>
    )
  }

  render() {
    return (
      <div className="pa4">
        <table className="collapse ba b--light-gray mb3">
          <thead>
            <tr className="fw6">
              <td className="pv2 ph4">Period</td>
              <td className="pv2 ph4 tr">Sum</td>
              <td className="pv2 ph4 tr">Average</td>
            </tr>
          </thead>
          {this.renderReport()}
        </table>
        {this.renderLoading()}
        <a className="underline blue pointer no-print" onClick={window.print}>Print</a>
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
  loading: PropTypes.bool.isRequired,
};

const mapStateToProps = (state, ownProps) => ({
  report: getReport(state, ownProps.match.params.userId),
  loading: getExpensesLoading(state),
});

const mapDispatchToProps = {
  fetchExpenses: thunks.fetchExpenses,
}

export default connect(mapStateToProps, mapDispatchToProps)(Report);
