import React, { Component } from 'react';
import * as qry from '../../services/query';

import VerticalFormField from '../form/VerticalFormField'

class LoginModal extends Component {

  constructor(props) {
    super(props);

    this.handleLogin = this.handleLogin.bind(this);
    this.handlePassword = this.handlePassword.bind(this);  
    this.handleSend = this.handleSend.bind(this);

    this.state = {
      login: '',
      password: ''
    }
  }

  handleLogin(e) {
    this.setState({login: e.target.value});
  }

  handlePassword(e) {
    this.setState({password: e.target.value});
  }

  handleSend(e) {
    console.log(this.state);
  }

  render() {
    return (
        <div className="login-modal-content">
          <div className="header mg-top-5 mg-bottom-10">
            Sign in
          </div>

          <div className="form-content">
            <div className="login-modal-form-row mg-auto mg-bottom-10">
              <VerticalFormField val={this.state.login} change={this.handleLogin} error={false} name="Login"/>
            </div>
            <div className="login-modal-form-row mg-auto">
              <VerticalFormField val={this.state.password} change={this.handlePassword} error={false}  name="Password"/>
            </div>
          </div>

          <div className="login-modal-footer login-modal-form-row flex-container">
            <button className="btn btn-text btn-light-red" onClick={this.props.onClose}>
              Cancel
            </button>
            <button className="btn btn-text btn-light-green flex-item-end" onClick={this.handleSend}>
              Submit
            </button>
          </div>
        </div>
    );
  };
}

export default LoginModal;