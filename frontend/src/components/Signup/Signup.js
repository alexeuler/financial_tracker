import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

import { login, updateSignupForm } from '../../thunks';
import { getSignupForm, getSignupErrors } from '../../selectors/signup';

import ErrorMessage from '../ErrorMessage';
import InputGroup from '../InputGroup';
import Button from '../Button';

const Signup = (props) => (
  <div className="flex flex-column items-center justify-center vh-100 vw-100">
    <div className="w5 flex flex-column">
      <ErrorMessage message={props.errors.general} />
      <InputGroup
        label="Email"
        value={props.form.email}
        onChange={text => props.updateSignupForm({ email: text })}
      />
      <InputGroup
        label="Password"
        value={props.form.password}
        type="password"
        onChange={text => props.updateSignupForm({ password: text })}
      />
      <InputGroup
        label="Password Confirmation"
        value={props.form.passwordConfirmation}
        type="password"
        onChange={text => props.updateSignupForm({ passwordConfirmation: text })}
      />

      <Button
        title="Sign up"
        className="mb3 w-100"
        onClick={() => props.login(props.form)} 
      />
      <Link to="/login" className="tr">Login</Link>
    </div>
  </div>
);

Signup.propTypes = {
  form: PropTypes.shape({
    email: PropTypes.string,
    password: PropTypes.string,
    passwordConfirmation: PropTypes.string,
  }).isRequired,
  errors: PropTypes.object.isRequired,
  login: PropTypes.func.isRequired,
  updateSignupForm: PropTypes.func.isRequired,
};

const mapStateToProps = state => ({
  form: getSignupForm(state),
  errors: getSignupErrors(state),
});

const mapDispatchToProps = {
  login,
  updateSignupForm,
};

export default connect(mapStateToProps, mapDispatchToProps)(Signup);
