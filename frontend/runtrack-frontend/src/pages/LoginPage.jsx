import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setIsSubmitting(true)
    try {
      await login(email, password)
      navigate('/')
    } catch (requestError) {
      setError(requestError.response?.data?.message || 'Unable to sign in. Check your email and password.')
    } finally {
      setIsSubmitting(false)
    }
  }

  return <main className="auth-page"><form className="auth-card" onSubmit={handleSubmit}>
    <h1>Welcome back</h1><p>Sign in to continue to RunTrack.</p>
    {error && <p className="form-error" role="alert">{error}</p>}
    <label htmlFor="email">Email</label><input id="email" type="email" value={email} onChange={(event) => setEmail(event.target.value)} autoComplete="email" required />
    <label htmlFor="password">Password</label><input id="password" type="password" value={password} onChange={(event) => setPassword(event.target.value)} autoComplete="current-password" required />
    <button type="submit" className="nav-btn active" disabled={isSubmitting}>{isSubmitting ? 'Signing in…' : 'Sign in'}</button>
    <p>New to RunTrack? <Link to="/register">Create an account</Link></p>
  </form></main>
}

export default LoginPage
