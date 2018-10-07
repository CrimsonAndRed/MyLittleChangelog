import React, { Component } from 'react';

// Toast structure:
// text: text of toast
// type: color of toast
class Toast extends Component {

  handleClick = (e) => {
    this.props.onDiposeClick(this.props.toast);
  };

  render() {
    return (
        <div 
          className="toast"
          onClick= { this.handleClick }
        >
          { this.props.toast.text }
        </div>
    );
  };
}

export default Toast;