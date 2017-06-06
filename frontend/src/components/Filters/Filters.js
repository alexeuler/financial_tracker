import React from 'react';
import PropTypes from 'prop-types';

import InputGroup from '../InputGroup';

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
            onChange={console.log}
          />
          <InputGroup
            label="Amount To"
            type="number"
            small
            className="ph2"
            onChange={console.log}
          />
        </div>
        <InputGroup
          label="Containing text"
          small
          className="ph2"
          onChange={console.log}
        />
        <InputGroup
          label="Date From"
          type="date"
          small
          onChange={console.log}
        />
        <InputGroup
          label="Date To"
          type="date"
          small
          onChange={console.log}
        />

      </div>
    )
  }

  render() {
    return (
      <div className="pa2">
        <a 
          className="underline blue pointer"
          onClick={() => this.setState({ visible: !this.state.visible })}
        >
          Filters
        </a>
        {this.renderFilters()}
      </div>
    )
  }
};

Filters.propTypes = {

}

export default Filters;
