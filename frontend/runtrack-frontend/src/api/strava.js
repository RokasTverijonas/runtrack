import api from './client';

export const getConnectUrl = () =>
    api.get('/strava/connect').then((res) => res.data);

export const syncActivities = () =>
    api.post('strava/sync').then((res) => res.data);

export const getActivities = () =>
    api.get('/activities').then((res) => res.data);

export const getStats = () =>
    api.get('/stats').then((res) => res.data);

export const getWeeklyStats = () =>
    api.get('/stats/weekly').then((res) => res.data);