import React from 'react';
import PropTypes from 'prop-types';

const Button = (props) => {
  const extraClasses = props.disabled ? 'bg-light-blue' : 'pointer bg-blue';
  return (
    <button
      className={`white f4 avenir bn br-pill pv3 ph4 outline-0 ${extraClasses} ${props.className}`}
      disabled={props.disabled}
      onClick={props.onClick}
    >
      {props.title}
    </button>
  );
};

Button.defaultProps = {
  disabled: false,
  className: '',
};

Button.propTypes = {
  title: PropTypes.string.isRequired,
  onClick: PropTypes.func.isRequired,
  disabled: PropTypes.bool,
  className: PropTypes.string,
};

export default Button;
