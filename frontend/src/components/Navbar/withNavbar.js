import React from 'react';

import Navbar from './Navbar';

export default Component => props =>
  <div>
    <Navbar match={props.match} />
    <Component {...props} />
  </div>