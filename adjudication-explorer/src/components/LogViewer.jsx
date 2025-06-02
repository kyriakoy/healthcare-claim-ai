import React, { useEffect, useState } from 'react';
import { getLogs } from '../api';
import CollapsibleSection from './CollapsibleSection';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { oneDark } from 'react-syntax-highlighter/dist/esm/styles/prism';

export default function LogViewer({ claimId }) {
  const [logs, setLogs] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!claimId) return;
    setLoading(true);
    setError(null);
    getLogs(claimId)
      .then(setLogs)
      .catch(e => setError('Failed to load logs'))
      .finally(() => setLoading(false));
  }, [claimId]);

  if (!claimId) return null;
  return (
    <CollapsibleSection title="Claim Processing Log">
      {loading && <div>Loading logs...</div>}
      {error && <div style={{ color: 'red' }}>{error}</div>}
      {logs && (
        <SyntaxHighlighter language="json" style={oneDark} customStyle={{ fontSize: 14 }}>
          {JSON.stringify(logs, null, 2)}
        </SyntaxHighlighter>
      )}
    </CollapsibleSection>
  );
} 