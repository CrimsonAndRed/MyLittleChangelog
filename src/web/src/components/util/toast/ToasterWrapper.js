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

  // Global id holder.
  // Seems like it does not need syncronize in js?
  static currentIdHolder = 0;

  addToast(toast) {
    toast.id = ++ToasterWrapper.currentIdHolder;
    this.setState((prevState) => ({
      toasts: [...this.state.toasts, toast]
    }));

    if (toast.timeout !== -1) {
      setTimeout(() => this.dispose(toast), toast.timeout * 1000 || 5000);
    }
  };

  dispose(toast) {
    let filteredToasts = _(this.state.toasts).filter((item) => item.id !== toast.id).value();
    if (filteredToasts.length !== this.state.toasts.length) {
      this.setState(prevState => ({
        toasts: filteredToasts
      }));
    }
  };


  render() {
    return (
      <div id='toast-wrapper'>
        { 
          this.state.toasts.map(item => 
            <Toast 
              key = { item.id }
              toast = { item }
              onDiposeClick = {(toast) => this.dispose(toast)}
            />
          ) 
        }
      </div>
    );
  };
}

export default ToasterWrapper;