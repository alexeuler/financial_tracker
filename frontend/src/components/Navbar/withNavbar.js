import React from 'react';

import Navbar from './Navbar';

export default Component => props =>
  <div>
    <Navbar />
    <Component {...props} />
  </div>