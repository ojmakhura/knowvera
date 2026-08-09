import '@angular/compiler';
import { initialiseApp } from "@app/app.config";

initialiseApp().catch((err) => console.error('Error during app initialization:', err));
