import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import { login, updateLoginForm } from '../../thunks';
import { getLoginForm, getLoginErrors } from '../../selectors/login';

import ErrorMessage from '../ErrorMessage';
import InputGroup from '../InputGroup';
import Button from '../Button';


const Login = (props) => (
  <div className="flex flex-column items-center justify-center vh-100 vw-100">
    <div className="w5 flex flex-column justify-center">
      <ErrorMessage message={props.errors.general} />
      <InputGroup
        label="Login"
        value={props.form.login}
        onChange={text => props.updateLoginForm({ login: text })}
      />
      <InputGroup
        label="Password"
        value={props.form.password}
        type="password"
        onChange={text => props.updateLoginForm({ password: text })}
      />
      <Button title="Sign in" onClick={() => props.login(props.form)} />
    </div>
  </div>
);

Login.propTypes = {
  form: PropTypes.shape({
    login: PropTypes.string,
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
