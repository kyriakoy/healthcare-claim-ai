import React from 'react';
import CollapsibleSection from './CollapsibleSection';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { oneDark } from 'react-syntax-highlighter/dist/esm/styles/prism';

export default function ApiResponseViewer({ response }) {
  if (!response) return null;
  return (
    <CollapsibleSection title="Adjudication Assist API Response">
      <SyntaxHighlighter language="json" style={oneDark} customStyle={{ fontSize: 14 }}>
        {JSON.stringify(response, null, 2)}
      </SyntaxHighlighter>
    </CollapsibleSection>
  );
} 