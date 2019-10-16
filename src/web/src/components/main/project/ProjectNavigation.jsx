import React, { Component } from 'react';
import { withRouter } from "react-router-dom";
import NavigationLink from "./NavigationLink";

// Navigation bar
// Has props:
// - versions
class ProjectNavigation extends Component {

  constructor(props) {
    super(props);
    this.state = {backup: this.props.versions};
  }

  render() {
    return (
        <div className="project-nav">
          <div className="header mg-bottom-10">
            Navigation
          </div>
            { this.props.versions.map(vers => <NavigationLink key={vers.id} version={vers} /> )}
        </div>
    );
  };
}

export default withRouter(ProjectNavigation);