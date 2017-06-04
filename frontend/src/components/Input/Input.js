import React from 'react';
import PropTypes from 'prop-types';

const Input = props => (
  <input
    className="f4 mid-gray pv2 w5 input-reset outline-0 bb bt-0 br-0 bl-0 b--gray"
    value={props.value}
    onChange={e => props.onChange(e.target.value)}
  />
);

Input.propTypes = {
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
};

export default Input;
