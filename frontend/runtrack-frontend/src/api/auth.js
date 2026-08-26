import api from './client';

export const login = (email, password) =>
    api.post('/auth/login', { email, password}).then((res) => res.data);


export const register = (name, email, password) =>
    api.post('/auth/register', { name, email, password }).then((res) => res.data);

export const getCurrentUser = () =>
    api.get('/users/me').then((res) => res.data);
