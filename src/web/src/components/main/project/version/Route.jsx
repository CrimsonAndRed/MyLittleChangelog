import React, { Component } from 'react';
import Changelog from './Changelog'

// Current route of version
// Has props:
// - route: current route
class Route extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <div className="main-centered content-container-5">
          <p>Changelogs:</p>
          <div>
            { this.props.route.changelogs.map((item, index) => <Changelog key={index} changelog={item} />) }
          </div>
        </div>
    );
  };
}

export default Route;