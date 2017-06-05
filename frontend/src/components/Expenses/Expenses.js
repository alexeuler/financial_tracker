import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import { getSessionState } from '../selectors/session';

class Expenses extends React.Component {

  componentDidMount() {
    
  }

  render() {
    return (
      <div>

      </div>
    )
  }
}

Expenses.propTypes = {

};

const mapStateToProps = state => ({
  session: getSessionState(state),
});

export default connect(mapStateToProps, null)(Expenses);
