import React from 'react';
import PropTypes from 'prop-types';

import InfoMessage from '../InfoMessage';
import ErrorMessage from '../ErrorMessage';
import InputGroup from '../InputGroup';
import Button from '../Button';

const AddExpense = props => (
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
      onClick={() => props.login(props.form, props.history)}
    />
  </div>
);

AddExpense.propTypes = {

}

export default AddExpense;
