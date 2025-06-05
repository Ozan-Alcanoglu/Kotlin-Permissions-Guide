This project is a simple Android application demonstrating runtime permission handling using Jetpack Compose. It showcases both automatic permission requests on screen launch and manual permission requests triggered by user interactions.

-Features
Runtime permission handling for common Android permissions (Camera, Storage, Location, etc.)

Support for modern permission model targeting SDK 33+ (including Android 13+ media permissions)

Two screens:

MainScreen: Automatically requests permissions on launch

SecondScreen: Allows manual permission requests via buttons

User feedback on permission status via Toast messages

Built entirely with Jetpack Compose UI toolkit

Image loading with Coil

-Project Structure
MainScreen: Entry screen where permissions are requested automatically when the screen appears.

SecondScreen: Screen with buttons allowing users to request specific permissions manually.

PermissionItem: Data class storing permission name and corresponding manifest permission string.

Permissions are requested using rememberLauncherForActivityResult with ActivityResultContracts.RequestPermission.

-How It Works
When the app launches, MainScreen automatically triggers permission requests.

Users can grant or deny permissions; results are shown with Toasts.

Navigating to SecondScreen allows users to manually request various permissions by tapping buttons.

Denied permissions can be requested again unless the user selects “Don’t ask again” in the system dialog.

The app supports new permission types introduced in Android 13 (e.g., READ_MEDIA_IMAGES, POST_NOTIFICATIONS).

-Permissions Used
READ_EXTERNAL_STORAGE / READ_MEDIA_IMAGES (Android 13+)

RECORD_AUDIO

ACCESS_FINE_LOCATION

CAMERA

POST_NOTIFICATIONS (Android 13+)

SEND_SMS

READ_CONTACTS

CALL_PHONE

-Notes
The “Don’t ask again” option is controlled by the Android system and cannot be reset programmatically.

Android 13 and above use updated media permissions such as READ_MEDIA_IMAGES.

If a permission is denied, users can request it again via buttons on SecondScreen.

Proper user education and rationale dialogs are recommended for production apps.


