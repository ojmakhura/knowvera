# Application

This project is built with [Angular CLI](https://github.com/angular/angular-cli) version 21.0.0.



```bash
npm install
```


To start a local development server, run:

```bash
npm start
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.


To view the project documentation in a web interface, run:

```bash
npm run docs
```

This will start the Hads documentation server at `http://localhost:4040/`


Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```


To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.


To execute unit tests with the [Karma](https://karma-runner.github.io) test runner, use the following command:

```bash
ng test
```


For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.



```
src/
âââ app/
â   âââ @core/         # Core services and utilities
â   âââ @shared/       # Shared components and modules
â   âââ controllers/   # Controllers
â   âââ models/        # Data models
â   âââ services/      # Application services
â   âââ shell/         # Shell layout component
â   âââ store/         # State management
â   âââ views/         # View components
âââ environments/      # Environment configurations
âââ public/           # Static assets
```


- **Material Design**: Modern UI with Angular Material components
- **Translation**: Multi-language support with ngx-translate (English, Spanish, French)
- **Responsive**: Mobile-first responsive design
- **Proxy Configuration**: API proxy for development
- **Git Hooks**: Husky for pre-commit formatting and linting


- `npm start` - Start development server
- `npm run build` - Build for production
- `npm test` - Run tests
- `npm run format` - Format code with Prettier
- `npm run format:check` - Check code formatting
- `npm run docs` - Start documentation server


The application uses a proxy configuration for API calls during development:

- Development: `/api` â `http://localhost:3000`
- Production: Direct calls to production API URL


1. Create a feature branch
2. Make your changes
3. Run `npm run format` to format code
4. Commit changes (husky will run pre-commit hooks)
5. Push and create a pull request


For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
