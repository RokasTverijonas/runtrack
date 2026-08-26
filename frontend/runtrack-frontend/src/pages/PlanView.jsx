import { useEffect, useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { getTrainingPlanWorkouts } from '../api/trainingPlans'

function PlanView() {
  const location = useLocation()
  const plan = location.state?.plan

  const [workouts, setWorkouts] = useState([])
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(!!plan)

  useEffect(() => {
    if (!plan) {
      return
    }

    const loadWorkouts = async () => {
      try {
        const data = await getTrainingPlanWorkouts(plan.id)
        setWorkouts(data)
      } catch (requestError) {
        setError(
          requestError.response?.data?.message ||
          'Unable to load plan workouts.'
        )
      } finally {
        setIsLoading(false)
      }
    }

    loadWorkouts()
  }, [plan])

  if (!plan) {
    return (
      <div className="page">
        <h2>My plan</h2>
        <p>You have not generated a plan in this browser session yet.</p>
        <Link to="/race">Create a training plan</Link>
      </div>
    )
  }

  if (isLoading) {
    return <div className="page"><p>Loading your workouts...</p></div>
  }

  return (
    <div className="page">
      <h2>{plan.raceType} training plan</h2>

      <p><strong>Race date:</strong> {plan.raceDate}</p>
      <p><strong>Status:</strong> {plan.status}</p>
      <p>{plan.planSummary}</p>

      {error && <p className="form-error" role="alert">{error}</p>}

      <h3>Workouts</h3>

      {workouts.length === 0 ? (
        <p>No workouts were generated.</p>
      ) : (
        <ul>
          {workouts.map((workout) => (
            <li key={workout.id}>
              <strong>Week {workout.weekNumber} — {workout.dayOfWeek}</strong>
              <br />
              {workout.workoutType}: {workout.distanceKm} km
              {workout.paceTarget && ` at ${workout.paceTarget}`}
              <br />
              {workout.description}
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}

export default PlanView