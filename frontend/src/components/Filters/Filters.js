import React from 'react';
import PropTypes from 'prop-types';

class Filters extends React.Component {
  constructor(props) {
    super(props);
    this.state = { visible: false };
  }

  renderFilters = () => {
    if (!this.state.visible) return null;
    return (
      <div>Expanded</div>
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
