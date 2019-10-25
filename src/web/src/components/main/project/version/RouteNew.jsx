import React, { Component } from 'react';

import VerticalFormField from '../../../form/VerticalFormField';

// Modal for new version settings
// Has props:
// - version - current version
class RouteNew extends Component {

  constructor(props) {
    super(props);

    this.handleName = this.handleName.bind(this);

    this.state = {
      name: '',
      suggestedRoutes: this.props.version.routes.filter(i => !i.isVisible)
    }
  }

  handleName(e) {
    let routes = this.props.version.routes;
    this.setState({name: e.target.value, suggestedRoutes: routes.filter(i => !i.isVisible).filter(i => i.name.startsWith(e.target.value))});
  }

  render() {
    return (
        <form className="login-modal-content" onSubmit={(e) => {e.preventDefault(); this.props.onSubmit(this.state)}}>
          <div className="header mg-top-5 mg-bottom-10">
            Add route
          </div>

          <div className="form-content">
            <div className="login-modal-form-row mg-auto mg-bottom-10">
              <VerticalFormField val={this.state.num} change={this.handleName} name="Route name"/>
            </div>
            <div className="login-modal-form-row mg-auto"/>
          </div>
          <div>
            This is "innovative" dropdown list.
          </div>
          <div className="form-content">
            { this.state.suggestedRoutes.map((item, index) => 
            <div className="as-link" key={index} onClick={() => {
              this.props.onSubmit(item.name);
            }}> 
              { item.name } 
            </div>
          )}
          </div>

          <div className="login-modal-footer login-modal-form-row flex-container">
            <button type="button" className="btn btn-text btn-light-red" onClick={this.props.onClose}>
              Cancel
            </button>
            <input type="submit" value="Submit" className="btn btn-text btn-light-green flex-item-end" onClick={(e) => {e.preventDefault(); this.props.onSubmit(this.state.name)}}/>            
          </div>
        </form>
    );
  };
}

export default RouteNew;