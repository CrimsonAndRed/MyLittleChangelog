import React, { Component } from 'react';
import { withRouter } from "react-router-dom";

// Has props:
// - project : Minimal Project entity
class UserProjectBar extends Component {

  constructor(props) {
    super(props);

    this.click = this.click.bind(this);
  }

  click() {
    this.props.history.push(`/version/${this.props.project.id}`)
  }

  render() {
    return (
        <div className="project-bar" onClick={this.click}>
          <div className="project-bar-img-container">
            <img
              src="https://dummyimage.com/300x300/000/fff&text=Project+Logo"
              alt="logo"
            />
          </div>
          <div className="project-bar-text-container">
            {this.props.project.name} 
          </div>
        </div>
    );
  };
}

export default withRouter(UserProjectBar);