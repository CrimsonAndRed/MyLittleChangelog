import React, { Component } from 'react';
import * as qry from '../../../services/query';

import { Link } from 'react-router-dom';

class UserProject extends Component {

  constructor(props) {
    super(props);
    this.state = {project: {versions: []}};
    
    qry.get(`project/full/${this.props.match.params.id}`, (data) => {
      if (data.errors.length !== 0) {
        data.errors.forEach(error => window.toaster.addToast(error))
      } else {
        this.setState({project: data.data});
      }
    });
  }

  render() {
    return (
        <div className="main-centered content-container-5">
          <div>
            {this.state.project.name}
          </div>
          <div>
            {this.state.project.description}
          </div>
          <p>Versions:</p>
          <div>
            { this.state.project.versions.map((item, index) => <Link key={index} to={`/version/${item.id}`} replace> { item.num } </Link>) }
          </div>
        </div>
    );
  };
}

export default UserProject;