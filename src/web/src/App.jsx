import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom'

import ToasterWrapper from './components/util/toast/ToasterWrapper';
import MainRouter from './components/main/MainRouter';

import './assets/css/main.min.css';

// Route urls to MainRouter component, so they are wrapped with "pretty" menu header and sides align
// Any route, than does not need menu and sides align, goes before <Route path='/'/>
class App extends Component {
  render() {
    return (
      <div>
        <ToasterWrapper ref={(toaster) => {window.toaster = toaster}} />
        <Switch>
          <Route path="/" component={MainRouter} />
        </Switch>
      </div>
    );
  };
}

export default App;