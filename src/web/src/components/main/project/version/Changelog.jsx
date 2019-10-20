import React, { Component } from 'react';

// Current changelog of version
// Has props:
// - changelog: current changelogs
// - mode: view or edit
class Changelog extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
        <div className="main-centered content-container-5">
          { this.props.changelog.text }
        </div>
    );
  };
}

export default Changelog;