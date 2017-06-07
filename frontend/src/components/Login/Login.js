import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import queryString from 'query-string';

import thunks from '../../thunks';
import { getLoginForm, getLoginErrors, getLoginLoading } from '../../selectors/login';

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
        disabled={props.loading}
        onClick={() => !props.loading && props.login(props.form, props.history)}
      />
    </div>
  </div>
);

Login.propTypes = {
  form: PropTypes.shape({
    email: PropTypes.string,
    password: PropTypes.string,
  }).isRequired,
  errors: PropTypes.object.isRequired,
  loading: PropTypes.bool.isRequired,
  login: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
  location: PropTypes.shape({
    search: PropTypes.string,
  }).isRequired,
  updateLoginForm: PropTypes.func.isRequired,
};

const mapStateToProps = state => ({
  form: getLoginForm(state),
  errors: getLoginErrors(state),
  loading: getLoginLoading(state),
});

const mapDispatchToProps = {
  login: thunks.login,
  updateLoginForm: thunks.updateLoginForm,
};

export default connect(mapStateToProps, mapDispatchToProps)(Login);
