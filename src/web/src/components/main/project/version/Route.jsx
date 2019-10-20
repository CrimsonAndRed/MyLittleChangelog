import React, { Component } from 'react';
import Changelog from './Changelog'

// Current route of version
// Has props:
// - route: current route
// - mode: view or edit
// - addChangelog: function to update version state
// - index: index of route in routes array
class Route extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <div className="content-container-5 route mg-bottom-10" id={`route-${this.props.index}-${this.props.route.name}`} >
          <p> Route { this.props.route.name } </p>
          <p>Changelogs:</p>
          <div>
            { this.props.route.changelogs.map((item, index) => <Changelog key={index} changelog={item} mode={this.props.mode}/>) }
          </div>
          <button className="btn btn-text" onClick={() => this.props.addChangelog(this.props.index)} > Add changelog </button>
        </div>
    );
  };
}

export default Route;