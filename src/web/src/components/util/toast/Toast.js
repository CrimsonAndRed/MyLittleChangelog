import React, { Component } from 'react';

// Toast structure:
// id: identifier of toast (automatically set by wrapper)
// text: text of toast
// type: type of toast. Defined in Toast.toastTypes.
// timeout: how many seconds until disappear, -1 is never, null -> 5 seconds.
class Toast extends Component {

  constructor(props) {
    super(props);

    this.handleClick = this.handleClick.bind(this);
    this.toastClass = this.toastClass.bind(this);
  };

  static toastTypes = {
          ERROR: 'error',
          WARNING: 'warning',
          SUCCESS: 'success'
  }

  handleClick(e) {
    this.props.onDiposeClick(this.props.toast);
  };

  toastClass(toast) {
    let toastType;
  
    switch (toast.type) {
      case Toast.toastTypes.ERROR:
        toastType = Toast.toastTypes.ERROR;
        break;
      case Toast.toastTypes.WARNING:
        toastType = Toast.toastTypes.WARNING;
        break;
      case Toast.toastTypes.SUCCESS:
        toastType = Toast.toastTypes.SUCCESS;
        break;
      default:
        toastType = Toast.toastTypes.ERROR;
        break;
    }

    return "toast " + toastType;
  };

  render() {
    return (
        <div 
          className={
            this.toastClass(this.props.toast)
          }  
          onClick= { this.handleClick }
        >
          <div className="several-lines">
            { this.props.toast.text }
          </div>
        </div>
    );
  };
}

export default Toast;
