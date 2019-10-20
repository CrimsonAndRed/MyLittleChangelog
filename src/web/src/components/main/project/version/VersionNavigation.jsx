import React, { Component } from 'react';
import { withRouter } from "react-router-dom";
import RouteNavigationLink from './RouteNavigationLink';

// Navigation bar
// Has props:
// - routes
class VersionNavigation extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <div className="version-nav">
          <div className="header mg-bottom-10">
            Navigation
          </div>
          { this.props.routes.map((route, index) => <RouteNavigationLink key={index} route={route} index={index}/> )}
        </div>
    );
  };
}

export default withRouter(VersionNavigation);