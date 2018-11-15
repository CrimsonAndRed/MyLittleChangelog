import React, { Component } from 'react';
import * as qry from '../../../services/query';
import UserProjectBar from './UserProjectBar'
import UserProjectBarStub from './UserProjectBarStub'

class UserProjects extends Component {

  constructor(props) {
    super(props);
    this.state = {projects: []};
    
    qry.get('project/my', (data) => {
      this.setState({projects: data})
    });
  }

  render() {
    return (
        <div className="main-centered content-container-5">
          <div className="projects-container">
            {this.state.projects.map((item, index) => <UserProjectBar key={item.id} project={item} />)}
            <UserProjectBarStub />
          </div>
        </div>
    );
  };
}

export default UserProjects;