import React, { Component } from 'react';
import { withRouter } from "react-router-dom";

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
            Navigation
            <ul>
              { this.props.routes.map((route, index) => <li key={index}> {route.name} </li> )}
            </ul>
        </div>
    );
  };
}

export default withRouter(VersionNavigation);