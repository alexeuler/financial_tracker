import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import { getFilters } from '../../selectors/expenses';

import InputGroup from '../InputGroup';
import thunks from '../../thunks';

class Filters extends React.Component {
  constructor(props) {
    super(props);
    this.state = { visible: false };
  }

  renderFilters = () => {
    if (!this.state.visible) return null;
    return (
      <div>
        <div className="flex flex-row">
          <InputGroup
            label="Amount From"
            type="number"
            small
            className="ph2"
            value={this.props.filters.amountFrom}
            onChange={(value) => this.props.updateFiltersExpenses({ amountFrom: value })}
          />
          <InputGroup
            label="Amount To"
            type="number"
            small
            className="ph2"
            value={this.props.filters.amountTo}
            onChange={(value) => this.props.updateFiltersExpenses({ amountTo: value })}
          />
        </div>
        <InputGroup
          label="Containing text"
          small
          className="ph2"
          value={this.props.filters.text}
          onChange={(value) => this.props.updateFiltersExpenses({ text: value })}
        />
        <InputGroup
          label="Date From"
          type="date"
          small
          className="ph2"
          value={this.props.filters.dateFrom}
          onChange={(value) => this.props.updateFiltersExpenses({ dateFrom: value })}
        />
        <InputGroup
          label="Date To"
          type="date"
          className="ph2"
          small
          value={this.props.filters.dateTo}
          onChange={(value) => this.props.updateFiltersExpenses({ dateTo: value })}
        />

      </div>
    )
  }

  render() {
    return (
      <div className="pa2">
        <div className="pb3">
          <a 
            className="underline blue pointer"
            onClick={() => this.setState({ visible: !this.state.visible })}
          >
            Filters
          </a>
        </div>
        {this.renderFilters()}
      </div>
    )
  }
};

const mapStateToProps = state => ({
  filters: getFilters(state),
});

const mapDispatchToProps = {
  updateFiltersExpenses: thunks.updateFiltersExpenses,
  resetFiltersExpenses: thunks.resetFiltersExpenses,
}

Filters.propTypes = {
  filters: PropTypes.shape({
    dateFrom: PropTypes.string,
    dateTo: PropTypes.string,
    amountFrom: PropTypes.string,
    amountTo: PropTypes.string,
    text: PropTypes.string,
  }),
  updateFiltersExpenses: PropTypes.func.isRequired,
  resetFiltersExpenses: PropTypes.func.isRequired,
}

export default connect(mapStateToProps, mapDispatchToProps)(Filters);
