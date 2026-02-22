// config/db.js
import mongoose from 'mongoose';
import dotenv from 'dotenv';

dotenv.config(); // load .env variables

const connectDB = async () => {
  try {
    console.log("MONGO_URI =", process.env.MONGO_URI); // optional: check if loaded
     const conn = await mongoose.connect(process.env.MONGO_URI); // no options needed
    console.log(`MongoDB Connected: ${conn.connection.host}`);
  } catch (error) {
    console.error(`Error connecting to MongoDB: ${error.message}`);
    process.exit(1);
  }
};

export default connectDB;