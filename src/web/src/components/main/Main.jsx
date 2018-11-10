import React, { Component } from 'react';
import * as qry from '../../services/query';
import Modal from '../util/modal/Modal';

// Main class for initial page ('/' route) 
class Main extends Component {

  constructor(props) {
    super(props);
    this.state = {showStrings: [], teststr: '', testModal: false};
    
    this.getError = this.getError.bind(this);
    this.openModal = this.openModal.bind(this);
    this.closeModal = this.closeModal.bind(this);
  }

  getError() {
    qry.get('exception', (data) => {});
  }

  openModal() {
    this.setState({ testModal: true });
  }

  closeModal() {
    this.setState({ testModal: false });
  }

	render() {
		return (
		    <div>
          <div>
            { this.state.showStrings.map(item => <span> { item } </span>) }
          </div>
		      <button onClick={() => window.toaster.addToast({text: this.state.teststr, type: 'error'})}> add </button>
          <button onClick={this.getError}> giv error </button>
          <button onClick={this.openModal}> giv modal </button>
          <textarea value={this.state.teststr} onChange={(e) => this.setState({teststr: e.target.value})} />

          { this.state.testModal && (
            <Modal onClose={this.closeModal}>
              <p> sdssadsds </p>
            </Modal>
          )}
		    </div>
		);
	};


	componentDidMount() {
    qry.get('test', (data) => this.setState({showStrings: data}));
  }
}

export default Main;