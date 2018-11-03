import axios from 'axios';
import Toast from '../components/util/toast/Toast';
// Methods for querying data from backend.
// If there was an error - generates toast

// Get query
// Redirects to /login in case of 401 or toasts in case of any other error.
// path - path for route, without first '/'
// cb - callback to apply after successful response. The only argument for callback - returned data.
export function get(path, cb) {
  let url = process.env.REACT_APP_API_URL + '/' + path;
  axios.get(url)
      .then(res => {
        cb(res.data);
      }, err => {
        let msg = '';
        if (err.response) {
          if (err.response.status === 401) {
            window.router.history.push('/login');
            return;
          }

          msg = 'Code: ' + err.response.status + '\nError: ' + err.response.data.exceptionName + ' - ' + (err.response.data.errorMessage || '');
        } else {
          msg = 'Could not create request to url: ' + url;
        }
        window.toaster.addToast({text: msg, type: Toast.toastTypes.ERROR, timeout: -1});
      })
      .catch((cbError) => {
        console.error(cbError);
        window.toaster.addToast({text: cbError, type: Toast.toastTypes.ERROR, timeout: -1});
      });
}


// Post query
// Redirects to /login in case of 401 or toasts in case of any other error.
// path - path for route, without first '/'
// arg - argument for post query.
// cb - callback to apply after successful response. The only argument for callback - returned data.
export function post(path, arg, cb) {
  let url = process.env.REACT_APP_API_URL + '/' + path;
  axios.post(url, arg)
      .then(res => {
        cb(res.data);
      }, err => {
        let msg = '';
        if (err.response) {
          if (err.response.status === 401) {
            window.router.history.push('/login');
            return;
          }

          msg = 'Code: ' + err.response.status + '\nError: ' + err.response.data.exceptionName + ' - ' + (err.response.data.errorMessage || '');
        } else if (err.request) {
          msg = 'Could not perform request to ' + err.request.responseURL;
        } else {
          msg = 'Could not create requset to server';
        }
        window.toaster.addToast({text: msg, type: Toast.toastTypes.ERROR});
      })
      .catch(cbError => {
        console.error(cbError);
        window.toaster.addToast({text: cbError, type: Toast.toastTypes.ERROR, timeout: -1});
      })
}