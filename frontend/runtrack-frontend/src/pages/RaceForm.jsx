import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { generateTrainingPlan } from '../api/trainingPlans'

function RaceForm() {
  const navigate = useNavigate()

  const [raceType, setRaceType] = useState('')
  const [distanceKm, setDistanceKm] = useState('')
  const [raceDate, setRaceDate] = useState('')
  const [error, setError] = useState('')
  const [isGenerating, setIsGenerating] = useState(false)

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setIsGenerating(true)

    try {
      const plan = await generateTrainingPlan(
        raceType,
        Number(distanceKm),
        raceDate
      )

      navigate('/plan', { state: { plan } })
    } catch (requestError) {
      setError(
        requestError.response?.data?.message ||
        'Unable to generate your training plan.'
      )
    } finally {
      setIsGenerating(false)
    }
  }

  return (
    <div className="page">
      <h2>Create a training plan</h2>
      <p>Enter your target race details.</p>

      <form className="auth-card" onSubmit={handleSubmit}>
        {error && <p className="form-error" role="alert">{error}</p>}

        <label htmlFor="raceType">Race type</label>
        <select
          id="raceType"
          value={raceType}
          onChange={(event) => setRaceType(event.target.value)}
          required
        >
          <option value="">Select a race</option>
          <option value="5K">5K</option>
          <option value="10K">10K</option>
          <option value="Half Marathon">Half Marathon</option>
          <option value="Marathon">Marathon</option>
          <option value="Other">Other</option>
        </select>

        <label htmlFor="distanceKm">Race distance (km)</label>
        <input
          id="distanceKm"
          type="number"
          value={distanceKm}
          onChange={(event) => setDistanceKm(event.target.value)}
          min="0.1"
          step="0.1"
          required
        />

        <label htmlFor="raceDate">Race date</label>
        <input
          id="raceDate"
          type="date"
          value={raceDate}
          onChange={(event) => setRaceDate(event.target.value)}
          min={new Date().toISOString().split('T')[0]}
          required
        />

        <button
          type="submit"
          className="nav-btn active"
          disabled={isGenerating}
        >
          {isGenerating ? 'Generating plan…' : 'Generate plan'}
        </button>
      </form>
    </div>
  )
}

export default RaceForm