# CollisionsSim
This is a client-server application that simulates collisions between objects in real time. Server does calculations and clients update their view accordingly. Server will do the heavy computations regarding the collisions while clients receive the result of those computations. Server can accept multiple clients. Server contains the same view that the clients do but is detached from the server protocol and therefore serves as a comparison. 

This is a basic demo and serves no real purpose other than being an interesting challenge. A combination of a physics simulation and a client-server application that needs to perform well.

Currently all properties are written in the code and everything is hosted locally.
To test, simply run the server application and then as many client applications as you desire.
