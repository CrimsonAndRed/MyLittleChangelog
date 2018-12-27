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
  let token = window.Cookies.get('My-Little-Token');
  axios.get(url, {
        headers: {
          'My-Little-Token': token ? token : ''
        }
      })
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
// cb - callback to apply after successful response. The only argument for callback - returned data.
// arg - argument for post query.
export function post(path, cb, arg) {
  let url = process.env.REACT_APP_API_URL + '/' + path;
  let token = window.Cookies.get('My-Little-Token');
  axios.post(url, arg, {
        headers: {
          'My-Little-Token': token ? token : ''
        }
      })
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

// Put query
// Redirects to /login in case of 401 or toasts in case of any other error.
// path - path for route, without first '/'
// cb - callback to apply after successful response. The only argument for callback - returned data.
// arg - argument for post query.
export function put(path, cb, arg) {
  let url = process.env.REACT_APP_API_URL + '/' + path;
  let token = window.Cookies.get('My-Little-Token');
  axios.put(url, arg, {
        headers: {
          'My-Little-Token': token ? token : ''
        }
      })
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
};

// Delete query
// Redirects to /login in case of 401 or toasts in case of any other error.
// path - path for route, without first '/'
// cb - callback to apply after successful response. The only argument for callback - returned data.
// arg - argument for post query.
export function del(path, cb, arg) {
  let url = process.env.REACT_APP_API_URL + '/' + path;
  let token = window.Cookies.get('My-Little-Token');
  axios.delete(url, {
        data: arg,
        headers: {
          'My-Little-Token': token ? token : ''
        }
      })
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
};