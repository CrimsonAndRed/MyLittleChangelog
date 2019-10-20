import React, { Component } from 'react';

// Route navigation link
// Has props:
// - route: route object
// - index: index in routes array
class RouteNavigationLink extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <div className="as-link mg-left-10" onClick={() => {
            window.location = `#route-${this.props.index}-${this.props.route.name}`
        }}> {this.props.route.name} </div>
    );
  };
}

export default RouteNavigationLink;