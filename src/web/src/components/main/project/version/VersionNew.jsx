import React, { Component } from 'react';

import VerticalFormField from '../../../form/VerticalFormField';

// Modal for new version settings
// Has props:
// - project - current full project
class VersionNew extends Component {

  constructor(props) {
    super(props);

    this.handleNum = this.handleNum.bind(this);

    this.state = {
      num: ''
    }
  }

  handleNum(e) {
    this.setState({num: e.target.value});
  }

  render() {
    return (
        <form className="login-modal-content" onSubmit={(e) => {e.preventDefault(); this.props.onSubmit(this.state)}}>
          <div className="header mg-top-5 mg-bottom-10">
            Add version
          </div>

          <div className="form-content">
            <div className="login-modal-form-row mg-auto mg-bottom-10">
              <VerticalFormField val={this.state.num} change={this.handleNum} error={this.state.duplicateNum} name="Version number"/>
            </div>
            <div className="login-modal-form-row mg-auto">

            </div>
          </div>

          <div className="login-modal-footer login-modal-form-row flex-container">
            <button type="button" className="btn btn-text btn-light-red" onClick={this.props.onClose}>
              Cancel
            </button>
            <input type="submit" value="Submit" className="btn btn-text btn-light-green flex-item-end" onClick={(e) => {e.preventDefault(); this.props.onSubmit(this.state)}}>
              
            </input>
          </div>
        </form>
    );
  };
}

export default VersionNew;