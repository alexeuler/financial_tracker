import React from 'react';
import PropTypes from 'prop-types';

const ErrorMessage = props => {
  if (!props.message) return null;
  return (
    <div className="avenir mb3 light-red">{props.message}</div>
  );
}

ErrorMessage.defaultProps = {
  message: null,
};

ErrorMessage.propTypes = {
  message: PropTypes.string,
};

export default ErrorMessage;
