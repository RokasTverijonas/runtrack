import { useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { getActivities, getConnectUrl, getStats, syncActivities } from '../api/strava'



function Dashboard() {
  const location = useLocation()
  const navigate = useNavigate()
  
  const [activities, setActivities] = useState([])
  const [stats, setStats] = useState(null)
  const [error, setError] = useState(' ')
  const [isLoading, setIsLoading] = useState(true)
  const [isConnecting, setIsConnecting] = useState(false)

  useEffect(() => {
    const loadDashboard = async () => {
      setError('')
      setIsLoading(true)

      const justConnected = new URLSearchParams(location.search).get('strava') === 'connected'

      try {
        if (justConnected) {
          await syncActivities()
        }

        const [activitiesData, statsData] = await Promise.all([
          getActivities(),
          getStats(),
        ])

        setActivities(activitiesData)
        setStats(statsData)

        if(justConnected) {
          navigate('/', { replace: true})
        }
       } catch(requestError) {
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

  const formatPace = (pace) => {
    const minutes = Math.floor(pace)
    const seconds = Math.round((pace - minutes) * 60)

    return `${minutes}:${String(seconds).padStart(2, '0')} min/km`
  }

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
      type='button'
      className='nav-btn active'
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
        <h3>Recent Activities</h3>

        {activities.length === 0 ? (
          <p>No Synced activities yet.</p>
        ) : (
          <ul>
            {activities.slice(0, 5).map((activity) => (
              <li key={activity.id}>
                {activity.name} - {(activity.distanceMeters / 1000).toFixed(1)} km
                - pace: {formatPace(activity.avgPace)}
              </li>
            ))}
          </ul>
        
        )}
      </section>
    </div>
  )
}

export default Dashboard
