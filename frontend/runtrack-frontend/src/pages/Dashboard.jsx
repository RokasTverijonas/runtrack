import { useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { getConnectUrl, getStats, syncActivities, getWeeklyStats } from '../api/strava'
import WeeklyDistanceChart from '../components/WeeklyDistanceChart'

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
        <section>
          <h3>Your stats</h3>
          <p>Total runs: {stats.totalRuns}</p>
          <p>Total distance: {stats.totalDistanceKm.toFixed(1)} km</p>
          <p>Longest run: {stats.longestRunKm.toFixed(1)} km</p>
          <p>Average weekly distance: {stats.avgWeeklyKm.toFixed(1)} km</p>
        </section>
      )}

      <section>
        <h3>Weekly Distance</h3>
        <WeeklyDistanceChart data={weeklyStats} />
      </section>
    </div>
  )
}

export default Dashboard