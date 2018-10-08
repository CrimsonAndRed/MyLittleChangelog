import React, { Component } from 'react';

// Toast structure:
// text: text of toast
// type: color of toast
// timeout: how many seconds until disappear
class Toast extends Component {

  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
  }

  handleClick(e) {
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