import React, { Component, Fragment } from 'react';
import * as qry from '../../../services/query';
import _ from 'lodash';
import Modal from '../../util/modal/Modal'
import VerticalFormField from '../../form/VerticalFormField'

import VersionNew from './version/VersionNew';
import ProjectNavigation from './ProjectNavigation';

class UserProject extends Component {

  constructor(props) {
    super(props);

    this.state = {
      project: {versions: []},
      mode: 'view',
      rollbackCopy: {},
      showAddVersionModal: false
    };
    
    this.editForm = this.editForm.bind(this);
    this.viewForm = this.viewForm.bind(this);
    this.saveForm = this.saveForm.bind(this);
    this.deleteProject = this.deleteProject.bind(this);
    this.addVersion = this.addVersion.bind(this);

    this.init = this.init.bind(this);

    this.handleName = this.handleName.bind(this);
    this.handleDescription = this.handleDescription.bind(this);

    this.dismissAddVersion = this.dismissAddVersion.bind(this);
    this.submitAddVersion = this.submitAddVersion.bind(this);

    this.init();
  }

  init() {
    qry.get(`project/${this.props.match.params.id}/full`, (data) => {
      if (data.errors.length === 0) {
        this.setState({project: data.data});
      } else {
        data.errors.forEach(error => window.toaster.addToast(error));
      }
    });
  }

  editForm() {
    this.setState({mode: 'edit', rollbackCopy: this.state.project});
  }

  viewForm() {
    this.setState({mode: 'view', project: this.state.rollbackCopy});
  }

  saveForm() {
    if (!this.state.project.name) {
      window.toaster.addToast({text: 'Project name is empty'});
    } else {
      qry.put('project', (res) => {
        if (res.errors.length === 0) {
          window.toaster.addToast({text: 'Project updated successfully', type: 'success'});
          this.init();
          this.setState({mode: 'view'});
        } else {
          res.errors.forEach((err) => window.toaster.addToast(err));
        }
      }, this.state.project);
    }
  }

  deleteProject() {
    qry.del('project', (res) => {
        if (res.errors.length === 0) {
          window.toaster.addToast({text: 'Project deleted successfully', type: 'success'});
          this.props.history.push('/projects')
        } else {
          res.errors.forEach((err) => window.toaster.addToast(err));
        }
      }, this.state.project);
  }

  handleName(e) {
    let newProject = _.assign({}, this.state.project,  {name: e.target.value});
    this.setState({project: newProject});
  }

  handleDescription(e) {
    let newProject = _.assign({}, this.state.project,  {description: e.target.value});
    this.setState({project: newProject});
  }

  // Add version modal
  addVersion() {
    this.setState({showAddVersionModal: true});
  }

  dismissAddVersion() {
    this.setState({showAddVersionModal: false});
  }

  submitAddVersion(newVersion) {
     qry.post('version', (data) => {
      if (data.errors.length === 0) {
        let newProject = _.assign({}, this.state.project);
        newProject.versions = [...this.state.project.versions, data.data];
        this.setState({project: newProject});
        this.dismissAddVersion();
        window.toaster.addToast({text: 'Version added successfully', type: 'success'});
      } else {
        data.errors.forEach(error => window.toaster.addToast(error));
      }
    }, {
      num: newVersion.num,
      projectId: this.state.project.id
    });
  }

  render() {
    return (
        <div className="project-container content-container-5">
          { this.state.showAddVersionModal && (
            <Modal onClose={this.dismissAddVersion} sizeClass="login-modal">
              <VersionNew onClose={this.dismissAddVersion} onSubmit={this.submitAddVersion}/>
            </Modal>
          )}
          <ProjectNavigation versions={this.state.project.versions}/>
          <div className="content-container-5 form-content main-centered project-main">
            <div className="new-project-container">
              <div className="new-project-img">
                <img
                  src="https://dummyimage.com/300x300/000/fff&text=Project+Logo"
                  alt="logo"
                />
              </div>

              { this.state.mode === 'edit' && (
                <div className="new-project-fields">
                  <div className="mg-bottom-10">
                    <VerticalFormField val={this.state.project.name} change={this.handleName} error={!this.state.project.name} name="Project name"/>
                  </div>
                  <div className="mg-bottom-5 block mg-left-5">
                    Project description
                  </div>
                  <textarea 
                    value={this.state.project.description} 
                    onChange={this.handleDescription} 
                    className="input-field no-resize" 
                    rows="4" 
                    placeholder="Any information about your project. It is optional..." 
                  />
                </div>
              )}

              { this.state.mode === 'view' && (
                <div className="new-project-fields">
                  <h1>
                    {this.state.project.name}
                  </h1>

                  <div className="several-lines">
                    {this.state.project.description}
                  </div>
                </div>
              )}
            </div>

          </div>
          <div className="project-act content-container-5">
            <button className="btn btn-text btn-light-red" onClick={this.deleteProject} > Delete </button>
            { this.state.mode === 'edit' && (
              <Fragment>
                <button className="btn btn-text btn-light-red" onClick={this.viewForm} > Cancel </button>
                <button className="btn btn-text btn-light-green" onClick={this.saveForm} > Save </button>
              </Fragment>
            )}
            { this.state.mode === 'view' && (
              <Fragment>
                <button className="btn btn-text" onClick={this.editForm} > Edit </button>
              </Fragment>
            )}
            
            <button className="btn btn-text mg-top-10" onClick={this.addVersion}> Add version </button>
            <button className="btn btn-text" > View chain (TODO)</button>
            <button className="btn btn-text" > Download chain (TODO)</button>
          </div>
        </div>
    );
  };
}

export default UserProject;