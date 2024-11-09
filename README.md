# Allianz Stadium Simulator

Allianz Stadium Simulator is a concurrent simulation of a stadium environment, designed to demonstrate and apply threading concepts in a client-server context. This project visually illustrates various agent interactions, critical sections, and states within a stadium, providing an immersive experience complete with sound and real-time event handling.

## Project Overview
The Allianz Stadium Simulator showcases the flow of fans and other agents through different stations within a stadium. Agents can navigate freely but are subject to limited-capacity buffers at various points, such as ticket booths, food stands, and entrances. The simulator reflects a realistic stadium environment, featuring distinct states and critical sections for each agent type.

### Key Components
- Agents: Fans, food vendors, ticket sellers, and players, each with unique behaviors.
- Concurrency Management: Critical sections managed via semaphores to control agent flow.
- Client-Server Architecture: Supports distributed simulation with agents transitioning between servers as needed.
- Graphical User Interface (GUI): Displays agents in real-time and includes a process table showing agent state transitions.
- Sound Effects: Enhances realism with contextual audio for different activities.
- Agent Types and Behaviors
- Fan: Interacts with various stadium areas, such as purchasing food or tickets, watching the game, or using restrooms.
- Food Vendor: Handles customer requests for food, limited by buffer capacity.
- Ticket Seller: Manages ticket sales, controlling entry into the stadium.
- Player: Simulates match participation, with defined states for waiting, playing, and leaving the field.
- Each agent operates in a distinct state, transitioning based on interactions and buffer availability. States are visually represented in a process table for real-time monitoring.

### Critical Sections and Buffers
Critical sections control access to high-traffic areas:

- Entrance & Security: Limits the number of fans entering at any given time.
- Ticket Booths & Food Stands: Regulate customer access with limited-capacity buffers.
- Seating Areas: Manages the number of fans in the viewing stands.
- Restrooms: Restricts restroom capacity for realistic flow control.
- Distributed Communication
- The simulator supports multiple stadium sections distributed across computers, mimicking a multi-terminal setup (e.g., different stadium sections for various games). Agents can migrate between computers, enforcing real-time updates on ticket availability and section occupancy.