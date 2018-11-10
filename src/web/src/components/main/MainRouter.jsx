import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom'

import Main from './Main';
import About from './About';
import NotFound from './NotFound';
import MenuHeader from './MenuHeader';

// Main router
// Add routes to <Switch> block to make them wrapped in header-wrap.
class MainRouter extends Component {
  render() {
    return (
    	<div id="main">
    		<MenuHeader/>
	    	<div id="main-body">
		      <Switch>
	          <Route exact path='/' component={Main}/>
	          <Route exact path='/about' component={About}/>
	          <Route component={NotFound}/>
	        </Switch>
        </div>
      </div>
    );
  };
}

export default MainRouter;