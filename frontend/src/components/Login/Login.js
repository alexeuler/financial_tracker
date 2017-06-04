import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import { login, updateLoginForm } from '../../thunks';
import { getLoginForm } from '../../selectors/login';
import InputGroup from '../InputGroup';
import Button from '../Button';


const Login = (props) => (
  <div className="flex flex-column items-center justify-center vh-100 vw-100">
    <InputGroup
      label="Login"
      value={props.form.login}
      onChange={text => props.updateLoginForm({ login: text })}
    />
    <InputGroup
      label="Password"
      value={props.form.password}
      onChange={text => props.updateLoginForm({ password: text })}
    />
    <Button title="Sing in" />
  </div>
);

Login.propTypes = {
  form: PropTypes.shape({
    login: PropTypes.string,
    password: PropTypes.string,
  }).isRequired,
  login: PropTypes.func.isRequired,
  updateLoginForm: PropTypes.func.isRequired,
};

const mapStateToProps = state => ({
  form: getLoginForm(state),
});

const mapDispatchToProps = {
  login,
  updateLoginForm,
};

export default connect(mapStateToProps, mapDispatchToProps)(Login);
