import { createStore } from 'redux'
import usernameReducer from './reducers/UsernameReducer'
import { combineReducers } from 'redux'

const myLittleReducers = combineReducers({
  usernameReducer
});

let store = createStore(myLittleReducers);

export default store;