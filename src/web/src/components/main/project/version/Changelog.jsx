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
          <div>
            { this.props.changelog.text }
          </div>
          {this.props.mode === 'edit' && this.props.changelog.id == this.props.changelog.vid && (
          <div>
            <button className="btn btn-text" onClick={this.props.onChangelogDelete} > Delete changelog </button>
          </div>
          )}
        </div>
    );
  };
}

export default Changelog;