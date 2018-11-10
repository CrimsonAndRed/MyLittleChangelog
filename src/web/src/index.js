import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';

// router
import { BrowserRouter } from 'react-router-dom'

// Redux
import { Provider } from 'react-redux';
import store from './redux/store';

// Cookies
import * as c from 'js-cookie';
// How do i do it in a right way???
window.Cookies = c;


// Initial application starter.
// window has global props:
// router - in any cases of manual redirects
// toaster - global toaster
ReactDOM.render(
  <Provider store={store}>
  	<BrowserRouter ref={(router) => {window.router = router}}>
  		<App />
  	</BrowserRouter>
  </Provider>, document.getElementById('root') 
);