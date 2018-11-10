import React, { Component } from 'react';
import { Link } from 'react-router-dom'
import { withRouter } from 'react-router-dom'
import Modal from '../util/modal/Modal'
import LoginModal from './LoginModal'

import { connect } from 'react-redux'
import { unset } from '../../redux/actions/UsernameActions'

import * as qry from '../../services/query'

// Menu header on the top of the page
class MenuHeader extends Component {

  constructor(props) {
    super(props);
    this.state = {showLoginModal: false, username: window.username};

    this.showLoginPage = this.showLoginPage.bind(this);
    this.dismissLoginPage = this.dismissLoginPage.bind(this);
    this.userLogin = this.userLogin.bind(this);
    this.signButton = this.signButton.bind(this);
    this.logout = this.logout.bind(this);
  }

  showLoginPage() {
    this.setState({showLoginModal: true})
  }

  dismissLoginPage() {
    this.setState({showLoginModal: false})
  }

  userLogin() {
    return this.props.username;
  }

  logout() {
    qry.post('logout', () => {
      window.Cookies.remove('My-Little-Token');
      window.Cookies.remove('My-Little-Username');
      this.props.logout();
    });
  }

  signButton() {
    if (this.userLogin()) {
      return (
        <div
          className="menu-item as-pointer content-container-5"
          onClick={this.logout}
        >
          <span>
            {'Logout (' + this.userLogin() + ')'}
          </span>
        </div>
      )
    } else {
      return (
        <div 
          className="menu-item as-pointer content-container-5"
          onClick={this.showLoginPage}
        >
          <span>
            Sign in
          </span>
        </div>
      )
    }
  }

  render() {
    let sb = this.signButton();
    
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
          <Link to="/about" className="flex-item-end content-container-5 menu-item">
            <span>
              About
            </span>
          </Link>
          {sb}
        </div>

        { this.state.showLoginModal && (
          <Modal onClose={this.dismissLoginPage} sizeClass="login-modal">
            <LoginModal onClose={this.dismissLoginPage} />
          </Modal>
        )}
      </div>
    );
  };
}

export default connect(
    state => ({
      username: state.usernameReducer.username
    }),
    dispatch => ({
      logout: () => dispatch(unset())
    })
  )(withRouter(MenuHeader));

