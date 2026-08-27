import React from 'react'

export default function WeeklyDistanceChart({ data = [] }) {
  if (!data || data.length === 0) {
    return <div className="chart-empty">No weekly data available.</div>
  }

  const items = data.map((d) => {
    const weekStart = d.weekStart || d.week || d.label || d.week_start || d.start
    const totalKm = d.totalKm ?? d.total_km ?? d.distanceKm ?? d.totalDistanceKm ?? d.km ?? d.distance
    const averagePace = d.averagePace ?? d.average_pace ?? d.avgPace ?? d.avg_pace
    return {
      weekStart,
      totalKm: Number(totalKm) || 0,
      averagePace: averagePace != null ? Number(averagePace) : null,
    }
  })

  const max = Math.max(...items.map((i) => i.totalKm), 1)
  const width = 600
  const height = 160
  const padding = { top: 10, right: 10, bottom: 30, left: 40 }
  const chartWidth = width - padding.left - padding.right
  const chartHeight = height - padding.top - padding.bottom
  const barWidth = Math.max(8, Math.floor(chartWidth / items.length) - 8)

  const formatLabel = (s) => {
    if (!s) return ''
    const d = new Date(s)
    if (!isNaN(d)) {
      return `${d.getMonth() + 1}/${d.getDate()}`
    }
    return String(s).slice(0, 6)
  }

  const formatPace = (pace) => {
    if (pace == null || isNaN(pace) || pace <= 0) return 'N/A'
    const minutes = Math.floor(pace)
    const seconds = Math.round((pace - minutes) * 60)
    return `${minutes}:${String(seconds).padStart(2, '0')} min/km`
  }

  return (
    <div className="weekly-chart" style={{ overflowX: 'auto' }}>
      <svg width="100%" viewBox={`0 0 ${width} ${height}`} role="img" aria-label="Weekly distance chart">
        <g transform={`translate(${padding.left},${padding.top})`}>
          {/* Y grid and labels */}
          {[0, 0.25, 0.5, 0.75, 1].map((t) => {
            const y = Math.round(chartHeight - t * chartHeight)
            const value = Math.round(max * t)
            return (
              <g key={t}>
                <line x1={0} y1={y} x2={chartWidth} y2={y} stroke="#eee" />
                <text x={-6} y={y + 4} fontSize="10" textAnchor="end" fill="#666">{value}</text>
              </g>
            )
          })}

          {/* Bars */}
          {items.map((it, idx) => {
            const x = idx * (barWidth + 8)
            const h = (it.totalKm / max) * chartHeight
            const y = chartHeight - h
            const labelX = barWidth / 2
            return (
              <g key={idx} transform={`translate(${x},0)`}>
                <rect x={0} y={y} width={barWidth} height={Math.max(1, h)} fill="#4f46e5">
                  <title>{`${formatLabel(it.weekStart)} — ${it.totalKm.toFixed(1)} km — avg pace: ${formatPace(it.averagePace)}`}</title>
                </rect>
                <text x={labelX} y={chartHeight + 14} fontSize="10" textAnchor="middle" fill="#333">{formatLabel(it.weekStart)}</text>
              </g>
            )
          })}
        </g>
      </svg>
    </div>
  )
}