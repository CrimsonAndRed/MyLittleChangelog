import axios from 'axios';
// Methods for querying data from backend.
// If there was an error - does nothing (will do smth soon).

// Get query
// path - path for route, without first '/'
// cb - callback to apply after successful response. The only argument for callback - returned data.
export function get(path, cb) {
  let url = process.env.REACT_APP_API_URL + '/' + path;
  axios.get(url)
      .then(res => {
        cb(res.data);
      }
  );
}


// Post query
// path - path for route, without first '/'
// arg - argument for post query.
// cb - callback to apply after successful response. The only argument for callback - returned data.
export function post(path, arg, cb) {
  let url = process.env.REACT_APP_API_URL + '/' + path;
  axios.post(url, arg)
      .then(res => {
        cb(res.data);
      }
  );
}