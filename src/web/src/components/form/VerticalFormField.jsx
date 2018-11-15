import React, { Component } from 'react';

// Vertical for is an <text> with <input> under it
// Has properties: 
// error - red border around field
// name - name of field (above field)
class VerticalFormField extends Component {

  constructor(props) {
    super(props);

    this.errorBorder = this.errorBorder.bind(this);
  }

  errorBorder() {
    if (this.props.error) {
      return 'input-field error'
    } else {
      return 'input-field'
    }
  }  

  render() {
    return (
        <div>
          <div className="mg-bottom-5 block mg-left-5">
            { this.props.name }
          </div>
          <input type="text" value={this.props.val} onChange={this.props.change} className={this.errorBorder()} />
        </div>
    );
  };
}

export default VerticalFormField;