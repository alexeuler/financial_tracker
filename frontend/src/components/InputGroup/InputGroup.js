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
      <ErrorMessage message={props.errorMessage} />
    </div>
  );
};

InputGroup.defaultProps = {
  errorMessage: null,
};

InputGroup.propTypes = {
  label: PropTypes.string.isRequired,
  errorMessage: PropTypes.string,
};

export default InputGroup;
