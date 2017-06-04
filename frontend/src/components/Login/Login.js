import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import queryString from 'query-string';

import thunks from '../../thunks';
import { getLoginForm, getLoginErrors } from '../../selectors/login';

import InfoMessage from '../InfoMessage';
import ErrorMessage from '../ErrorMessage';
import InputGroup from '../InputGroup';
import Button from '../Button';

const Login = (props) => (
  <div className="flex flex-column items-center justify-center vh-100 vw-100">
    <div className="w5 flex flex-column">
      <InfoMessage message={queryString.parse(props.location.search).message} />
      {props.errors.general && props.errors.general.map(error =>
        <ErrorMessage key={error} message={error} />,
      )}
      <InputGroup
        label="Email"
        value={props.form.email}
        errors={props.errors.email}
        onChange={text => props.updateLoginForm({ email: text })}
      />
      <InputGroup
        label="Password"
        value={props.form.password}
        errors={props.errors.password}
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
  location: PropTypes.shape({
    search: PropTypes.string,
  }).isRequired,
  updateLoginForm: PropTypes.func.isRequired,
};

const mapStateToProps = state => ({
  form: getLoginForm(state),
  errors: getLoginErrors(state),
});

const mapDispatchToProps = {
  login: thunks.login,
  updateLoginForm: thunks.updateLoginForm,
};

export default connect(mapStateToProps, mapDispatchToProps)(Login);
