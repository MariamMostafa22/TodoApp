<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#key-features">Key Features</a></li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#technologies-used">Technologies Used</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#additional-notes">Additional Notes</a></li>
    <li><a href="#license">License</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About the Project
This is a simple to-do list Android application built using Kotlin. The app allows users to manage their tasks (add, edit, delete) and save them to their account. 
User and task data are stored locally using Room, a SQLite-based persistence library.

### Built With
* [![Kotlin][Kotlin-badge]][Kotlin-url]
* [![Android][Android-badge]][Android-url]

## Key Features
- User Authentication
  - Sign up with email, username, and password.
  - Log in to access personalized task data.
  
- Task Management
  - Add new tasks.
  - Edit existing tasks.
  - Delete tasks.
  - Mark tasks as complete.

- Data Persistence
  - User and task data are stored locally using the Room database.

<!-- GETTING STARTED -->
## Getting Started
This section explains how to run this project on your pc.

### Prerequisites
You need to have **Android Studio** installed.

###  Installation
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/MariamMostafa22/TodoApp.git
   ```
3. **Open in Android Studio:** Open the project in Android Studio.
4. **Build and Run:** Build the project and run it on an Android emulator or device.

<!-- USAGE EXAMPLES -->
## Usage
1. **Create an Account:** Register a new account using the provided form.

![sign-up](https://github.com/user-attachments/assets/e599e2b9-fcbe-42fa-87f5-365a36d17fe5)

2. **Login:** Log in to your account using your credentials.

![login](https://github.com/user-attachments/assets/f617ff9f-68b8-4e50-8326-16e66a5f1c32)

3. **Add Tasks:** Tap the "+" button to create a new task.

![add-task](https://github.com/user-attachments/assets/af36c7c7-1c3d-46de-85de-32147a1b4b2f)

4. **Edit Tasks:** Tap on a the more button attached to a task, then select "Edit" to edit its details.

![edit-task](https://github.com/user-attachments/assets/217d4922-85b2-4f9d-812a-c61f582e058e)

5. **Delete Tasks:** Tap on a the more button attached to a task, then select "Delete" to delete it.

![delete-task](https://github.com/user-attachments/assets/5826cdb4-1837-42fb-9659-de2ebb300bc4)

<!-- Tech Stack -->
## Technologies Used
- **Language**: Kotlin
- **Database**: Room (SQLite)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Android Navigation Component (Safe Args)
- **UI**: Jetpack Components, XML Layouts

## Contributing
Contributions are welcome! Please feel free to fork the repository, make your changes, and submit a pull requests for any features, improvements, or bug fixes.

## Additional Notes
- You can customize the app's appearance and functionality to your preferences.
- Consider adding features like task categorization, reminders, and synchronization with cloud storage.

## License
This project is licensed under the MIT License.

<!-- LINKS & IMAGES -->
[Kotlin-badge]: https://img.shields.io/badge/Kotlin-%237F52FF?style=for-the-badge&logo=kotlin&logoColor=white
[Kotlin-url]: https://kotlinlang.org/
[Android-badge]: https://img.shields.io/badge/Android-%2334A853?style=for-the-badge&logo=android&logoColor=white
[Android-url]: https://developer.android.com/
