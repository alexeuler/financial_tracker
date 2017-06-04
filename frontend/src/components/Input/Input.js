import React from 'react';
import PropTypes from 'prop-types';

const Input = props => (
  <input
    className="f4 mid-gray pv1 mb2 w-100 input-reset outline-0 bb bt-0 br-0 bl-0 b--gray lh-copy"
    value={props.value}
    type={props.type}
    onChange={e => props.onChange(e.target.value)}
  />
);

Input.defaultProps = {
  type: 'text',
}

Input.propTypes = {
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  type: PropTypes.string,
};

export default Input;
