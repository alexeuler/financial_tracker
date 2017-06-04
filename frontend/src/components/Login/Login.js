import React, { PropTypes } from 'react';

import InputGroup from '../InputGroup';

const Login = (props) => (
  <div className="flex flex-column items-center justify-center vh-100 vw-100">
    <InputGroup label="Login" value="YO" onChange={console.log} />
    <InputGroup label="Password" value="YO" onChange={console.log} />
  </div>
);

Login.propTypes = {

}

export default Login;
