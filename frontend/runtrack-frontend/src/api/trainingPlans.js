import api from './client'

export const generateTrainingPlan = (raceType, distanceKm, raceDate) =>
    api.post('/training-plans/generate', {
        raceType,
        distanceKm,
        raceDate,
    }).then((res) => res.data)

export const getTrainingPlanWorkouts = (planId) =>
  api.get(`/training-plans/${planId}/workouts`).then((res) => res.data)

export const getCurrentUserTrainingPlans = () =>
    api.get('/training-plans').then((res) => res.data)

export const markWorkoutCompleted = (workoutId) =>
    api.patch('/workouts/${workoutId}/complete').then((res) => res.data)