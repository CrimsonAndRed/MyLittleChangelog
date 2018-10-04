import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom'

import _ from 'lodash';
import MainRouter from './components/main/MainRouter';

import './assets/css/main.min.css';

// Route urls to MainRouter component, so they are wrapped with "pretty" menu header and sides align
// Any route, than does not need menu and sides align, goes before <Route path='/'/>
class App extends Component {
  render() {
    return (
        <Switch>
          <Route path='/' component={MainRouter}/>
        </Switch>
    );
  };

  componentDidMount(){
  	console.log(_);
  }
}

export default App;