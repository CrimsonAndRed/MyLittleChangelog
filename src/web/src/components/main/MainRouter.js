import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom'

import Main from './Main';
import Login from './Login';
import NotFound from './NotFound';
import MenuHeader from './MenuHeader';

class MainRouter extends Component {
  render() {
    return (
    	<div id="main">
    		<MenuHeader/>
	    	<div id="main-body">
		        <Switch>
		          <Route exact path='/' component={Main}/>
		          <Route exact path='/login' component={Login}/>
		          <Route component={NotFound}/>
		        </Switch>
	        </div>
        </div>
    );
  };
}

export default MainRouter;