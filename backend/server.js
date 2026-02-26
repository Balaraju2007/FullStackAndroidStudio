import dotenv from 'dotenv';
import app from './app.js';
import connectDB from './config/db.js'; // ✅ fixed

dotenv.config();

// Connect to MongoDB
connectDB();

// Start server
const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log(`Server runningg on port ${PORT}`));