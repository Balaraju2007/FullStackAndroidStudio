// app.js
import express from 'express';
import cors from 'cors';
import morgan from 'morgan';
import dotenv from 'dotenv';

dotenv.config();

const app = express();

// Middleware
app.use(cors());
app.use(express.json());   // parse JSON bodies
app.use(morgan('dev'));    // log HTTP requests

// Test route
app.get('/', (req, res) => {
  res.send('Backend is running!');
});

export default app;