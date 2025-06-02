import React, { useEffect, useState } from 'react';
import { getClaimDetails } from '../api';
import CollapsibleSection from './CollapsibleSection';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { oneDark } from 'react-syntax-highlighter/dist/esm/styles/prism';

export default function ClaimDetails({ claimId }) {
  const [details, setDetails] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!claimId) return;
    setLoading(true);
    setError(null);
    getClaimDetails(claimId)
      .then(setDetails)
      .catch(e => setError('Failed to load claim details'))
      .finally(() => setLoading(false));
  }, [claimId]);

  if (!claimId) return null;
  return (
    <CollapsibleSection title="Claim, Member, and Rule Data">
      {loading && <div>Loading claim details...</div>}
      {error && <div style={{ color: 'red' }}>{error}</div>}
      {details && (
        <SyntaxHighlighter language="json" style={oneDark} customStyle={{ fontSize: 14 }}>
          {JSON.stringify(details, null, 2)}
        </SyntaxHighlighter>
      )}
    </CollapsibleSection>
  );
} 