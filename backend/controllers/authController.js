import user from '../models/user.js';
import bycerpt from 'bcryptjs';
// @desc    Register a new user 
// @route   POST /api/register
const registerUser = async (req, res) => {
    const { name, email, password } = req.body;

    try {
        if (!name || !email || !password) {
            return res.status(400).json({ message: 'Please provide name, email, and password' });
        }

        const existingUser = await user.findOne({ email });
        if (existingUser) {
            return res.status(400).json({ message: 'User already exists' });
        }   
        
        const hashedPassword = await bycerpt.hash(password, 10);
        const newUser = new user({ name, email, password: hashedPassword });
        await newUser.save();
        res.status(201).json({ message: 'User registered successfully', 
            user:{ name: newUser.name, email: newUser.email , id: newUser._id  }
         });
    } catch (error) {
        console.error('Error registering user:', error);
        res.status(500).json({ message: 'Server error' });
    }
};

export { registerUser };