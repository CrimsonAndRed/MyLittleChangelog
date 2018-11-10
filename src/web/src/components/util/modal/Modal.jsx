import React, { Component } from 'react';

// Has props:
// onClose - anything this modal should do on "outside area" click.
class Modal extends Component {

  constructor(props) {
    super(props);

    this.styleModal = this.styleModal.bind(this);
  }

  styleModal() {
    let className = 'modal-window content-container-5 relative ';
    if (this.props.sizeClass) {
      className += this.props.sizeClass;
    } else {
      className += 'default-size';
    }
    return className;
  }

  render() {
     return (
        <div 
          className="modal-wrapper as-pointer"
          onClick={this.props.onClose}
        > 
          <div 
            className={this.styleModal()}
            onClick={(e) => {
              e.stopPropagation();
            }} 
          >
            {this.props.children}
          </div>
        </div>
    );
  }
}

export default Modal;