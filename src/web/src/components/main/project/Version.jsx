import React, { Component } from 'react';
import * as qry from '../../../services/query';

class Version extends Component {

  constructor(props) {
    super(props);
    this.state = {version: {routes: []}};
    
    qry.get(`version/${this.props.match.params.id}`, (data) => {
      if (data.errors.length !== 0) {
        data.errors.forEach(error => window.toaster.addToast(error))
      } else {
        console.log(data.data)
        this.setState({version: data.data});
      }
    });
  }

  render() {
    return (
        <div className="main-centered content-container-5">
          <div>
            {this.state.version.num}
          </div>
          <div>
            {this.state.version.description}
          </div>
          <p>Routes:</p>
          <div>
            { this.state.version.routes.map((item, index) => <span key={index}> { item.name } </span>) }
          </div>
        </div>
    );
  };
}

export default Version;