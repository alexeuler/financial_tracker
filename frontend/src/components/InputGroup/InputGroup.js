import React from 'react';
import PropTypes from 'prop-types';
import { omit } from 'ramda';

import Input from '../Input';
import ErrorMessage from '../ErrorMessage';

const InputGroup = (props) => {
  const inputProps = omit(['label'], props);
  const fontClass = props.small ? 'f5' : 'f4';
  return (
    <div className="mb4 w-100">
      <div className={`${fontClass} gray`}>{props.label}</div>
      <Input {...inputProps} />
      {props.errors.map(error => <ErrorMessage key={error} message={error} />)}
    </div>
  );
};

InputGroup.defaultProps = {
  errors: [],
  small: false,
};

InputGroup.propTypes = {
  label: PropTypes.string.isRequired,
  small: PropTypes.bool,
  errors: PropTypes.arrayOf(PropTypes.string),
};

export default InputGroup;
