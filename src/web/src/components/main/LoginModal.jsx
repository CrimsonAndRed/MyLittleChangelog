import React, { Component } from 'react';
import * as qry from '../../services/query';

import { connect } from 'react-redux'
import { set } from '../../redux/actions/UsernameActions'

import VerticalFormField from '../form/VerticalFormField'

// Modal for handling user credentials.
class LoginModal extends Component {

  constructor(props) {
    super(props);

    this.handleLogin = this.handleLogin.bind(this);
    this.handlePassword = this.handlePassword.bind(this);  
    this.sendQry = this.sendQry.bind(this);

    this.state = {
      login: '',
      password: '',
      wrongCredentials: false
    }
  }

  handleLogin(e) {
    this.setState({login: e.target.value});
  }

  handlePassword(e) {
    this.setState({password: e.target.value});
  }

  sendQry(e) {
    qry.post('login', (result) => {
      if (result.data) {
        window.Cookies.set('My-Little-Token', result.data.token, {exires: 1});
        window.Cookies.set('My-Little-Username', result.data.username, {exires: 1});
        this.props.login(result.data.username);
        this.props.onClose();
      } else {
        result.errors.forEach(err => window.toaster.addToast({text: err.text, type: 'error'}));
        this.setState({wrongCredentials: true});
      }
    }, {login: this.state.login, password: this.state.password})
  }

  render() {
    return (
        <div className="login-modal-content">
          <div className="header mg-top-5 mg-bottom-10">
            Sign in
          </div>

          <div className="form-content">
            <div className="login-modal-form-row mg-auto mg-bottom-10">
              <VerticalFormField val={this.state.login} change={this.handleLogin} error={this.state.wrongCredentials} name="Login"/>
            </div>
            <div className="login-modal-form-row mg-auto">
              <VerticalFormField val={this.state.password} change={this.handlePassword} error={this.state.wrongCredentials}  name="Password"/>
            </div>
          </div>

          <div className="login-modal-footer login-modal-form-row flex-container">
            <button className="btn btn-text btn-light-red" onClick={this.props.onClose}>
              Cancel
            </button>
            <button className="btn btn-text btn-light-green flex-item-end" onClick={this.sendQry}>
              Submit
            </button>
          </div>
        </div>
    );
  };
}

export default connect(
    state => ({}),
    dispatch => ({
      login: (username) => dispatch(set(username))
    })
  )(LoginModal);