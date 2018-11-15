import React, { Component } from 'react';
import { withRouter } from "react-router-dom";


// Has props:
// - project : Minimal Project entity
class UserProjectBarStub extends Component {

  constructor(props) {
    super(props);

    this.click = this.click.bind(this);
  }

  click() {
    this.props.history.push(`/project/new`);
  }

  render() {
    return (
        <div className="project-bar" onClick={this.click}>
          <div className="project-bar-img-container">
            <img
              src="https://dummyimage.com/300x300/000/fff&text=Add+Project"
              alt="logo"
            />
          </div>
          <div className="project-bar-text-container">
            Add new project... 
          </div>
        </div>
    );
  };
}

export default withRouter(UserProjectBarStub);