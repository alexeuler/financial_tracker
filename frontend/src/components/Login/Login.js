import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

import { login, updateLoginForm } from '../../thunks';
import { getLoginForm, getLoginErrors } from '../../selectors/login';

import ErrorMessage from '../ErrorMessage';
import InputGroup from '../InputGroup';
import Button from '../Button';

const Login = (props) => (
  <div className="flex flex-column items-center justify-center vh-100 vw-100">
    <div className="w5 flex flex-column">
      <ErrorMessage message={props.errors.general} />
      <InputGroup
        label="Email"
        value={props.form.email}
        onChange={text => props.updateLoginForm({ email: text })}
      />
      <InputGroup
        label="Password"
        value={props.form.password}
        type="password"
        onChange={text => props.updateLoginForm({ password: text })}
      />
      <Button
        title="Sign in"
        className="mb3 w-100"
        onClick={() => props.login(props.form)} 
      />
      <Link to="/signup" className="tr">Sign up</Link>
    </div>
  </div>
);

Login.propTypes = {
  form: PropTypes.shape({
    email: PropTypes.string,
    password: PropTypes.string,
  }).isRequired,
  errors: PropTypes.object.isRequired,
  login: PropTypes.func.isRequired,
  updateLoginForm: PropTypes.func.isRequired,
};

const mapStateToProps = state => ({
  form: getLoginForm(state),
  errors: getLoginErrors(state),
});

const mapDispatchToProps = {
  login,
  updateLoginForm,
};

export default connect(mapStateToProps, mapDispatchToProps)(Login);
