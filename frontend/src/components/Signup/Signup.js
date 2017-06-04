import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

import { signup, updateSignupForm } from '../../thunks';
import { getSignupForm, getSignupErrors } from '../../selectors/signup';

import ErrorMessage from '../ErrorMessage';
import InputGroup from '../InputGroup';
import Button from '../Button';

const Signup = (props) => (
  <div className="flex flex-column items-center justify-center vh-100 vw-100">
    <div className="w5 flex flex-column">
      {props.errors.general && props.errors.general.map(error =>
        <ErrorMessage message={error} />,
      )}
      <InputGroup
        label="Email"
        value={props.form.email}
        onChange={text => props.updateSignupForm({ email: text })}
        errors={props.errors.email}
      />
      <InputGroup
        label="Password"
        value={props.form.password}
        type="password"
        onChange={text => props.updateSignupForm({ password: text })}
        errors={props.errors.password}
      />
      <InputGroup
        label="Password Confirmation"
        value={props.form.passwordConfirmation}
        type="password"
        onChange={text => props.updateSignupForm({ passwordConfirmation: text })}
        errors={props.errors.passwordConfirmation}
      />

      <Button
        title="Sign up"
        className="mb3 w-100"
        onClick={() => props.signup(props.form)}
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
  signup: PropTypes.func.isRequired,
  updateSignupForm: PropTypes.func.isRequired,
};

const mapStateToProps = state => ({
  form: getSignupForm(state),
  errors: getSignupErrors(state),
});

const mapDispatchToProps = {
  signup,
  updateSignupForm,
};

export default connect(mapStateToProps, mapDispatchToProps)(Signup);
