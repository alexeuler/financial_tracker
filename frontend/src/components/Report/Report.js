import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux'

import { getReport } from '../../selectors/expenses';
import thunks from '../../thunks';
import { moneyToString } from '../../utils';

class Report extends React.Component {

  componentDidMount() {
    this.fetch();
  }

  fetch = () => {
    this.props.fetchExpenses(this.props.match.params.userId, this.props.history);
  }

  render() {
    return (
      <div className="pa4">
        <table className="collapse ba b--light-gray mb3">
          <thead>
            <tr className="fw6">
              <td className="pv2 ph4">Period</td>
              <td className="pv2 ph4">Sum</td>
              <td className="pv2 ph4">Average</td>
            </tr>
          </thead>
          <tbody>
            {this.props.report.entries.map(entry =>
              <tr className="striped--light-gray" key={entry.start}>
                <td className="pv3 ph4">{`${entry.start} - ${entry.end}`}</td>
                <td className="pv3 ph4">{moneyToString(entry.sum.toFixed(2))}</td>
                <td className="pv3 ph4">{moneyToString(entry.avg.toFixed(2))}</td>
              </tr>
            )}
            {this.props.report.entries.length && <tr className="fw6 bt b--black">
              <td className="pv3 ph4">For all time from first to last expense</td>
              <td className="pv3 ph4">{moneyToString(this.props.report.sum.toFixed(2))}</td>
              <td className="pv3 ph4">{moneyToString(this.props.report.avg.toFixed(2))}</td>
            </tr>}
          </tbody>
        </table>
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
};

const mapStateToProps = (state, ownProps) => ({
  report: getReport(state, ownProps.match.params.userId),
});

const mapDispatchToProps = {
  fetchExpenses: thunks.fetchExpenses,
}

export default connect(mapStateToProps, mapDispatchToProps)(Report);
