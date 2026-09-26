# Job Application Manager

A desktop application built with Java and JavaFX for tracking job applications, interview progress, application stages, notes, and statistics in one place.

The project was created to make managing graduate and professional job applications easier by replacing spreadsheets and scattered notes with a dedicated desktop application.

## Features

### Job Application Tracking
- Add, edit and delete job applications
- Store:
  - Company
  - Role
  - Salary
  - Location
  - Application date
  - Current application status
  - Job description
  - Personal notes

  <br>
  <img width="877" height="282" alt="image" src="https://github.com/user-attachments/assets/e8c7864b-581d-498e-b39d-acd73a121b2a" />


### Application Progress
- Track application stages:
  - Applied
  - Online Test
  - Interview
  - Offer
  - Rejected

  <br>
  <img width="164" height="236" alt="image" src="https://github.com/user-attachments/assets/c361183e-93f3-4bb3-bd32-d56a382a3c45" />

- Maintain a history of stages each application has reached
- View current application statistics
- View historical statistics showing how many applications reached each stage
- Pie chart showing the distribution of current application statuses

<br>
<img width="733" height="586" alt="image" src="https://github.com/user-attachments/assets/481c5db7-ceae-42a9-8cb1-40935441a8a1" />


### Interview Management
- Manage interviews linked to individual applications
- Store:
  - Interview date and time
  - Interview type
  - Location or meeting link
  - Interview notes
  - Upcoming interview reminders

  <br>
  <img width="685" height="318" alt="image" src="https://github.com/user-attachments/assets/b2cca6bd-ecfa-4bbe-a10c-d6b2aaf3e065" />


### Other Features
- Search applications by company, location or entry number
- Filter applications by status
- Pagination for application records
- Automatic deletion of associated interviews and status history when an application is deleted
- Input validation for application and interview data
- SQLite database persistence
- Dark JavaFX interface with custom CSS styling
- Windows installer created using `jpackage`

## Technology Stack

- **Java 21**
- **JavaFX**
- **Maven**
- **SQLite**
- **JDBC**
- **CSS**
- **Git / GitHub**
- **jpackage**
- **WiX Toolset**

## Project Structure

```text
src/main/java/com/pratik/jobtracker
│
├── database
│   ├── Database.java
│   ├── JobApplicationSNL.java
│   ├── InterviewSNL.java
│   └── ApplicationStatusHistorySNL.java
│
├── model
│   ├── JobApplication.java
│   ├── Interview.java
│   ├── ApplicationStatus.java
│   └── ApplicationStatusHistory.java
│
├── service
│   ├── ApplicationService.java
│   └── InterviewService.java
│
├── ui
│   ├── AddApplicationView.java
│   ├── EditApplicationView.java
│   ├── InterviewManagementView.java
│   ├── AddInterviewView.java
│   ├── EditInterviewView.java
│   └── StageHistoryStatsView.java
│
├── Launcher.java
└── Main.java
