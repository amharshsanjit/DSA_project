
# 🏫 Rungta R1 Smart Campus Navigation App

### Smart Campus Navigation Using Graph Algorithms

![Java](https://img.shields.io/badge/Java-17-orange)
![React](https://img.shields.io/badge/React-TypeScript-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Backend-green)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue)
![Project](https://img.shields.io/badge/Project-Campus%20Navigation-purple)

---

## 📌 About The Project

The **Rungta R1 Smart Campus Navigation App** is a campus navigation platform designed specifically for Rungta Educational Campus, Bhilai, Chhattisgarh.

The application helps students, faculty members, new admissions, and visitors easily locate buildings, blocks, departments, classrooms, laboratories, and other campus facilities.

Users can search for destinations, explore the campus map, and find efficient walking routes using Graph Algorithms.

## 🎯 Objectives

- Provide smart navigation within Rungta R1 Campus.
- Help users locate buildings and departments.
- Find the shortest walking-distance route.
- Find routes with minimum stops.
- Provide building and block information.
- Support indoor navigation using verified floor plans.
- Make campus navigation simple and accessible.

## ✨ Key Features

- 🔍 Smart Campus Search
- 🗺️ Interactive Campus Map
- 📍 Building and Block Directory
- 🏢 Department Navigation
- 🚶 Shortest Route Finder
- 🔄 Fewest Stops Navigation
- 🏫 Indoor Floor Navigation
- 🛠️ Admin Dashboard
- 📱 Responsive User Interface

## 🏛️ Campus Coverage

The application aims to cover the following institutions and facilities:

- Rungta College of Engineering & Technology
- Rungta College of Dental Sciences & Research
- Rungta Institute of Pharmaceutical Sciences
- Rungta Institute of Sciences & Management
- Rungta College of Pharmaceutical Sciences & Research
- Rungta Public School
- Rungta Industrial Training Institute
- Central Library
- Auditorium
- Hostels
- Canteens
- Sports Facilities
- Parking Areas
- Medical Facilities
- ATM Facilities

**Note:** Individual block numbers, classroom numbers, floor details, and building coordinates must be verified using official campus information.

## 💻 Technology Stack

### Frontend
- React.js
- TypeScript
- Tailwind CSS
- Leaflet.js
- OpenStreetMap

### Backend
- Java
- Spring Boot
- REST API

### Database
- PostgreSQL

### Data Structures & Algorithms
- Graph
- Adjacency List
- HashMap
- Dijkstra's Algorithm
- Breadth-First Search (BFS)

## 🧠 DSA Implementation

### 1. Graph Data Structure

The campus is represented as a weighted graph.

- Vertices represent campus locations.
- Edges represent walking connections.
- Edge weights represent distances in metres.

### 2. Dijkstra's Algorithm

Used to calculate the shortest-distance route between two campus locations.

**Components:**
- Priority Queue
- Distance Array
- Parent Array
- Path Reconstruction

### 3. Breadth-First Search (BFS)

Used to find the route with the minimum number of stops.

**Components:**
- Queue
- Visited Array
- Parent Array
- Path Reconstruction

## 📊 Sample Graph Data

| Connection | Distance |
|---|---:|
| Gate ↔ Library | 100 m |
| Gate ↔ Admin | 50 m |
| Admin ↔ Library | 40 m |
| Library ↔ Canteen | 60 m |
| Admin ↔ Canteen | 200 m |

*These are sample distances from the project problem statement, not verified physical campus measurements.*

## ⚙️ Installation & Setup

### Prerequisites

- Java JDK 17+
- Node.js and npm
- PostgreSQL
- Maven
- Git

### 1. Clone Repository

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
cd Rungta-Campus-Navigation
```

### 2. Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 3. Frontend Setup

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

### 4. Database Setup

1. Install PostgreSQL.
2. Create a project database.
3. Configure database credentials.
4. Execute the database schema.
5. Start the backend server.

*Update commands and configuration according to the final implementation.*

## 📂 Project Structure

```text
Rungta-Campus-Navigation/
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── maps/
│   │   ├── floorplans/
│   │   └── services/
│   └── package.json
│
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   ├── controllers/
│   │       │   ├── services/
│   │       │   ├── models/
│   │       │   ├── repositories/
│   │       │   └── algorithms/
│   │       └── resources/
│   └── pom.xml
│
├── database/
│   └── schema.sql
│
├── screenshots/
│
├── README.md
└── .gitignore
```

## 🧪 Test Cases

| Test Case | Expected Output |
|---|---|
| Gate → Canteen | 150 m |
| Gate → Gate | 0 m |
| Gate → Hostel | Location not found |
| Admin → Canteen (BFS) | 1 Stop |
| Admin → Canteen (Dijkstra) | 100 m |

## 🚀 Future Scope

- GPS-Based Navigation
- Mobile Application
- Voice-Guided Navigation
- QR Code Building Identification
- Real-Time Indoor Positioning
- Floor-Wise Navigation
- Accessibility-Friendly Routes

## 👨‍💻 Developer Details

**Project Name:** Rungta R1 Smart Campus Navigation App

**Developed By:** Harsh Sanjit

**ERP Number:** RU-25-10555

**Teacher In-Charge:** Surya Mani Verma

**College:** Rungta Educational Campus R1, Bhilai, Chhattisgarh

**Problem Statement:** 82

## 📍 Campus Location

Rungta Educational Campus, Kohka–Kurud Road, Bhilai, Chhattisgarh, India.

[View Campus on Google Maps](https://www.google.com/maps/search/?api=1&query=Rungta+Educational+Campus+Bhilai)

## 🤝 Contributions

Suggestions and improvements are welcome.

Feel free to open an issue or submit a pull request.

## 📜 License

This project is developed for academic and educational purposes.

---

<p align="center">
  <b>⭐ Rungta R1 Smart Campus Navigation App ⭐</b>
  <br>
  Making Campus Navigation Smarter, Simpler and Faster!
</p>
