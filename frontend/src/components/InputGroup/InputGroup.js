import React from 'react';
import PropTypes from 'prop-types';
import { omit } from 'ramda';

import Input from '../Input';
import ErrorMessage from '../ErrorMessage';

const InputGroup = (props) => {
  const inputProps = omit(['label'], props);
  return (
    <div className="mb4 w-100">
      <div className="f4 gray">{props.label}</div>
      <Input {...inputProps} />
      {props.errors.map(error => <ErrorMessage message={error} />)}
    </div>
  );
};

InputGroup.defaultProps = {
  errors: [],
};

InputGroup.propTypes = {
  label: PropTypes.string.isRequired,
  errors: PropTypes.arrayOf(PropTypes.string),
};

export default InputGroup;
