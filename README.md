# 🕓 HarvestTime KMP
<p align="left"> Multi-Platform Harvest Time Tracking clone project built with SwiftUI, Jetpack Compose, Kotlin/Js </p>

<p align="left">
    <a href="https://kotlinlang.org/docs/releases.html">
      <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-1.6.21-blue.svg?style=for-the-badge&logo=appveyor"/>
    </a>
    <a href="https://opensource.org/licenses/Apache-2.0">
      <img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=for-the-badge&logo=appveyor"/>
    </a>
    <a href = "https://github.com/mutualmobile/HarvestTimeKMP/stargazers">
        <img src="https://img.shields.io/github/stars/mutualmobile/HarvestTimeKMP?style=for-the-badge&logo=appveyor" />
    </a>
    <a href = "https://github.com/mutualmobile/HarvestTimeKMP/issues">
        <img src="https://img.shields.io/github/issues/mutualmobile/HarvestTimeKMP?style=for-the-badge&logo=appveyor" />
    </a>
    <a href = "https://github.com/mutualmobile/HarvestTimeKMP/network/members">
        <img src="https://img.shields.io/github/forks/mutualmobile/HarvestTimeKMP?style=for-the-badge&logo=appveyor" />
    </a>
    <a href = "https://github.com/mutualmobile/HarvestTimeKMP/watchers">
        <img src="https://img.shields.io/github/watchers/mutualmobile/HarvestTimeKMP?style=for-the-badge&logo=appveyor" />
    </a>
</p>
<p align="left">
    <a href="https://github.com/mutualmobile/HarvestTimeKMP/actions/workflows/build_firebase_deploy.yml">
      <img alt="Build Status" src="https://github.com/mutualmobile/HarvestTimeKMP/actions/workflows/build_firebase_deploy.yml/badge.svg"/>
    </a>
    <a href="">
        <img src="https://img.shields.io/badge/PRs-WELCOME-brightgreen.svg?style=flat-square&logo=appveyor"/>
    </a>
    <a href = "https://twitter.com/MutualMobile">
        <img src = "https://img.shields.io/twitter/url?label=follow&style=social&url=https%3A%2F%2Ftwitter.com%MutualMobile" />
    </a>
</p>

### 💡 The purpose of this repository:

- Build cross-platform applications and share common code between Android, iOS, Web and Desktop.
- Provide support for different platform with respective Native UI for each, and sharing the common business logic.
- Dependency Injection using Koin.
- Usage of latest Ktor client for Networking.
- Performing background task with Kotlin Coroutines.

### 🔹 Currently running on:

* Android (Jetpack Compose) 🚧 WIP
* Web (Kotlin/JS + React + MUI) 🚧 WIP
* iOS (SwiftUI) 🚧 WIP
* Desktop JVM (Jetpack Compose) 🚧 WIP
* macOS (SwiftUI) 🚧 WIP

Built using [PraxisKMP](https://github.com/mutualmobile/PraxisKMP) as the base project.

## 🏛️ Architecture
<table style="width:100%">
  <tr>
    <td><img src = "art/architecture/harvest_kmp_architecture.png" /></td>
  </tr>
</table>

Architecture Diagram: [Here](https://lucid.app/lucidchart/f4b7e964-9b54-4b9f-b0b9-e797b6b4275a/edit?viewport_loc=158%2C690%2C2767%2C1340%2C0_0&invitationId=inv_0102040b-2279-46f6-ad6c-3228f375a17d#)

## 🌐 API
The Harvest API is written in Kotlin with SpringBoot. Find the repo [here](https://github.com/mutualmobile/HarvestAPISpring).

- Authentication :lock:
  - Find Organization ✅ DONE
  - Sign In User ✅ DONE
  - Sign Up User ✅ DONE
  - Login User ✅ DONE
  - Forgot Password ✅ DONE
  - Change Password ✅ DONE

- List Projects 📽
  - Org admin can create projects ✅ DONE
  - Org admin can list,search projects ✅ DONE
  - Org admin can assign projects to users ✅ DONE
  - more are.. 🚧 WIP

- Logging Time :office:
  - Log Time 🚧 WIP
  - an other


## 🏗️️ Built with ❤️ using Kotlin
| What            | How                        |
|----------------	|------------------------------	|
| 🎭 Android UI   | [Jetpack Compose](https://developer.android.com/jetpack/compose)                |
| 🎭 IOS UI   | [Swift UI](https://developer.apple.com/documentation/swiftui/)                |
| 🎭 Web UI   | [React JS with MUI](https://mui.com/)                |
| 🏗 Architecture    | [Clean](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)                            |
| 💉 DI                | [Koin](https://insert-koin.io/)                        |
| 🌊 Async            | [Coroutines, Flows, KMP Native Coroutines](https://github.com/rickclephas/KMP-NativeCoroutines)                |
| 🌐 Networking        | [Ktor](https://ktor.io/)                        |
| ð Storage       | [Key Value, SqlDelight](https://github.com/russhwolf/multiplatform-settings)                        |


## 📷 Screenshots
- [Android](#android-screenshots)
- [React](#web-screenshots-reactjs)
- [IOS](#ios-screenshots)
- Desktop  🚧 WIP
- MacOS  🚧 WIP

### Android Screenshots
- [OnBoarding](#android-onboarding-screens)
- [Authenticate](#android-authentication-screens)
- [Home](#android-home-screens)
- [Settings](#android-settings-screens)

#### Android OnBoarding Screens
<table style="width:100%">
  <tr>
    <th>OnBoarding One</th>
    <th>OnBoarding Two</th> 
    <th>OnBoarding Three</th>
    <th>OnBoarding Four</th> 
  </tr>
  <tr>
    <td><img src = "art/android_screenshots/android_onboarding_one.png" width=240/></td> 
    <td><img src = "art/android_screenshots/android_onboarding_two.png" width=240/></td>
    <td><img src = "art/android_screenshots/android_onboarding_three.png" width=240/></td> 
    <td><img src = "art/android_screenshots/android_onboarding_four.png" width=240/></td>
  </tr>
</table>

#### Android Authentication Screens
<table style="width:100%">
  <tr>
    <th>Enter Organization Screen</th>
    <th>Sign up Screen</th> 
    <th>Sign In Screen</th>
  </tr>
  <tr>
    <td><img src = "art/android_screenshots/android_enter_org_screen.png" width=240/></td> 
    <td><img src = "art/android_screenshots/android_sign_up.png" width=240/></td>
    <td><img src = "art/android_screenshots/android_harvest_sign_in.png" width=240/></td> 
  </tr>
</table>

#### Android Home Screens
<table style="width:100%">
  <tr>
    <th>Home Screen</th>
    <th>New Entry Screen</th> 
    <th>Home Drawer</th>
  </tr>
  <tr>
    <td><img src = "art/android_screenshots/android_home_screen.png" width=240/></td> 
    <td><img src = "art/android_screenshots/android_new_entry_screen.png" width=240/></td>
    <td><img src = "art/android_screenshots/android_home_drawer.png" width=240/></td> 
  </tr>
</table>

#### Android Settings Screens
<table style="width:100%">
  <tr>
    <th>Settings Screen</th>
    <th>Report Screen</th> 
  </tr>
  <tr>
    <td><img src = "art/android_screenshots/android_setting_screen.png" width=240/></td> 
    <td><img src = "art/android_screenshots/android_report_screen.png" width=240/></td>
  </tr>
</table>

### Web Screenshots (ReactJS)
- Authentication Screens
  - [Verify Organization Screen](#verify-organization-screen)
  - [Sign Up Screen](#sign-up-screen)
  - [Sign In Screen](#sign-in-screen)
  - [Log In Screen](#log-in-screen)
  - [Forgot Password Screen](#forgot-password-screen)
- Home Screens
  - [Home Screen](#home-screen)
  - [Home Drawer Screen](#home-drawer-screen)
  - [All User Screen](#all-user-screen)
  - [All Projects Screen](#all-project-assignment-screen)
  - [Settings Screen](#settings-screen)

#### Verify Organization Screen
<table style="width:100%">
  <tr>
    <th>Verify Organization</th>
  </tr>
  <tr>
    <td><img src = "art/react/web_enter_org_screen.png" /></td>
  </tr>
</table>

#### Sign Up Screen
<table style="width:100%">
  <tr>
    <th>Sign Up</th>
  </tr>
  <tr>
    <td><img src = "art/react/web_sign_up_screen.png" /></td>
  </tr>
</table>

#### Sign In Screen
<table style="width:100%">
  <tr>
    <th>Sign In</th>
  </tr>
  <tr>
    <td><img src = "art/react/web_sign_in_form.png" /></td>
  </tr>
</table>

#### Log In Screen
<table style="width:100%">
  <tr>
    <th>Log In</th>
  </tr>
  <tr>
    <td><img src = "art/react/web_login_screen.png" /></td>
  </tr>
</table>

#### Forgot Password Screen
<table style="width:100%">
  <tr>
    <th>Forgot Password</th>
  </tr>
  <tr>
    <td><img src = "art/react/web_forgot_password_screen.png" /></td>
  </tr>
</table>

#### Home Screen
<table style="width:100%">
  <tr>
    <th>Home</th>
  </tr>
  <tr>
    <td><img src = "art/react/web_home_Screen.png" /></td>
  </tr>
</table>

#### Home Drawer Screen
<table style="width:100%">
  <tr>
    <th>Home Drawer</th>
  </tr>
  <tr>
    <td><img src = "art/react/web_drawer_screen.png" /></td>
  </tr>
</table>

#### All User Screen
<table style="width:100%">
  <tr>
    <th>All User</th>
  </tr>
  <tr>
    <td><img src = "art/react/web_all_users.png" /></td>
  </tr>
</table>

#### All Project Assignment Screen
<table style="width:100%">
  <tr>
    <th>All Project Assignment</th>
  </tr>
  <tr>
    <td><img src = "art/react/web_project_assignments_screen.png" /></td>
  </tr>
</table>

#### Settings Screen
<table style="width:100%">
  <tr>
    <th>Settings</th>
  </tr>
  <tr>
    <td><img src = "art/react/web_settings_screen.png" /></td>
  </tr>
</table>


### iOS Screenshots
- [Authentications](#ios-authentications-screen)

#### iOS Authentications Screen

<table style="width:100%">
  <tr>
    <th>On Boarding Screen</th>
    <th>Sign In Screen</th> 
  </tr>
  <tr>
    <td><img src = "art/ios/ios_onBoarding_one.png" width=240/></td> 
    <td><img src = "art/ios/ios_sign_in.jpg" width=240/></td>
  </tr>
</table>


### 🪪 License
```
Copyright 2022 Mutual Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
  ```
