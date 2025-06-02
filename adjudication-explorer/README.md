# Adjudication Explorer - Frontend UI

A modern React-based web application that provides an intuitive interface for healthcare claims adjudication workflows. This frontend connects to the Healthcare Claims Adjudication API to deliver a seamless user experience for claims processing, AI-powered validation, and adjudication decision-making.

## 🎯 Purpose

The Adjudication Explorer serves as the primary user interface for:

- **Claims Dashboard**: Overview and management of healthcare claims requiring adjudication
- **AI-Powered Validation**: Interactive visualization of LLM-based rule validation results
- **Transparency Tools**: Inspection of AI prompts and decision-making processes
- **Adjudication Workflow**: Guided interface for claims processing and decision-making
- **Real-time Monitoring**: Live updates on claim processing status and results

## 🚀 Quick Start

### Prerequisites
- **Node.js 18+** (recommended: use the latest LTS version)
- **npm** or **yarn** package manager
- **Healthcare Claims API** running on `http://localhost:8080`

### Installation & Setup

1. **Navigate to the UI directory:**
   ```bash
   cd adjudication-explorer
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```
   
   Or with yarn:
   ```bash
   yarn install
   ```

3. **Start the development server:**
   ```bash
   npm run dev
   ```
   
   Or with yarn:
   ```bash
   yarn dev
   ```

4. **Access the application:**
   
   Open your browser and navigate to `http://localhost:5173`
   
   The application will automatically reload when you make changes to the source code.

### Production Build

To create a production build:

```bash
npm run build
```

To preview the production build locally:

```bash
npm run preview
```

## 🏗️ Architecture & Technology Stack

### Core Technologies
- **React 19**: Latest React with modern hooks and concurrent features
- **Vite**: Fast build tool and development server
- **JavaScript (ES6+)**: Modern JavaScript with modules and async/await
- **CSS3**: Modern styling with flexbox and grid layouts
- **React Syntax Highlighter**: Code syntax highlighting for AI prompts

### Project Structure
```
adjudication-explorer/
├── public/                 # Static assets
│   ├── index.html         # Main HTML template
│   └── favicon.ico        # Application icon
├── src/                   # Source code
│   ├── components/        # React components
│   ├── services/          # API service layer
│   ├── utils/             # Utility functions
│   ├── styles/            # CSS stylesheets
│   ├── App.jsx            # Main application component
│   └── main.jsx           # Application entry point
├── package.json           # Dependencies and scripts
├── vite.config.js         # Vite configuration
├── eslint.config.js       # ESLint configuration
└── README.md              # This file
```

## 🎨 Key Features

### 1. Claims Dashboard
- **Claims List**: Comprehensive view of all claims requiring adjudication
- **Status Tracking**: Real-time status updates (pending, processing, completed)
- **Filtering & Search**: Advanced filtering by claim type, member, provider, date range
- **Priority Sorting**: Sort by urgency, amount, complexity

### 2. AI Validation Interface
- **Rule Validation Results**: Visual representation of LLM validation outcomes
- **Explanation Display**: Clear presentation of AI reasoning for each rule
- **Confidence Indicators**: Visual cues for AI confidence levels
- **Interactive Rule Details**: Expandable sections for detailed rule information

### 3. Transparency & Audit Tools
- **Prompt Inspection**: View the exact prompts sent to LLMs
- **Decision Trail**: Complete audit trail of adjudication decisions
- **AI Response Analysis**: Detailed breakdown of AI responses
- **Syntax Highlighting**: Formatted display of prompts and responses

### 4. Adjudication Workflow
- **Guided Process**: Step-by-step adjudication workflow
- **Decision Support**: AI recommendations with human oversight
- **Note Generation**: AI-assisted adjudication note creation
- **Action Buttons**: Approve, deny, or pend claims with one click

### 5. Real-time Updates
- **Live Status**: Real-time claim processing status updates
- **Progress Indicators**: Visual progress bars for long-running operations
- **Notifications**: Toast notifications for important events
- **Auto-refresh**: Automatic data refresh for current information

## 🔧 Development

### Available Scripts

- **`npm run dev`**: Start development server with hot reload
- **`npm run build`**: Create production build
- **`npm run preview`**: Preview production build locally
- **`npm run lint`**: Run ESLint for code quality checks

### Development Guidelines

1. **Component Structure**: Use functional components with hooks
2. **State Management**: Leverage React's built-in state management
3. **API Integration**: Use the service layer for all API calls
4. **Error Handling**: Implement comprehensive error boundaries
5. **Accessibility**: Follow WCAG guidelines for accessibility
6. **Performance**: Optimize for fast loading and smooth interactions

### Adding New Features

1. **Create Components**: Add new components in `src/components/`
2. **API Services**: Add API calls in `src/services/`
3. **Styling**: Use CSS modules or styled-components
4. **Testing**: Add unit tests for new components
5. **Documentation**: Update this README for significant changes

## 🔌 API Integration

The frontend integrates with the Healthcare Claims API through:

### Base Configuration
```javascript
const API_BASE_URL = 'http://localhost:8080';
```

### Key API Endpoints Used
- `POST /api/claims/assist-adjudication` - Trigger adjudication workflow
- `GET /api/claims/{claimId}/adjudication-note-prompt` - View AI prompts
- `GET /api/claims/{claimId}/prompt` - View rule validation prompts
- `POST /api/ai/chat` - Direct AI chat interface

### Error Handling
- Network error handling with user-friendly messages
- API error response parsing and display
- Retry mechanisms for failed requests
- Loading states for better user experience

## 🎨 UI/UX Design

### Design Principles
- **Clean & Intuitive**: Minimalist design focused on usability
- **Responsive**: Works seamlessly on desktop, tablet, and mobile
- **Accessible**: WCAG 2.1 AA compliance for accessibility
- **Fast**: Optimized for quick loading and smooth interactions

### Color Scheme
- **Primary**: Healthcare blue (#2563eb)
- **Secondary**: Success green (#10b981)
- **Warning**: Amber (#f59e0b)
- **Error**: Red (#ef4444)
- **Neutral**: Gray scale for text and backgrounds

### Typography
- **Headers**: System font stack for optimal performance
- **Body**: Readable font sizes with proper line height
- **Code**: Monospace fonts for AI prompts and technical content

## 🧪 Testing

### Running Tests
```bash
npm run test
```

### Testing Strategy
- **Unit Tests**: Component testing with React Testing Library
- **Integration Tests**: API integration testing
- **E2E Tests**: End-to-end workflow testing
- **Accessibility Tests**: Automated accessibility testing

### Manual Testing Checklist
- [ ] Claims dashboard loads correctly
- [ ] AI validation results display properly
- [ ] Prompt inspection works
- [ ] Adjudication workflow completes
- [ ] Error handling works as expected
- [ ] Responsive design on different screen sizes

## 🚀 Deployment

### Production Build
```bash
npm run build
```

### Static Hosting
The built application can be deployed to any static hosting service:
- **Netlify**: Drag and drop the `dist` folder
- **Vercel**: Connect your Git repository
- **AWS S3**: Upload the `dist` folder to an S3 bucket
- **GitHub Pages**: Use GitHub Actions for automated deployment

### Environment Configuration
For different environments, create environment-specific configuration:

```javascript
// config/environment.js
const config = {
  development: {
    API_BASE_URL: 'http://localhost:8080'
  },
  production: {
    API_BASE_URL: 'https://your-api-domain.com'
  }
};
```

## 🔍 Troubleshooting

### Common Issues

1. **API Connection Failed**
   - Ensure the backend API is running on `http://localhost:8080`
   - Check CORS configuration in the backend
   - Verify network connectivity

2. **Build Errors**
   - Clear node_modules and reinstall: `rm -rf node_modules && npm install`
   - Check Node.js version compatibility
   - Review ESLint errors and warnings

3. **Performance Issues**
   - Use React DevTools Profiler to identify bottlenecks
   - Implement code splitting for large components
   - Optimize images and assets

### Debug Mode
Enable debug logging by setting:
```javascript
localStorage.setItem('debug', 'true');
```

## 🤝 Contributing

1. **Code Style**: Follow the ESLint configuration
2. **Components**: Create reusable, well-documented components
3. **Testing**: Add tests for new features
4. **Documentation**: Update README for significant changes
5. **Performance**: Consider performance impact of changes

### Pull Request Guidelines
- Provide clear description of changes
- Include screenshots for UI changes
- Ensure all tests pass
- Update documentation as needed

## 📚 Additional Resources

- [React Documentation](https://react.dev/)
- [Vite Documentation](https://vitejs.dev/)
- [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/)
- [ESLint Rules](https://eslint.org/docs/rules/)

---

**[← Back to Main Project](../README.md)**
