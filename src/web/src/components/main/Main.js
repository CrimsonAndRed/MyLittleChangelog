import React, { Component } from 'react';
import * as qry from '../../services/query';

class Main extends Component {

  constructor(props) {
    super(props);
    this.state = {showStrings: [], teststr: ''};
  }

	render() {
		return (
		    <div>
          <div>
            { this.state.showStrings.map(item => <span> { item } </span>) }
          </div>
		      <button onClick= {() => window.toaster.addToast({text: this.state.teststr, type: 'error'})}> add </button>
          <textarea value={ this.state.teststr } onChange={(e) => this.setState({teststr: e.target.value})}/>
		    </div>
		);
	};


	componentDidMount() {
    qry.get('test', (data) => this.setState({showStrings: data}));
  }
}

export default Main;