import React, { useEffect, useState } from 'react';
import { getLLMResponse } from '../api';
import CollapsibleSection from './CollapsibleSection';

export default function LLMResponseViewer({ claimId, refreshKey }) {
  const [llmResponse, setLLMResponse] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!claimId) return;
    setLoading(true);
    setError(null);
    getLLMResponse(claimId)
      .then(data => setLLMResponse(data.llmResponse))
      .catch(e => setError('Failed to load LLM response'))
      .finally(() => setLoading(false));
  }, [claimId, refreshKey]);

  if (!claimId) return null;
  return (
    <CollapsibleSection title="Raw LLM Response">
      {loading && <div>Loading LLM response...</div>}
      {error && <div style={{ color: 'red' }}>{error}</div>}
      {llmResponse && (
        <div style={{
          background: '#23272e',
          color: '#eaeaea',
          padding: 16,
          borderRadius: 6,
          fontFamily: 'monospace',
          fontSize: 15,
          whiteSpace: 'pre-wrap',
          wordBreak: 'break-word',
          margin: '8px 0'
        }}>
          {llmResponse}
        </div>
      )}
    </CollapsibleSection>
  );
} 