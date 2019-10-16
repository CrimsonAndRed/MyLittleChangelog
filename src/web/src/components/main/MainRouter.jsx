import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom'

import Main from './Main';
import About from './About';
import NotFound from './NotFound';
import MenuHeader from './MenuHeader';
import UserProjects from './project/UserProjects';
import UserProject from './project/UserProject';
import UserProjectNew from './project/UserProjectNew';
import Version from './project/version/Version';

// Main router
// Add routes to <Switch> block to make them wrapped in header-wrap.
class MainRouter extends Component {
  render() {
    return (
    	<div id="main">
    		<MenuHeader/>
	    	<div>
		      <Switch>
	          <Route exact path='/' component={Main}/>
	          <Route exact path='/about' component={About}/>
            <Route exact path='/projects' component={UserProjects}/>
            <Route exact path='/project/new' component={UserProjectNew}/>
            <Route exact path='/project/:id' component={UserProject}/>
            <Route exact path='/version/:id' component={Version}/>
	          <Route component={NotFound}/>
	        </Switch>
        </div>
      </div>
    );
  };
}

export default MainRouter;