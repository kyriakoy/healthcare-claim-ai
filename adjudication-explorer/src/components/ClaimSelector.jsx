import React, { useState } from 'react';

export default function ClaimSelector({ claimIds, selected, onSelect }) {
  const [search, setSearch] = useState('');
  const filtered = claimIds.filter(id => id.toLowerCase().includes(search.toLowerCase()));
  return (
    <div style={{ marginBottom: 16 }}>
      <input
        type="text"
        placeholder="Search claims..."
        value={search}
        onChange={e => setSearch(e.target.value)}
        style={{ marginRight: 8, padding: 4 }}
      />
      <select value={selected} onChange={e => onSelect(e.target.value)}>
        <option value="">-- Choose a claim --</option>
        {filtered.map(id => (
          <option key={id} value={id}>{id}</option>
        ))}
      </select>
    </div>
  );
} 