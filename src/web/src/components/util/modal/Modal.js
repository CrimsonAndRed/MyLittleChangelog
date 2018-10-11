import React, { Component } from 'react';

// Should have property onClose - what to do on modal close
class Modal extends Component {

  render() {
     return (
        <div 
          className="modal-wrapper as-pointer"
          onClick={ this.props.onClose }
        > 
          <div 
            className="modal-window"
            onClick={(e) => {
              e.stopPropagation();
            }} 
          >
            { this.props.children }
          </div>
        </div>
    );
  }
}

export default Modal;