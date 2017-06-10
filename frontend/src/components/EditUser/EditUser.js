import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import ErrorMessage from '../ErrorMessage';
import InputGroup from '../InputGroup';
import Button from '../Button';

import thunks from '../../thunks';
import { getUsersForm, getUsersErrors, getUsersLoading, getEditingFocus } from '../../selectors/users';

const EditUser = (props) => {
  if (props.hidden) return null;
  return (
    <div className="flex flex-column pa3 ba b--light-gray mh2-l mv2 mv0-l w-100 w5-l">
      {props.errors.general && props.errors.general.map(error =>
        <ErrorMessage key={error} message={error} />,
      )}
      { !props.edit && <InputGroup
        label="Email"
        value={props.form.email}
        errors={props.errors.email}
        small
        onChange={text => props.updateFormUsers({ email: text })}
      />}
      <InputGroup
        label="Password"
        value={props.form.password}
        errors={props.errors.password}
        type="password"
        small
        onChange={text => props.updateFormUsers({ password: text })}
      />
      {props.edit && <InputGroup
        label="Role"
        value={props.form.role}
        errors={props.errors.role}
        type="select"
        options={['User', 'Manager', 'Admin']}
        small
        onChange={text => props.updateFormUsers({ role: text })}
      />}
      <Button
        title={props.submitTitle}
        className="mb3 w-100"
        color="green"
        disabled={props.loading}
        onClick={() => {
          if (props.userId) {
            props.updateUser(props.userId, props.history);
          } else {
            props.createUser(props.history);
          }
        }}
      />
    </div>
  );
};

EditUser.propTypes = {
  form: PropTypes.shape({
    email: PropTypes.string.isRequired,
    password: PropTypes.string.isRequired,
    role: PropTypes.string.isRequired,
  }),
  errors: PropTypes.object.isRequired,
  loading: PropTypes.bool.isRequired,
  hidden: PropTypes.bool,
  submitTitle: PropTypes.string.isRequired,
  edit: PropTypes.bool.isRequired,
  userId: PropTypes.number,
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
  updateFormUsers: PropTypes.func.isRequired,
  createUser: PropTypes.func.isRequired,
  updateUser: PropTypes.func.isRequired,
};

const mapStateToProps = state => ({
  form: getUsersForm(state),
  errors: getUsersErrors(state),
  loading: getUsersLoading(state),
  userId: getEditingFocus(state),
});

const mapDispatchToProps = {
  updateFormUsers: thunks.updateFormUsers,
  createUser: thunks.createUser,
  updateUser: thunks.updateUser,
};

export default connect(mapStateToProps, mapDispatchToProps)(EditUser);
