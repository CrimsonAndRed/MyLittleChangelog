import React, { Component } from 'react';
import Toast from './Toast';
import _ from 'lodash';

class ToasterWrapper extends Component {

  constructor(props) {
    super(props);
    this.state = {toasts: []};

    this.addToast = this.addToast.bind(this);
    this.dispose = this.dispose.bind(this);
  }

  addToast(text) {
    this.setState(prevState => ({
      toasts: [...this.state.toasts, text]
    }));
  };

  dispose(toast) {
    let filteredToasts = _(this.state.toasts).filter((item) => item !== toast).value();
    this.setState(prevState => ({
      toasts: filteredToasts
    }));
  };


  render() {
    return (
      <div id='toast-wrapper'>
        { 
          this.state.toasts.map(item => 
            <Toast 
              toast={ item }
              onDiposeClick={(toast) => this.dispose(toast)}
            />
          ) 
        }
      </div>
    );
  };
}

export default ToasterWrapper;