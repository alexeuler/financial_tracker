export const getDataFromJWT = jwt => {
  const data = jwt.split('.')[1];
  return JSON.parse(window.atob(data));
};
