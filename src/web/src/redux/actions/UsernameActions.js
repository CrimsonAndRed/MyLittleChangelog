export const SET = 'SET'
export const UNSET = 'UNSET'

export const set = (username) => {
  return {
    type: SET,
    username
  }
}

export const unset = () => {
  return {
    type: UNSET
  }
}