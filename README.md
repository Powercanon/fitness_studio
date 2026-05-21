
## 📋 Project Overview
**Fitness Studio** is a comprehensive Java-based REST API for managing a fitness studio's operations. Built with Spring Boot 4.0.6, Spring Security, and PostgreSQL, it provides complete functionality for managing members, courses, trainers, and facility check-ins.

---

##  Core Features

### 1. **Authentication & Authorization**
- User login with JWT token-based authentication
- Role-based access control (User roles including ADMIN)
- Secure logout functionality
- CORS support for frontend integration (localhost:3000)

### 2. **Member Management**
- **Member Registration**: Create and manage member profiles
- **Member Information**: Store comprehensive member details including:
  - Auto-generated unique member numbers (format: M-YYYY-NNNNN)
  - First and last name
  - Email and phone contact information
  - Birth date and gender
  - Contract model and contract start date
- **Member Status Tracking**: Active/Inactive status management
- **Advanced Filtering**: Filter members by:
  - Membership status
  - Gender
  - Contract model
  - Contract date range
  - Search functionality
- **Sorting**: Customizable sorting by multiple fields
- **Member Deletion**: Admin-only member removal

### 3. **Course Management**
- **Course Creation & Scheduling**: Create fitness courses with detailed information:
  - Course name and description
  - Category classification
  - Difficulty levels (various difficulty options)
  - Assigned trainer
  - Maximum participant capacity
  - Specific date and time scheduling (from time to end time)
  - Course status management
- **Course Listing**: Retrieve all available courses
- **Course Schedule**: View upcoming course schedules
- **Advanced Course Filtering**:
  - Filter by status (Active/Cancelled)
  - Filter by category
  - Filter by difficulty level
  - Filter by trainer
  - Filter by date range
  - Filter by availability
  - Full-text search
- **Sorting Options**: Multiple sort criteria and directions (ascending/descending)
- **Course Updates**: Modify existing course details
- **Course Cancellation**: Cancel courses with status updates

### 4. **Course Registration**
- **Member Registration**: Members can register for available courses
- **Registration Tracking**: Monitor all course registrations
- **Registration Status**: Track registration states (Active/Cancelled/Completed)
- **Cancellation**: Members can cancel their course registrations
- **Capacity Management**: Automatic validation of course capacity
- **Authentication Integration**: Links registrations to authenticated members

### 5. **Check-in/Check-out System**
- **Facility Check-in**: Record member entry into the fitness studio
- **Facility Check-out**: Track member departure
- **Check-in Tracking**: Maintain complete history of facility visits
- **Capacity Monitoring**: Real-time facility capacity tracking and reporting
- **Advanced Check-in Filtering**:
  - Filter by check-out status
  - Filter by date range
  - Search functionality
- **Sorting**: Sort check-in records by timestamp or other criteria
- **Check-in Status**: Track checked-in and checked-out members

### 6. **Trainer Management**
- Trainer profiles and information
- Trainer assignment to courses
- Trainer retrieval and management

### 7. **Data Models & Enumerations**
- **User Roles**: Admin, User roles for permission management
- **Member Status**: Active, Inactive states
- **Course Status**: Active, Cancelled states
- **Course Categories**: Various fitness course types
- **Course Difficulty**: Multiple difficulty levels
- **Gender**: Male, Female gender options
- **Contract Models**: Different membership contract types
- **Registration Status**: Active, Cancelled, Completed tracking

---

## 🔧 Technical Stack
- **Language**: Java 25
- **Framework**: Spring Boot 4.0.6
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT (JJWT 0.12.3)
- **Persistence**: Spring Data JPA
- **Validation**: Spring Validation framework
- **Build Tool**: Maven
- **Additional Libraries**: Lombok (1.18.38) for code generation

---

---

## 📡 API Endpoints Summary

| Feature | Endpoints |
|---------|-----------|
| **Auth** | POST `/api/auth/login`, POST `/api/auth/logout` |
| **Members** | GET/POST/PUT/PATCH/DELETE `/api/members`, GET `/api/members/{id}` |
| **Courses** | GET/POST/PUT/PATCH `/api/courses`, GET `/api/courses/{id}`, POST/DELETE `/api/courses/{id}/register`, GET `/api/courses/schedule` |
| **Check-in** | GET/POST `/api/checkin`, GET `/api/checkin/capacity`, PATCH `/api/checkin/{id}/checkout` |
| **Trainers** | Multiple trainer-related endpoints |

---

