import {SET, UNSET} from './../actions/UsernameActions'

let initialState = { username: null };

const usernameReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET:
      return Object.assign({}, state, { username: action.username });
    case UNSET:
      return state = initialState;
    default:
      return state;
  }
};

export default usernameReducer