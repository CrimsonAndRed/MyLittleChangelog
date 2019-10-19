import React, { Component, Fragment } from 'react';
import * as qry from '../../../../services/query';
import Route from './Route';
import VersionNavigation from './VersionNavigation';

class Version extends Component {

  constructor(props) {
    super(props);
    this.state = {
      version: {routes: []},
      mode: 'view',
      rollbackCopy: {}
    };
    
    qry.get(`version/${this.props.match.params.id}`, (data) => {
      if (data.errors.length !== 0) {
        data.errors.forEach(error => window.toaster.addToast(error))
      } else {
        this.setState({version: data.data});
      }
    });

    this.editVersion = this.editVersion.bind(this);
    this.viewVersion = this.viewVersion.bind(this);
    this.saveVersion = this.saveVersion.bind(this);
    this.deleteVersion = this.deleteVersion.bind(this);
    this.addRoute = this.addRoute.bind(this);
  }

  editVersion() {
    this.setState({mode: 'edit', rollbackCopy: this.state.project});
  }

  viewVersion() {
    this.setState({mode: 'view', project: this.state.rollbackCopy});
  }

  saveVersion() {
    if (!this.state.project.name) {
      window.toaster.addToast({text: 'Project name is empty'});
    } else {
      qry.put('version', (res) => {
        if (res.errors.length === 0) {
          window.toaster.addToast({text: 'Version updated successfully', type: 'success'});
          this.init();
          this.setState({mode: 'view'});
        } else {
          res.errors.forEach((err) => window.toaster.addToast(err));
        }
      }, this.state.version);
    }
  }

  deleteVersion() {
    qry.del('version', (res) => {
        if (res.errors.length === 0) {
          window.toaster.addToast({text: 'Version deleted successfully', type: 'success'});
          this.props.history.push(`/project/${this.state.version.project.id}`)
        } else {
          res.errors.forEach((err) => window.toaster.addToast(err));
        }
      }, this.state.version);
  }

  addRoute() {

    console.log(this.state);
    var dto = {name: '1111', projectId: this.state.version.project.id};

    qry.post('route', (res) => {
      if (res.errors.length === 0) {
        window.toaster.addToast({text: 'Route created successfully', type: 'success'});

        this.setState(prevState => {
          let routesUpd = prevState.version.routes.concat(res.data);
          return {...prevState, version: {...prevState.version, routes: routesUpd}};
        });
      } else {
        res.errors.forEach((err) => window.toaster.addToast(err));
      }
    }, dto);
  }

  render() {
    return (
        <div className="version-container content-container-5">
          <VersionNavigation routes={this.state.version.routes}/>
          <div className="content-container-5 form-content main-centered version-main">
            <div className="header slightly-bigger-text">
              You are {this.state.mode === 'view' ? "viewing" : "editing"} version {this.state.version.num}
            </div>
            <div>
              {this.state.version.description}
            </div>
            <p>Routes:</p>
            <div>
              { this.state.version.routes.map((item, index) => <Route key={index} route={item} />) }
            </div>
          </div>
          <div className="version-act content-container-5">
            <button className="btn btn-text btn-light-red" onClick={this.deleteVersion} > Delete </button>
            { this.state.mode === 'edit' && (
              <Fragment>
                <button className="btn btn-text mg-top-10" onClick={this.addRoute}> Add route </button>
                <button className="btn btn-text btn-light-red" onClick={this.viewVersion} > Cancel </button>
                <button className="btn btn-text btn-light-green" onClick={this.saveVersion} > Save </button>
              </Fragment>
            )}
            { this.state.mode === 'view' && (
              <Fragment>
                <button className="btn btn-text" onClick={this.editVersion} > Edit </button>
              </Fragment>
            )}
          </div>
        </div>
    );
  };
}

export default Version;