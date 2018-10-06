import React, { Component } from 'react';
import axios from 'axios';

class Main extends Component {

  constructor(props) {
    super(props);
    this.state = {showStrings: []};
  }

	render() {
		return (
		    <div>
		      { this.state.showStrings.map(item => <p> { item } </p>) }
		    </div>
		);
	};

	componentDidMount() {
		axios.get(`http://localhost:9000/test`)
  		.then(res => {
        console.log(res.data)
        this.setState({showStrings: res.data});
      });
  }
}

export default Main;