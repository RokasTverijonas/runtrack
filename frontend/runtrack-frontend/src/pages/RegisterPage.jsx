import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

function RegisterPage() {
  const { register } = useAuth()
  const navigate = useNavigate()
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setIsSubmitting(true)
    try {
      await register(name, email, password)
      navigate('/')
    } catch (requestError) {
      setError(requestError.response?.data?.message || 'Unable to create your account.')
    } finally {
      setIsSubmitting(false)
    }
  }

  return <main className="auth-page"><form className="auth-card" onSubmit={handleSubmit}>
    <h1>Create your account</h1><p>Start building training plans with your running data.</p>
    {error && <p className="form-error" role="alert">{error}</p>}
    <label htmlFor="name">Name</label><input id="name" value={name} onChange={(event) => setName(event.target.value)} autoComplete="name" required />
    <label htmlFor="email">Email</label><input id="email" type="email" value={email} onChange={(event) => setEmail(event.target.value)} autoComplete="email" required />
    <label htmlFor="password">Password</label><input id="password" type="password" value={password} onChange={(event) => setPassword(event.target.value)} autoComplete="new-password" minLength="8" required />
    <button type="submit" className="nav-btn active" disabled={isSubmitting}>{isSubmitting ? 'Creating account…' : 'Create account'}</button>
    <p>Already have an account? <Link to="/login">Sign in</Link></p>
  </form></main>
}

export default RegisterPage
