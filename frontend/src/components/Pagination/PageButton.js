import React from 'react';
import PropTypes from 'prop-types';

const getClasses = ({ active, disabled }) => {
  if (active) {
    return 'dark-gray mh1';
  }

  const common = 'mh1';
  return disabled
    ? `${common} dark-gray`
    : `${common} blue pointer underline`;
};

const PageButton = props => (
  <div className={props.className}>
    <a
      className={`
        flex
        pa2
        f4
        avenir
        items-center
        justify-center
        ${getClasses(props)}
      `}
      onClick={() => !props.disabled && !props.active && props.onClick()}
    >
      {props.children}
    </a>
  </div>
);

PageButton.defaultProps = {
  active: false,
  disabled: false,
  className: '',
};

PageButton.propTypes = {
  // eslint-disable-next-line
  active: PropTypes.bool,
  disabled: PropTypes.bool,
  children: PropTypes.node.isRequired,
  onClick: PropTypes.func.isRequired,
  className: PropTypes.string,
};

export default PageButton;
