// app.js
import express from 'express';
import cors from 'cors';
import morgan from 'morgan';
import dotenv from 'dotenv';
import { registerUser } from './controllers/authController.js';
import { loginUser } from './controllers/authController.js';

dotenv.config();

const app = express();

// Middleware
app.use(cors());
app.use(express.json());   // parse JSON bodies
app.use(morgan('dev'));    // log HTTP requests

// app.post("/api");
// Test route
app.post('/api/register', registerUser);
app.post('/api/login', loginUser);

app.get('/', (req, res) => {
  res.send('Backend is running!');
});

export default app;