import { useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { getConnectUrl, getStats, syncActivities, getWeeklyStats } from '../api/strava'
import WeeklyDistanceChart from '../components/WeeklyDistanceChart'

const weekKm = (week) => Number(week?.totalDistanceKm ?? 0)

function Dashboard() {
  const location = useLocation()
  const navigate = useNavigate()

  const [stats, setStats] = useState(null)
  const [weeklyStats, setWeeklyStats] = useState([])
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(true)
  const [isConnecting, setIsConnecting] = useState(false)

  useEffect(() => {
    const loadDashboard = async () => {
      setError('')
      setIsLoading(true)

      const justConnected = new URLSearchParams(location.search).get('strava') === 'connected'

      try {
        await syncActivities()

        const [statsData, weeklyData] = await Promise.all([
          getStats(),
          getWeeklyStats(),
        ])

        setStats(statsData)
        setWeeklyStats(weeklyData)

        if (justConnected) {
          navigate('/', { replace: true })
        }
      } catch (requestError) {
        setError(
          requestError.response?.data?.message ||
          'Unable to load your running data.'
        )
      } finally {
        setIsLoading(false)
      }
    }

    loadDashboard()
  }, [location.search, navigate])

  const handleConnectStrava = async () => {
    setError('')
    setIsConnecting(true)

    try {
      const { authorizationUrl } = await getConnectUrl()
      window.location.assign(authorizationUrl)
    } catch (requestError) {
      setError(
        requestError.response?.data?.message ||
        'Unable to start the Strava connection.'
      )
      setIsConnecting(false)
    }
  }

  if (isLoading) {
    return <div className="page"><p>Loading your running data...</p></div>
  }

  // Week-over-week trend for the "average weekly distance" stat card.
  let trend = null
  if (weeklyStats.length >= 2) {
    const current = weekKm(weeklyStats[weeklyStats.length - 1])
    const previous = weekKm(weeklyStats[weeklyStats.length - 2])
    if (previous > 0) {
      const pctChange = ((current - previous) / previous) * 100
      trend = {
        direction: pctChange >= 0 ? 'up' : 'down',
        value: Math.abs(pctChange).toFixed(0),
      }
    }
  }

  return (
    <div className="page">
      <h2>Dashboard</h2>
      <p>Connect Strava to import your recent runs and build your training plan.</p>

      {error && <p className="form-error" role="alert">{error}</p>}
      <button
        type="button"
        className="nav-btn active"
        onClick={handleConnectStrava}
        disabled={isConnecting}
      >
        {isConnecting ? 'Connecting...' : 'Connect Strava'}
      </button>

      {stats && (
        <section className="section">
          <span className="section-eyebrow">Overview</span>
          <h3>Your stats</h3>
          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-value">{stats.totalRuns}</div>
              <div className="stat-label">Total runs</div>
            </div>
            <div className="stat-card stat-card--accent">
              <div className="stat-value">{stats.totalDistanceKm.toFixed(1)}<span className="stat-unit">km</span></div>
              <div className="stat-label">Total distance</div>
            </div>
            <div className="stat-card">
              <div className="stat-value">{stats.longestRunKm.toFixed(1)}<span className="stat-unit">km</span></div>
              <div className="stat-label">Longest run</div>
            </div>
            <div className="stat-card">
              <div className="stat-value">{stats.avgWeeklyKm.toFixed(1)}<span className="stat-unit">km</span></div>
              <div className="stat-label">Average weekly distance</div>
              {trend && (
                <div className={`stat-trend stat-trend--${trend.direction}`}>
                  {trend.direction === 'up' ? '▲' : '▼'} {trend.value}% vs last week
                </div>
              )}
            </div>
          </div>
        </section>
      )}

      <section className="section">
        <span className="section-eyebrow">Trends</span>
        <h3>Weekly Distance</h3>
        <WeeklyDistanceChart data={weeklyStats} />
      </section>
    </div>
  )
}

export default Dashboard