import React, { Component } from 'react';
import { Link } from 'react-router-dom'
import { withRouter } from 'react-router-dom'
import Modal from '../util/modal/Modal'
import LoginModal from './LoginModal'

  

class MenuHeader extends Component {

  constructor(props) {
    super(props);
    this.state = {showLoginModal: false};

    this.showLoginPage = this.showLoginPage.bind(this);
    this.dismissLoginPage = this.dismissLoginPage.bind(this);
  }

  showLoginPage() {
    this.setState({showLoginModal: true})
  }

  dismissLoginPage() {
    this.setState({showLoginModal: false})
  }

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
          <Link to="/about" className="flex-item-end menu-item">
            <span>
              About
            </span>
          </Link>
          <a 
            className="menu-item as-pointer"
            onClick= { this.showLoginPage }
          >
            <span>
              Login
            </span>
          </a>
        </div>


        { this.state.showLoginModal && (
          <Modal onClose={this.dismissLoginPage} sizeClass="login-modal">
            <LoginModal onClose={this.dismissLoginPage}/>
          </Modal>
        )}
      </div>
    );
  };
}

export default withRouter(MenuHeader);
