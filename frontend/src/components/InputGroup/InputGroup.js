import React, { PropTypes } from 'react';

import Input from '../Input';

const InputGroup = props => (
  <div className="mb4">
    <div className="f4 gray mb3">{props.label}</div>
    <Input value={props.value} onChange={props.onChange} />
  </div>
);

InputGroup.propTypes = {
  label: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
};

export default InputGroup;
