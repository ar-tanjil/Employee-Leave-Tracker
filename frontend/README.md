# Employee Leave Tracker - Frontend

A modern Angular application for managing employee leave requests, approvals, and organizational leave policies with a responsive and intuitive user interface.

## 📋 Project Overview

The Employee Leave Tracker frontend provides a comprehensive user interface for employees, managers, 
and HR administrators to manage employee, roles, leave applications, view leave balances, approve/reject requests. 
The application consumes REST APIs from the Spring Boot backend and features role-based access control, real-time updates, 
and a responsive design.

## 🛠 Tech Stack

- **Framework**: Angular 21.2.0
- **Language**: TypeScript 5.9.2
- **Build Tool**: Angular CLI 21.2.6
- **Package Manager**: npm 11.12.1
- **Styling**: TailwindCSS 4.1.12
- **UI Components**: Angular CDK 21.2.9
- **HTTP Client**: Angular HttpClient
- **Routing**: Angular Router
- **Forms**: Angular Reactive Forms
- **Notifications**: @ngxpert/hot-toast 6.2.0
- **Loading**: ngx-spinner 21.1.0
- **Alerts**: SweetAlert2 11.26.24
- **Development**: Angular DevTools
- **Containerization**: Docker

## 📁 Folder Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── core/                              # Core application logic
│   │   │   ├── components/                    # Reusable core components
│   │   │   ├── guards/                        # Route guards (auth, etc.)
│   │   │   ├── interceptors/                  # HTTP interceptors
│   │   │   └── services/                      # Core services (auth, api)
│   │   ├── features/                          # Feature modules
│   │   │   ├── dashboard/                     # Dashboard component
│   │   │   ├── employee/                      # Employee management
│   │   │   ├── leave/                         # Leave management
│   │   │   └── profile/                       # User profile
│   │   ├── layouts/                           # Layout components
│   │   │   └── auth-layout/                   # Authentication application layout
│   │   │   └── main-layout/                   # Main application layout
│   │   ├── models/                            # Data models and interfaces
│   │   ├── shared/                            # Shared components and utilities
│   │   │   ├── components/                    # Reusable UI components
│   │   │   ├── directives/                    # Custom directives
│   │   │   ├── pipes/                         # Custom pipes
│   │   │   └── utils/                         # Utility functions
│   │   ├── app.config.ts                      # Angular app configuration
│   │   ├── app.routes.ts                      # Application routing
│   │   ├── app.ts                             # Root component
│   │   ├── app.html                           # Root template
│   │   └── app.css                            # Global styles
│   ├── environments/                          # Environment configurations
│   │   ├── environment.ts                     # Default environment
│   │   └── environment.prod.ts                # Production environment
│   ├── styles/                                # Global styles
│   │   └── styles.css                         # Main stylesheet
│   ├── index.html                             # HTML entry point
│   └── main.ts                                # Application bootstrap
├── public/                                    # Static assets
├── .dockerignore                              # Docker ignore file
├── Dockerfile                                 # Docker build configuration
├── nginx.conf                                 # Nginx configuration for production
├── package.json                               # Dependencies and scripts
├── package-lock.json                          # Dependency lock file
├── proxy.conf.json                            # Development proxy configuration
├── tsconfig.json                              # TypeScript configuration
├── tsconfig.app.json                          # App-specific TypeScript config
├── tsconfig.spec.json                         # Test TypeScript config
└── angular.json                               # Angular CLI configuration
```

## 🚀 Installation Steps

### Prerequisites

- Node.js 21+ or latest LTS version
- npm 11.12.1+ (comes with Node.js)
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Employee-Leave-Tracker-main/frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Verify installation**
   ```bash
   ng version
   ```

## 🏃‍♂️ Development Server

### Start Development Server

```bash
# Start the development server
npm start

# Or using Angular CLI directly
ng serve

# With proxy configuration (recommended for API calls)
ng serve --proxy-config proxy.conf.json
```

### Development Server Options

```bash
# Open browser automatically
ng serve --open

# Use different port
ng serve --port 4201

# Enable SSL
ng serve --ssl

# Watch for changes (default)
ng serve --watch
```

### Access Points

- **Application**: `http://localhost:4200`
- **API Proxy**: Routes `/api/*` to backend at `http://localhost:8099`

## 🔨 Build Commands

### Development Build

```bash
# Build for development
npm run build

# Or using Angular CLI
ng build

# Build with development configuration
ng build --configuration development
```

### Production Build

```bash
# Build for production
ng build --configuration production

# Build with localization
ng build --localize

# Build with source maps (for debugging)
ng build --source-map
```

### Build Analysis

```bash
# Analyze bundle size
ng build --configuration production --stats-json
npx webpack-bundle-analyzer dist/frontend/stats.json
```


## 🎯 Key Features & Pages

### Authentication

- **Login Page**: User authentication with JWT tokens
- **Role-based Access**: Different views for employees, managers, and HR
- **Session Management**: Expire token refresh and logout

### Dashboard

- **Overview**: Total employee, And Leave Summary by current data

### Employee Management (HR/Manager)

- **Employee Directory**: Create, view  and terminate employees
- **Employee Profile**: Detailed employee information
- **Role Management**: Manage role assignments
- **Leave Approval**: Approve/reject leave requests

### Leave Management

- **Leave Request**: Submit new leave requests
- **Leave History**: View past and current leave requests
- **Leave Balance**: Check available leave balances


### User Profile

- **Personal Information**: Update user profile
- **Password Change**: Change account password

## 🚀 Deployment Instructions

### Option 1: Docker Deployment (Recommended)

1. **Build and Deploy with Docker Compose**
   ```bash
   # From project root directory
   docker-compose up -d
   ```

2. **Frontend will be available at**: `http://localhost:80`

3. **Docker Build Commands**
   ```bash
   # Build frontend image only
   docker build -t employee-leave-frontend ./frontend
   
   # Run frontend container
   docker run -d -p 80:80 employee-leave-frontend
   ```

### Option 2: Static Deployment

1. **Build for production**
   ```bash
   ng build --configuration production
   ```

2. **Deploy to web server**
   ```bash
   # Deploy dist/frontend/ to your web server
   # Examples: Nginx, Apache, S3, Netlify, Vercel
   ```

3. **Nginx Configuration Example**
   ```nginx
   server {
       listen 80;
       server_name your-domain.com;
       root /path/to/dist/frontend;
       index index.html;
       
       location / {
           try_files $uri $uri/ /index.html;
       }
       
       location /api/ {
           proxy_pass http://backend:8099/api/;
           proxy_set_header Host $host;
       }
   }
   ```

## 🔧 Development Tools & Commands

### Code Generation

```bash
# Generate component
ng generate component components/new-component

# Generate service
ng generate service services/new-service

# Generate module
ng generate module modules/new-module

# Generate interface
ng generate interface models/new-interface
```

### Linting and Formatting

```bash
# Run linting
ng lint

# Fix linting issues
ng lint --fix

# Format code (if Prettier is configured)
npx prettier --write "src/**/*.ts"
```

### Testing

```bash
# Run unit tests
npm test

# Run tests with coverage
ng test --code-coverage

# Run end-to-end tests
ng e2e

# Run specific test file
ng test --include="**/auth.spec.ts"
```

### Performance Analysis

```bash
# Build with stats
ng build --stats-json

# Analyze bundle
npx webpack-bundle-analyzer dist/frontend/stats.json

# Lighthouse audit
npx lighthouse http://localhost:4200 --output html --output-path ./lighthouse-report.html
```

## 🎨 Styling & Theming

### TailwindCSS Configuration

The application uses TailwindCSS for styling:

```bash
# Generate Tailwind config
npx tailwindcss init

# Build CSS
npx tailwindcss -i ./src/styles.css -o ./dist/output.css
```

### Custom Themes

- Edit `src/styles.css` for global styles
- Component-specific styles in component files
- Responsive design with Tailwind utilities

## 🐛 Troubleshooting

### Common Issues

1. **Proxy Configuration Issues**
   ```bash
   # Ensure backend is running on port 8099
   # Check proxy.conf.json configuration
   # Restart development server
   ```

2. **Build Errors**
   ```bash
   # Clear cache
   npm cache clean --force
   
   # Delete node_modules and reinstall
   rm -rf node_modules package-lock.json
   npm install
   ```

3. **Performance Issues**
   ```bash
   # Check bundle size
   ng build --stats-json
   
   # Enable lazy loading
   # Optimize images and assets
   # Use trackBy in *ngFor
   ```

## 📱 Browser Support

- **Chrome**: Latest version
- **Firefox**: Latest version
- **Safari**: Latest version
- **Edge**: Latest version
- **Mobile**: iOS Safari, Android Chrome

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

