// Scripts for firebase and firebase messaging
importScripts('https://www.gstatic.com/firebasejs/7.24.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/7.24.0/firebase-messaging.js');

// Initialize the Firebase app in the service worker by passing the generated config
var firebaseConfig = {
  apiKey: "AIzaSyAEKlvIkYeG9rcZ1FuA7KOJoiLn1o4t1YU",
  authDomain: "harvestkmp.firebaseapp.com",
  projectId: "harvestkmp",
  storageBucket: "harvestkmp.appspot.com",
  messagingSenderId: "877837074584",
  appId: "1:877837074584:web:0d32488e576994a668d0ed"
};

firebase.initializeApp(firebaseConfig);

// Retrieve firebase messaging
const messaging = firebase.messaging();

messaging.onBackgroundMessage(function(payload) {
  console.log('Received background message ', payload);

  const notificationTitle = payload.notification.title;
  const notificationOptions = {
    body: payload.notification.body,
  };

  self.registration.showNotification(notificationTitle,
    notificationOptions);
});