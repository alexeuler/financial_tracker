import React from 'react';
import PropTypes from 'prop-types';

const Button = (props) => {
  const extraClasses = props.disabled ? `bg-light-${props.color}` : `pointer bg-${props.color}`;
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
  color: 'blue',
};

Button.propTypes = {
  title: PropTypes.string.isRequired,
  onClick: PropTypes.func.isRequired,
  disabled: PropTypes.bool,
  color: PropTypes.oneOf(['blue', 'green']),
  className: PropTypes.string,
};

export default Button;
