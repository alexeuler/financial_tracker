import React from 'react';
import PropTypes from 'prop-types';

const ErrorMessage = props => {
  if (!props.message) return null;
  return (
    <div className={`avenir mb3 light-red ${props.className}`}>{props.message}</div>
  );
}

ErrorMessage.defaultProps = {
  message: null,
  className: '',
};

ErrorMessage.propTypes = {
  message: PropTypes.string,
  className: PropTypes.string,
};

export default ErrorMessage;
