import React, { Component } from 'react';
import { Link } from 'react-router-dom'
import { withRouter } from 'react-router-dom'

  

class MenuHeader extends Component {
  render() {
    return (
      <div id="main-header">
        <div id="header-continer" className="flex-container content-container-5">
          <div id="logo">
            <Link to='/'>
              <img
                src="https://dummyimage.com/100x40/000/fff&text=My+Little+Logo" 
                alt="logo"
              />
            </Link>
          </div>
          <Link to='/about' className="flex-item-end menu-item">
            <span>
              About
            </span>
          </Link>
          <Link to='/login' className="menu-item">
            <span>
              Login
            </span>
          </Link>
        </div>
      </div>
    );
  };
}

export default withRouter(MenuHeader);
