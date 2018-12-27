import React, { Component } from 'react';

import VerticalFormField from '../../form/VerticalFormField'

import { withRouter } from "react-router-dom";
import * as qry from '../../../services/query';


// Has props:
// - project : Minimal Project entity
class UserProjectNew extends Component {

  constructor(props) {
    super(props);

    this.state = {
      name: '',
      description: ''
    };

    this.handleName = this.handleName.bind(this);
    this.handleDescription = this.handleDescription.bind(this);

    this.saveForm = this.saveForm.bind(this);
    this.dismissForm = this.dismissForm.bind(this);
  }

  saveForm() {
    if (!this.state.name) {
      window.toaster.addToast({text: 'Project name is empty'});
    } else {
      qry.post('project', (res) => {
        if (res.errors.length === 0) {
          window.toaster.addToast({text: 'Project created successfully', type: 'success'});
          this.props.history.push('/projects');
        } else {
          res.errors.forEach((err) => window.toaster.addToast(err));
        }
        
      }, {
        name: this.state.name,
        description: this.state.description
      });
    }
  }

  dismissForm() {
    this.props.history.goBack()
  }

  handleName(e) {
    this.setState({name: e.target.value});
  }

  handleDescription(e) {
    this.setState({description: e.target.value});
  }

  render() {
    return (
        <div className="main-centered content-container-5 form-content">
          <div className="new-project-container">
            <div className="new-project-img">
              <img
                src="https://dummyimage.com/300x300/000/fff&text=Project+Logo"
                alt="logo"
              />
            </div>
            <div className="new-project-fields">
              <div className="mg-bottom-10">
                <VerticalFormField val={this.state.name} change={this.handleName} error={!this.state.name} name="Project name"/>
              </div>
              <div className="mg-bottom-5 block mg-left-5">
                Project description
              </div>
              <textarea 
                value={this.state.description} 
                onChange={this.handleDescription} 
                className="input-field no-resize" 
                rows="4" 
                placeholder="Any information about your project. It is optional..." 
              />
              <div className="mg-top-10">
                <button className="btn btn-text btn-light-red" onClick={this.dismissForm} > Cancel </button>
                <button className="btn btn-text btn-light-green mg-left-5" onClick={this.saveForm} > Save </button>
              </div>
            </div>
          </div>
        </div>
    );
  };
}

export default withRouter(UserProjectNew);