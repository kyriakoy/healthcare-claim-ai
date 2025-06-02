import React, { useState } from 'react';

export default function CollapsibleSection({ title, children, defaultOpen = true }) {
  const [open, setOpen] = useState(defaultOpen);
  return (
    <div style={{ margin: '24px 0', borderRadius: 6, boxShadow: '0 2px 8px #0001', background: '#222' }}>
      <div
        style={{ cursor: 'pointer', fontWeight: 600, fontSize: 18, padding: 12, background: '#333', borderRadius: 6 }}
        onClick={() => setOpen(o => !o)}
      >
        {open ? '▼' : '►'} {title}
      </div>
      {open && <div style={{ padding: 16 }}>{children}</div>}
    </div>
  );
} 