import React from 'react';
import PropTypes from 'prop-types';
import { omit } from 'ramda';

import Input from '../Input';

const InputGroup = (props) => {
  const inputProps = omit(['label'], props);
  return (
    <div className="mb4">
      <div className="f4 gray mb3">{props.label}</div>
      <Input {...inputProps} />
    </div>
  );
};

InputGroup.propTypes = {
  label: PropTypes.string.isRequired,
};

export default InputGroup;
