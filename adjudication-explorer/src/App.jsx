import React, { useState, useEffect } from 'react';
import ClaimSelector from './components/ClaimSelector';
import ClaimDetails from './components/ClaimDetails';
import PromptViewer from './components/PromptViewer';
import LLMResponseViewer from './components/LLMResponseViewer';
import AdjudicationNotePromptViewer from './components/AdjudicationNotePromptViewer';
import ApiResponseViewer from './components/ApiResponseViewer';
import LogViewer from './components/LogViewer';
import { getClaimIds, getApiResponse } from './api';
import './App.css';

function App() {
  const [claimIds, setClaimIds] = useState([]);
  const [selectedClaim, setSelectedClaim] = useState('');
  const [apiResponse, setApiResponse] = useState(null);
  const [loading, setLoading] = useState(false);
  const [llmRefreshKey, setLlmRefreshKey] = useState(0);

  useEffect(() => {
    getClaimIds().then(setClaimIds);
  }, []);

  useEffect(() => {
    if (selectedClaim) {
      setLoading(true);
      getApiResponse(selectedClaim)
        .then(res => {
          setApiResponse(res);
          setLlmRefreshKey(k => k + 1); // trigger LLM response refresh
        })
        .finally(() => setLoading(false));
    } else {
      setApiResponse(null);
    }
  }, [selectedClaim]);

  return (
    <div style={{ maxWidth: 800, margin: '40px auto', fontFamily: 'sans-serif' }}>
      <h1>Adjudication Assist Explorer</h1>
      <ClaimSelector claimIds={claimIds} selected={selectedClaim} onSelect={setSelectedClaim} />
      {selectedClaim && <ClaimDetails claimId={selectedClaim} />}
      {selectedClaim && <PromptViewer claimId={selectedClaim} />}
      {selectedClaim && <LLMResponseViewer claimId={selectedClaim} refreshKey={llmRefreshKey} />}
      {selectedClaim && <AdjudicationNotePromptViewer claimId={selectedClaim} refreshKey={llmRefreshKey} />}
      {loading && <div>Loading...</div>}
      <ApiResponseViewer response={apiResponse} />
      {selectedClaim && <LogViewer claimId={selectedClaim} />}
    </div>
  );
}

export default App;
