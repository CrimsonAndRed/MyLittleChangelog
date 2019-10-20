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
          <div className="flex-container">
            <div>
              <p> Route { this.props.route.name } </p>
            </div>
            { this.props.mode === 'edit' && (<div className="flex-item-end">
              <button className="btn btn-text" onClick={() => this.props.addChangelog(this.props.index)} > Add changelog </button>
            </div>
            )}
          </div>
          <p>Changelogs:</p>
          <div>
            { this.props.route.changelogs.map((item, index) => <Changelog key={index} changelog={item} mode={this.props.mode}/>) }
          </div>
          
        </div>
    );
  };
}

export default Route;