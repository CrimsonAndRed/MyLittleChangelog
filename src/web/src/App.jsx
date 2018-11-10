import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom';

import ToasterWrapper from './components/util/toast/ToasterWrapper';
import MainRouter from './components/main/MainRouter';

import {connect} from 'react-redux' 
import { set } from './redux/actions/UsernameActions';

import { withRouter } from 'react-router-dom';
 
import './assets/css/main.min.css';

// Route urls to MainRouter component, so they are wrapped with "pretty" menu header and sides align
// Any route, than does not need menu and sides align, goes before <Route path='/'/>
class App extends Component {
  constructor(props) {
    super(props);

    let username = window.Cookies.get('My-Little-Username');
    if (username) {
      this.props.enterCookiesLogin(username);
    }
  }

  render() {
    return (
      <div>
        <ToasterWrapper />
        <Switch>
          <Route path="/" component={MainRouter} />
        </Switch>
      </div>
    );
  };
}

// No idea why withRouter fixes routing after adding redux???
export default withRouter(connect(
    state => ({}),
    dispatch => ({
      enterCookiesLogin: (login) => dispatch(set(login))
    })
  )(App));