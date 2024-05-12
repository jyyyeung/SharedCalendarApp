COMP4521 Mobile Application Development
Final Report

Project topic: Shared Calendar
Group number:  Group 8
Group member:
NG Hon Yung  (20779231) (Leader)
YEUNG Jasmine Yat Yau (20794918)

Word count: nnnn (not more than 5000)

Date: 12/05/20014
Table of Content
1 Introduction
1.1 Overview
1.2 Objective
1.3 Limitation and Challenges
2 Project Overview
2.1 Gantt chart
2.2 Distribution of work
3 User Requirement
3.1 Use case
3.1.1 Add event
3.1.2 Edit event
3.1.3 Add person to calendar
3.1.4 Register new account
4 Design and architecture
4.1 Front-end design
4.2 Back-end design
5 Development
5.1 Front-end development
5.1.1 Recycler view
5.1.2 Spinner
5.1.3 Datepicker and Timepicker
5.1.4 Sidebar drawer
5.2 Backend development
6 Testing
6.1 Unit testing
6.2 Blackbox testing
7 Conclusion
8 Future work
9 Referenced libraries

1 Introduction
1.1 Overview

The average American adult has a mere 4 hours and 26 minutes of free time per week [1], indicating a
daily schedule brimming with activities and appointments. With such a packed agenda, it’s not
uncommon for individuals to overlook an appointment or forget a meeting. The situation becomes
particularly chaotic when attempting to coordinate social activities. Imagine planning to hang out
with a friend; you both agree on a suitable time, only to discover a conflicting event. To manage
their schedules, many people resort to using physical calendars or digital calendar applications.
However, these tools often lack the functionality to share schedules with others [2], adding an
extra layer of inconvenience when arranging appointments.

To address this scheduling challenge, we introduce a shared calendar application designed to
simplify social planning with couples, friends, and family. This application allows users to invite
others to collaborate on a shared calendar, reserving time slots for events. Users can input the
time and title for each event in their schedule, clearly indicating their unavailability during
these periods. This transparency makes it easy to identify the best time that accommodates everyone
when planning group activities.

We recognize that repeatedly asking each member for their availability can be tedious. Hence, we
propose this shared calendar application as a solution, aiming to revolutionize the way we
socialize.

1.2 Objective
The objective of this project is to develop a shared calendar application that simplifies group
scheduling.

To accomplish this, we have outlined the following sub-goals:
Design and Develop a User-Friendly Mobile Application for Android Users
Enable users to visualize their schedules on the dashboard.
Incorporate the schedules of other group members into the calendar.
Ensure intuitive navigation within the application.
Allow event details to accommodate multiple attributes.
Develop a Robust Backend Database
Ensure the database is stable and robust to support the application’s functionalities.
Implement security measures for both data-in-transit and data-at-rest to safeguard user privacy.
Embrace a modular database design for future scalability and seamless integration with diverse
platforms.
Build calendar view
Calendar should be accurate in day of week, and can hold multiple events
Month, week and day view should show events accurately menu page
Main menu page
Most of the setting are hidden within the main menu and its sub page, to ensure simplicity in the
main page
Animation
Animation can make the application feel more responsive and smooth, thus improving the user
experience

Given the aforementioned objectives, users will be able to:
Manage their daily schedule with ease.
Swiftly pinpoint mutual free slots with friends.
Promptly inform their family about urgent changes in their schedule.
Seamlessly transition to and adopt our application.
1.3 Limitation and Challenges
The paramount hurdle lies in architecting a robust backend database that can efficiently manage
calendars and user data. Given the plethora of database options available, the selection of an
optimal database becomes a critical determinant of the project’s success. Moreover, as our
application necessitates data transmission over the internet, it is imperative to guarantee the
utmost security of our chosen technologies and implementation strategies.

2 Project Overview
The overall project went mostly according to our initial planning in the proposal. Below is the
gantt chart and the work distribution that shows our work progress.
2.1 Gantt chart

Task
Week
7
8
9
10
11
12
13
Write Proposal

Local Development Environment Setup

Design User Interface

Design Software Logic

Design DB Schema & PHP API

Implement User Interface

Implement PHP API & Database Connection

Implement Software Control & Logic

UI Testing

Database Testing

Integration: Combining Front- and Back-end

Full Stack Application Testing

Debugging & Correcting Issues

Complete Final Report

Preparation for Presentation

2.2 Distribution of work
Task
Jasmine
Alex
Design
Design User Interface
○
✓
Design Software Logic
○
✓
Design Database Structure
✓
○
Development
Implement User Interface
○
✓
Implement Database Connection
✓
○
Implement Software Control & Logic
○
✓
Integration: Combining Front- and Back-end
✓
○
Testing
UI Testing
○
✓
Database Testing
✓
○
Full Stack Application Testing
✓
✓
Debugging & Correcting Issues
✓
✓
Report
Final Report
✓
✓
Preparation for Presentation
✓
✓

✓ Leader ○ Assistant
3 User Requirement
This application provides a convenient way to manage the timetable within a group. The interface
should be easier to understand, and view the timetable of the whole group in a glance. Users can add
participants to the calendar, add, edit and delete events.
3.1 Use case
3.1.1 Add event
Actors: User
Precondition: User is logged in to the calendar
Main flow:
User click the add event button
User input the information about the event to be added
User press the save button
Postcondition: New event is created in the calendar and database
3.1.2 Edit event
Actors: User
Precondition: There exist a event to be edit
Main flow:
User click on the day cell of the monthly view, or the day on the weekly view
A list of event will appears on the window, user click on one of the event
The edit event window will slide up. User change the information about the event
User press the “Save” button
Postcondition: The event information is now changed
3.1.3 Add person to calendar
Actors: User
Precondition: Both users have an account to our application
Main flow:
Calendar ower open up the setting sidebar, and navigate to “Add person to calendar”
Choose the calendar he wish to share, input the email of the target user, and select the permission
of that user.
Click on “Save” button
3.1.4 Register new account
Actors: User
Precondition: User is not logged in to the calendar
Main flow:
User swipe left to open up the register page
User input the information required to the corresponding field
User click “Register” button
Postcondition: User is registered to the system, and logged in
4 Design and architecture
4.1 Front-end design
From the diagram,the application mainly consists of two activities, the login and register activity
and main calendar activity. The login and register activity will only be accessed when the user is
not logged in to an account. If the user has not registered before, he will go to the register page
to register an account. If the user has an account before, then he can login using the login page.
After finishing from either page, the user will be brought to the main calendar activity.

The main calendar activity consists of the calendar view, setting sidebar and the edit event window.
The calendar view includes a monthly view, and a weekly view. The user can proceed to a day view by
clicking on the specific day on the monthly view or weekly view. On the day view, user can click on
the specific event and go to the edit event window. User can also add event by pressing the add
event button, and open up the edit event window. Lastly, user can either swipe right or by pressing
the hamburger icon on the top left to open up the setting sidebar. The setting sidebar contains
buttons that link to other settings pages.

Below are screenshot from the above activity view:

## 4.2 Back-end design

### Database Design

The backend of the application is built using Firebase and Kotlin. Firebase is a cloud-based
platform that provides a variety of services, including authentication, real-time database, and
cloud storage. We chose Firebase for its ease of use and scalability, as it allows us to focus on
developing the application rather than managing the infrastructure. The backend consists of the
following components:

Authentication: Firebase Authentication is used to handle user registration and login. Users can
create an account using their email and password, and log in to access the application. We believe
it is more secure to use existing authentication methods from a trusted third-party, compared to
creating our own authentication mechanism in this short time limit.

Database Storage: Firestore is a NoSQL database that stores user data, event data, and calendar
data. The database is structured to support efficient queries and updates, enabling users to manage
their events and share events with others.

Local Storage: SharedPreferences is used to store user preferences and settings, such as the user's
preferred view mode (monthly, weekly, or daily) and which calendars to display. This allows the
application to remember the user's preferences across sessions. The preferences are separated per
account, so that each user can have their own settings. The format of the key is “${User ID}
_{category}_{field}”.

### 4.2.1 Dataflow of the application

The data flow of the application is as follows:
User Provides data through input fields and forms
Event Listeners attached to views listens for user interaction
The data is sent to the view models for processing
If storage is needed, connect to firebase and store data
The view models query database to get updated data
The data is sent back to view models
View Model observers listens for data update and refreshes the frontend as needed
Example:
Users log in to the application using their email and password.
The application authenticates the user with Firebase Authentication and retrieves the user's
calendar and event data from Firestore.
The user's calendar data is displayed in the calendar view, showing events for the selected day,
week, or month.
The user can add, edit, or delete events in the calendar, which are stored in Firestore.
The user can share their calendar with others by adding them as collaborators, who can view and edit
events in the calendar.
The application stores user preferences and settings in Local SharedPreferences, allowing the user
to customize their experience.
The user can log out of the application, which clears their session data and returns them to the
login screen.
The application securely stores user data and settings in Firebase and Local SharedPreferences,
ensuring that the user's information is protected and accessible across sessions.

### 4.2.2 Database design

Users Collection: Stores user data, including user ID, email, and name.
Calendars Collection: Stores calendar data, including calendar ID, owner ID, name, and color.
Events Collection: Stores event data, including event ID, calendar ID, title, description, start
time, end time, and color tag.
Shares Collection: Stores share data, including Share ID, calendar ID, target User email, and Share
scope.
The database design is structured to support efficient queries and updates, enabling users to manage
their schedules and share events with others. The collections are organized by user, calendar,
event, and share, allowing users to access and update their data easily. The database schema is
designed to be scalable and flexible, accommodating future features and enhancements.
Overall, the backend provides a reliable and scalable foundation for the application, allowing users
to manage their schedules and share events with others. The combination of Firebase and Kotlin
enables us to build a robust and user-friendly shared calendar application that meets the needs of
our target users.

## 5 Development

### 5.1 Front-end development

#### 5.1.1 Recycler view

The Day view contains a recycler view, to display a list of items on that day. The items in the
recycler view have an on click listener, which will record the item that the user clicks, and open
up the event edit window for the user to update the event.

#### 5.1.2 Spinner

The edit event window and add event window both contain a spinner. The spinner is used for selecting
the color tag for that event. To make the color blob and the text align with other text, the spinner
is hidden from the view, and only the result is displayed on the text view and image view. The
spinner itself is activated by clicking on the text view.

#### 5.1.3 Datepicker and Timepicker

Both the edit event window and add event window contain a datepicker and timepicker. These are used
for picking the start time and end time of the event, and are triggered by clicking on the
corresponding text view. The result is also shown on the text view.

#### 5.1.4 Sidebar drawer

The application includes a sidebar drawer, for users to easily access the setting menu. Since the
setting are not a daily use function, hiding them inside the sidebar is the best option clear up the
UI for the user, and make the application more intuitive.

### 5.2 Backend development

## 6 Testing

Testing helps to ensure the reliability of the application. This project includes unit testing and
black box testing to help to test for anomalies.
6.1 Unit testing
Unit testing is the technique that focuses on testing the programme unity by unit. Individual test
cases are designed to test each part, and a report showing coverage and success rate is generated.
Our code has reached 50% code coverage, and 82% success rate.

6.2 Blackbox testing
Black box testing is the technique of flooding the system with random input, without knowing the
code. In our project, we aim to focus on invalid input from the user, for example inputting the
wrong type to the field, and the end result is outstanding. This is because we include multiple
checks in our programme, including checking for invalid input in the database and in the UI.

7 Conclusion
In conclusion, the shared calendar app has been successfully implemented, creating a platform for
groups and communities to share their schedule and showing events to be held. Our target users are
not limited to a group of friends, but also couples, study group, event holders and more. The Shared
Calendar deviates from the traditional calendar, symbolizing our commitment in making changes,
reflecting our creativity spirit and our engagement in communities.

During the development process, we have learned practical skills, and became familiar with android
studio and kotlin. The integration between frontend and backend was also a valuable experience, as
it helps to strengthen our programming knowledge, and working with logic and algorithms.
Furthermore, the project also helps to improve our team working spirit, dividing work among the
members and finishing tasks on time. Overall, we think this is an important experience that will
benefit us in not only developing in android, but also our programmer career.

8 Future work
Even though we have met our initial objectives, goals and expectations, we have identified some
places that we can improve upon. Continuous development is an important stage for every application,
and we reckon that our application can have improvement in multiple areas as well.
Firstly, we could have further improved our UI design. The current UI design is clear, showing every
information that the user needs, and the navigation is simple, and we are satisfied with the current
UI. However, the color tone, layout design, and also the animation, can surely have some further
improvement. Especially layout design, the locations of buttons, and where the textbox is placed,
can also be worked on, so that it is more aesthetically pleasing.
Next, we can also improve on further functionality. Currently the description only allows words. But
users may wish to store an image, or recording, as a reminder to the event. We think this can
benefit different user groups as well, since event holders may wish to put up leaflets for the
event.

9 Referenced libraries
Below is a list of libraries referenced in our project, to aid our development. We hereby show our
gratitude to the developer of these libraries, and recognise their contribution:
