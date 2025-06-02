import React, { useEffect, useState } from 'react';
import CollapsibleSection from './CollapsibleSection';

export default function AdjudicationNotePromptViewer({ claimId, refreshKey }) {
  const [prompt, setPrompt] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!claimId) return;
    setLoading(true);
    setError(null);
    fetch(`/api/mcp/claims/${claimId}/adjudication-note-prompt`)
      .then(res => res.json())
      .then(data => setPrompt(data.adjudicationNotePrompt))
      .catch(e => setError('Failed to load adjudication note prompt'))
      .finally(() => setLoading(false));
  }, [claimId, refreshKey]);

  if (!claimId) return null;
  return (
    <CollapsibleSection title="Adjudication Note Prompt Sent to LLM">
      {loading && <div>Loading adjudication note prompt...</div>}
      {error && <div style={{ color: 'red' }}>{error}</div>}
      {prompt && (
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
          {prompt}
        </div>
      )}
    </CollapsibleSection>
  );
} 