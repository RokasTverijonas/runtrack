import { useEffect, useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { getTrainingPlanWorkouts } from '../api/trainingPlans'

const DAY_INDEX = {
  Monday: 0,
  Tuesday: 1,
  Wednesday: 2,
  Thursday: 3,
  Friday: 4,
  Saturday: 5,
  Sunday: 6,
}

const WORKOUT_TYPES = [
  { match: /rest/i, key: 'rest', label: 'Rest' },
  { match: /interval/i, key: 'interval', label: 'Interval' },
  { match: /tempo/i, key: 'tempo', label: 'Tempo' },
  { match: /long/i, key: 'long', label: 'Long Run' },
  { match: /race/i, key: 'race', label: 'Race' },
]

function workoutTypeMeta(workoutType) {
  const found = WORKOUT_TYPES.find((entry) => entry.match.test(workoutType || ''))
  return found || { key: 'easy', label: 'Easy Run' }
}

function formatShortDate(date) {
  if (!date) return ''
  return date.toLocaleDateString(undefined, { month: 'short', day: 'numeric' })
}


function computeWeekStarts(workouts, raceDate) {
  if (!workouts.length || !raceDate) return {}

  const totalWeeks = Math.max(...workouts.map((w) => w.weekNumber))
  const race = new Date(`${raceDate}T00:00:00`)
  const raceDayIndex = (race.getDay() + 6) % 7 

  const lastWeekMonday = new Date(race)
  lastWeekMonday.setDate(race.getDate() - raceDayIndex)

  const weekStarts = {}
  for (let week = 1; week <= totalWeeks; week += 1) {
    const monday = new Date(lastWeekMonday)
    monday.setDate(lastWeekMonday.getDate() - (totalWeeks - week) * 7)
    weekStarts[week] = monday
  }
  return weekStarts
}

function groupByWeek(workouts) {
  return workouts.reduce((groups, workout) => {
    const key = workout.weekNumber
    if (!groups[key]) groups[key] = []
    groups[key].push(workout)
    return groups
  }, {})
}

function EmptyPlanIcon() {
  return (
    <svg width="40" height="40" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
      <circle cx="20" cy="20" r="18" stroke="var(--accent-border)" strokeWidth="2" strokeDasharray="4 4" />
      <path d="M14 12v16M14 12l10 4-10 4" stroke="var(--accent)" strokeWidth="2" strokeLinejoin="round" strokeLinecap="round" />
    </svg>
  )
}

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
        <div className="empty-state">
          <div className="empty-state-icon"><EmptyPlanIcon /></div>
          <h3>No plan yet</h3>
          <p>You have not generated a plan in this browser session yet.</p>
          <Link to="/race" className="nav-btn active">Create a training plan</Link>
        </div>
      </div>
    )
  }

  if (isLoading) {
    return <div className="page"><p>Loading your workouts...</p></div>
  }

  const weekStarts = computeWeekStarts(workouts, plan.raceDate)
  const weekGroups = groupByWeek(workouts)
  const weekNumbers = Object.keys(weekGroups).map(Number).sort((a, b) => a - b)

  return (
    <div className="page">
      <h2>{plan.raceType} training plan</h2>

      <div className="plan-summary-card">
        <div className="plan-summary-row">
          <div>
            <div className="plan-summary-label">Race date</div>
            <div className="plan-summary-value">{plan.raceDate}</div>
          </div>
          <span className={`status-badge status-badge--${plan.status?.toLowerCase()}`}>
            {plan.status}
          </span>
        </div>
        <p className="plan-summary-text">{plan.planSummary}</p>
      </div>

      {error && <p className="form-error" role="alert">{error}</p>}

      <section className="section">
        <span className="section-eyebrow">Schedule</span>
        <h3>Workouts</h3>

        {workouts.length === 0 ? (
          <p>No workouts were generated.</p>
        ) : (
          weekNumbers.map((weekNumber) => {
            const weekMonday = weekStarts[weekNumber]
            const weekSunday = weekMonday ? new Date(weekMonday) : null
            if (weekSunday) weekSunday.setDate(weekSunday.getDate() + 6)

            return (
              <div className="week-group" key={weekNumber}>
                <div className="week-group-header">
                  <span>Week {weekNumber}</span>
                  {weekMonday && (
                    <span className="week-group-range">
                      {formatShortDate(weekMonday)} – {formatShortDate(weekSunday)}
                    </span>
                  )}
                </div>

                <div className="workout-list">
                  {weekGroups[weekNumber].map((workout) => {
                    const dayOffset = DAY_INDEX[workout.dayOfWeek]
                    const workoutDate = weekMonday && dayOffset !== undefined
                      ? new Date(weekMonday.getTime() + dayOffset * 86400000)
                      : null
                    const typeMeta = workoutTypeMeta(workout.workoutType)

                    return (
                      <div className={`workout-card workout-card--${typeMeta.key}`} key={workout.id}>
                        <div className="workout-body">
                          <div className="workout-heading">
                            <span className="workout-day">
                              {workout.dayOfWeek}
                              {workoutDate && (
                                <span className="workout-date"> · {formatShortDate(workoutDate)}</span>
                              )}
                            </span>
                            {workout.completed && <span className="workout-done">Done</span>}
                          </div>
                          <div className="workout-title-row">
                            <span className={`type-badge type-badge--${typeMeta.key}`}>
                              {typeMeta.label}
                            </span>
                            <span className="workout-title">
                              {workout.distanceKm > 0 && `${workout.distanceKm} km`}
                              {workout.paceTarget && ` · ${workout.paceTarget}`}
                            </span>
                          </div>
                          <p className="workout-description">{workout.description}</p>
                        </div>
                      </div>
                    )
                  })}
                </div>
              </div>
            )
          })
        )}
      </section>
    </div>
  )
}

export default PlanView