import React, { Component } from 'react';
import { withRouter } from "react-router-dom";

// Navigation bar
// Has props:
// - version
class NavigationLink extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <div className="as-link mg-left-10" onClick={() => this.props.history.push(`/version/${this.props.version.id}`)}> {this.props.version.num} </div>
    );
  };
}

export default withRouter(NavigationLink);