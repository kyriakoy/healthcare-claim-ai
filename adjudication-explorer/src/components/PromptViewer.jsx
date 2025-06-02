import React, { useEffect, useState } from 'react';
import { getPrompt } from '../api';
import CollapsibleSection from './CollapsibleSection';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { oneDark } from 'react-syntax-highlighter/dist/esm/styles/prism';

export default function PromptViewer({ claimId }) {
  const [prompt, setPrompt] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!claimId) return;
    setLoading(true);
    setError(null);
    getPrompt(claimId)
      .then(data => setPrompt(data.prompt))
      .catch(e => setError('Failed to load prompt'))
      .finally(() => setLoading(false));
  }, [claimId]);

  if (!claimId) return null;
  return (
    <CollapsibleSection title="Prompt Sent to LLM">
      {loading && <div>Loading prompt...</div>}
      {error && <div style={{ color: 'red' }}>{error}</div>}
      {prompt && (
        <SyntaxHighlighter language="text" style={oneDark} customStyle={{ fontSize: 14 }}>
          {prompt}
        </SyntaxHighlighter>
      )}
    </CollapsibleSection>
  );
} 