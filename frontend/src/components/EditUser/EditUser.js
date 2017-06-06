import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import ErrorMessage from '../ErrorMessage';
import InputGroup from '../InputGroup';
import Button from '../Button';

import thunks from '../../thunks';
import { getUsersForm, getUsersErrors, getUsersLoading, getEditingFocus } from '../../selectors/users';

const AddUser = (props) => {
  if (props.hidden) return null;
  return (
    <div className="flex flex-column pa3 ba b--light-gray mw6">
      {props.errors.general && props.errors.general.map(error =>
        <ErrorMessage key={error} message={error} />,
      )}
      <InputGroup
        label="Email"
        value={props.form.identity}
        errors={props.errors.identity}
        small
        onChange={text => props.updateFormUsers({ description: text })}
      />
      <Button
        title={props.submitTitle}
        className="mb3 w-100"
        color="green"
        disabled={props.loading}
        onClick={() => {
          if (props.userId) {
            props.updateUser(props.match.params.userId, props.userId, props.history);
          } else {
            props.createUser(props.match.params.userId, props.history);
          }
        }}
      />
    </div>
  );
};

AddUser.propTypes = {
  form: PropTypes.shape({
    occuredAt: PropTypes.string,
    amount: PropTypes.string.isRequired,
    description: PropTypes.string.isRequired,
    comment: PropTypes.string.isRequired,
  }),
  match: PropTypes.shape({
    params: PropTypes.shape({
      userId: PropTypes.string.isRequired,
    }).isRequired,
  }).isRequired,
  errors: PropTypes.object.isRequired,
  loading: PropTypes.bool.isRequired,
  hidden: PropTypes.bool,
  submitTitle: PropTypes.string.isRequired,
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

export default connect(mapStateToProps, mapDispatchToProps)(AddUser);
