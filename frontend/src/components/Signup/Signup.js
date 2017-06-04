import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

import thunks from '../../thunks';
import { getSignupForm, getLoading, getSignupErrors } from '../../selectors/signup';

import ErrorMessage from '../ErrorMessage';
import InputGroup from '../InputGroup';
import Button from '../Button';

const Signup = (props) => (
  <div className="flex flex-column items-center justify-center vh-100 vw-100">
    <div className="w5 flex flex-column">
      {props.errors.general && props.errors.general.map(error =>
        <ErrorMessage key={error} message={error} />,
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
        disabled={props.loading}
        onClick={() => props.signup(props.form, props.history)}
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
  loading: PropTypes.bool.isRequired,
  errors: PropTypes.object.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
  signup: PropTypes.func.isRequired,
  updateSignupForm: PropTypes.func.isRequired,
};

const mapStateToProps = state => ({
  form: getSignupForm(state),
  loading: getLoading(state),
  errors: getSignupErrors(state),
});

const mapDispatchToProps = {
  signup: thunks.signup,
  updateSignupForm: thunks.updateSignupForm,
};

export default connect(mapStateToProps, mapDispatchToProps)(Signup);
