import React, { Component, Fragment } from 'react';
import * as qry from '../../../../services/query';
import Route from './Route';
import VersionNavigation from './VersionNavigation';
import Modal from '../../../util/modal/Modal'
import RouteNew from './RouteNew'
import _ from 'lodash';

class Version extends Component {

  constructor(props) {
    super(props);
    this.state = {
      version: {routes: []},
      mode: 'view',
      rollbackCopy: {},
      showAddRouteModal: false
    };
  
    this.init = this.init.bind(this);

    this.saveVersion = this.saveVersion.bind(this);
    this.editVersion = this.editVersion.bind(this);
    this.viewVersion = this.viewVersion.bind(this);
    this.saveVersion = this.saveVersion.bind(this);
    this.deleteVersion = this.deleteVersion.bind(this);
    this.dismissAddRoute = this.dismissAddRoute.bind(this);
    this.submitAddRoute = this.submitAddRoute.bind(this);
    this.addRoute = this.addRoute.bind(this);
    this.addChangelog = this.addChangelog.bind(this);
    this.hideUnused = this.hideUnused.bind(this);
    this.init();
  }

  init() {
    qry.get(`version/${this.props.match.params.id}`, (data) => {
      if (data.errors.length !== 0) {
        data.errors.forEach(error => window.toaster.addToast(error))
      } else {
        data.data.routes.forEach(r => r.isVisible = r.changelogs.length > 0);
        this.setState({version: data.data});
      }
    });
  }

  saveVersion() {
    qry.put(`version/${this.state.version.id}`, (res) => {
      if (res.errors.length === 0) {
        window.toaster.addToast({text: 'Version updated successfully', type: 'success'});
        this.init();
        this.setState({mode: 'view'});
      } else {
        res.errors.forEach((err) => window.toaster.addToast(err));
      }
    }, this.state.version);
  }


  editVersion() {
    this.setState({mode: 'edit', rollbackCopy: _.cloneDeep(this.state.version)});
  }

  viewVersion() {
    this.setState({mode: 'view', version: this.state.rollbackCopy});
  }

  deleteVersion() {
    qry.del(`version/${this.state.version.id}`, (res) => {
        if (res.errors.length === 0) {
          window.toaster.addToast({text: 'Version deleted successfully', type: 'success'});
          this.props.history.push(`/project/${this.state.version.project.id}`)
        } else {
          res.errors.forEach((err) => window.toaster.addToast(err));
        }
      }, this.state.version);
  }

  addRoute() {
    this.setState({showAddRouteModal: true});
  }

  hideUnused() {
    this.setState(prevState => {
      prevState.version.routes.forEach(r => r.isVisible = r.changelogs.length > 0);
      return prevState;
    });
  }

  dismissAddRoute() {
    this.setState({showAddRouteModal: false});
  }

  submitAddRoute(st) {


    let toShow = _.findIndex(this.state.version.routes, {name: st});
    if (toShow >= 0) {
      this.setState(prevState => {
        prevState.version.routes[toShow].isVisible = true;
        return {...prevState, showAddRouteModal: false};
      });
    } else {
      var dto = {name: st, projectId: this.state.version.project.id};

      qry.post('route', (res) => {
        if (res.errors.length === 0) {
          window.toaster.addToast({text: 'Route created successfully', type: 'success'});

          res.data.isVisible = true;
          this.setState(prevState => {
            let routesUpd = prevState.version.routes.concat(res.data);
            return {version: {...prevState.version, routes: routesUpd}, showAddRouteModal: false};
          });
        } else {
          res.errors.forEach((err) => window.toaster.addToast(err));
        }
      }, dto);
    }
  }

  addChangelog(index) {
    this.setState(prevState => {
      let routesUpd = prevState.version.routes; 
      routesUpd[index].changelogs = routesUpd[index].changelogs.concat({text: 'aaaa'});
      return {version: {...prevState.version, routes: routesUpd}, showAddRouteModal: false};
    });
  }

  render() {
    return (
        <div className="version-container content-container-5">
          { this.state.showAddRouteModal && (
            <Modal onClose={this.dismissAddRoute} sizeClass="login-modal">
              <RouteNew version={this.state.version} onClose={this.dismissAddRoute} onSubmit={this.submitAddRoute}/>
            </Modal>
          )}
          <VersionNavigation routes={this.state.version.routes}/>
          <div className="content-container-5 form-content main-centered version-main">
            <div className="header slightly-bigger-text mg-bottom-10">
              You are {this.state.mode === 'view' ? "viewing" : "editing"} version {this.state.version.num}
            </div>
            <div>
              {this.state.version.description}
            </div>
            <div>
              { this.state.mode === 'view' && <div className="mg-top-10 mg-bottom-10">
                  <button className="btn btn-text" onClick={this.addRoute}> Add route </button>
                  <button className="btn btn-text" onClick={this.hideUnused}> Hide unused </button>
                </div>
              }
              { this.state.version.routes.filter(i => i.isVisible).map((item, index) => 
                <Route key={index} 
                    route={item} 
                    mode={this.state.mode} 
                    addChangelog={this.addChangelog}
                    index={index}
                />
              )}
            </div>
          </div>
          <div className="version-act content-container-5">
            <button className="btn btn-text btn-light-red" onClick={this.deleteVersion} > Delete </button>
            { this.state.mode === 'edit' && (
              <Fragment>
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