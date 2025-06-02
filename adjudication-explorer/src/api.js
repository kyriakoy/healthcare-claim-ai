// API utility for MCP Adjudication Explorer

export async function getClaimIds() {
  // For demo, hardcode or fetch from backend if available
  return [
    'CLM1001', 'CLM2001', 'CLM2002', 'CLM2003',
    'CLM3001', 'CLM3002', 'CLM4001', 'CLM4002'
  ];
}

export async function getClaimDetails(claimId) {
  return fetch(`/api/mcp/claims/${claimId}/details`).then(res => res.json());
}

export async function getPrompt(claimId) {
  return fetch(`/api/mcp/claims/${claimId}/prompt`).then(res => res.json());
}

export async function getLLMResponse(claimId) {
  return fetch(`/api/mcp/claims/${claimId}/llm-response`).then(res => res.json());
}

export async function getApiResponse(claimId) {
  return fetch('/api/mcp/claims/assist-adjudication', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ claimId })
  }).then(res => res.json());
}

export async function getLogs(claimId) {
  return fetch(`/api/mcp/claims/${claimId}/logs`).then(res => res.json());
} 