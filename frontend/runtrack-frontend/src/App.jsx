import { Navigate, Route, Routes, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from './context/AuthContext'
import Dashboard from './pages/Dashboard'
import LoginPage from './pages/LoginPage'
import RaceForm from './pages/RaceForm'
import PlanView from './pages/PlanView'
import RegisterPage from './pages/RegisterPage'
import './App.css'

function ProtectedLayout() {
  const { user, isLoading, logout } = useAuth()
  const location = useLocation()
  const navigate = useNavigate()

  if (isLoading) return <main className="main">Loading your RunTrack account…</main>
  if (!user) return <Navigate to="/login" replace />

  const activePage = { '/': 'dashboard', '/race': 'race', '/plan': 'plan' }[location.pathname]

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="app">
      <header className="header">
        <h1>RunTrack</h1>
        <nav className="nav">
          <button
            type="button"
            className={activePage === 'dashboard' ? 'nav-btn active' : 'nav-btn'}
            onClick={() => navigate('/')}
          >
            Dashboard
          </button>
          <button
            type="button"
            className={activePage === 'race' ? 'nav-btn active' : 'nav-btn'}
            onClick={() => navigate('/race')}
          >
            Race form
          </button>
          <button
            type="button"
            className={activePage === 'plan' ? 'nav-btn active' : 'nav-btn'}
            onClick={() => navigate('/plan')}
          >
            My plan
          </button>
          <button type="button" className="nav-btn" onClick={handleLogout}>Log out</button>
        </nav>
      </header>

      <main className="main">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/race" element={<RaceForm />} />
          <Route path="/plan" element={<PlanView />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
    </div>
  )
}

function PublicRoute({ children }) {
  const { user, isLoading } = useAuth()
  if (isLoading) return null
  return user ? <Navigate to="/" replace /> : children
}

function App() {
  return (
    <Routes>
      <Route path="/login" element={<PublicRoute><LoginPage /></PublicRoute>} />
      <Route path="/register" element={<PublicRoute><RegisterPage /></PublicRoute>} />
      <Route path="/*" element={<ProtectedLayout />} />
    </Routes>
  )
}

export default App
