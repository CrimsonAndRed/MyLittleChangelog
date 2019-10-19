import React, { Component } from 'react';

import VerticalFormField from '../../../form/VerticalFormField';

// Modal for new version settings
// Has props:
// - project - current full project
class RouteNew extends Component {

  constructor(props) {
    super(props);

    this.handleName = this.handleName.bind(this);

    this.state = {
      name: ''
    }
  }

  handleName(e) {
    this.setState({name: e.target.value});
  }

  render() {
    return (
        <form className="login-modal-content" onSubmit={(e) => {e.preventDefault(); this.props.onSubmit(this.state)}}>
          <div className="header mg-top-5 mg-bottom-10">
            Add route
          </div>

          <div className="form-content">
            <div className="login-modal-form-row mg-auto mg-bottom-10">
              <VerticalFormField val={this.state.num} change={this.handleName} error={this.state.duplicateNum} name="Version number"/>
            </div>
            <div className="login-modal-form-row mg-auto"/>
          </div>

          <div className="login-modal-footer login-modal-form-row flex-container">
            <button type="button" className="btn btn-text btn-light-red" onClick={this.props.onClose}>
              Cancel
            </button>
            <input type="submit" value="Submit" className="btn btn-text btn-light-green flex-item-end" onClick={(e) => {e.preventDefault(); this.props.onSubmit(this.state)}}/>            
          </div>
        </form>
    );
  };
}

export default RouteNew;