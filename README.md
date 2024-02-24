# ID/Passport Photo Recognition App

This app uses **tflite** for photo recognition of the image from ID/Passport and comparing it with user selfie. It also allows the user to read the **NFC data** from the document, save it inside the database, and then take a picture. After that, the user can compare his face with the image of the document with the **FaceNet** and **MobileFaceNet** models that were developed on **python** and **tensorflow**, and then optimized to use on **android** and **tflite**.

## Features

- Scan and extract the information from the **MRZ** (Machine Readable Zone) of the ID/Passport using **OCR** (Optical Character Recognition).
- Read the **NFC data** from the document and store it in a local database using **Room**.
- Capture a selfie of the user and compare it with the image of the document using **FaceNet** and **MobileFaceNet** models for **face recognition** and **verification**.
- Display the results of the comparison and the confidence score using **Material Design** components.


## Installation

To install and run the app, follow these steps:

- Clone this repository:
```bash
git clone https://github.com/HamedMajdi/Passport-Photo-Verification.git
cd Passport-Photo-Verification
```

- Open the project in **Android Studio** and sync the Gradle files.
- Connect your device or emulator and run the app.

## Usage

To use the app, follow these steps:

- On the main screen, tap on the **Scan ID/Passport** button to start the scanning process.
- Align the document with the camera and wait for the app to detect and extract the MRZ information. You can also enter the data manually if the OCR doesn't work in your case.
- Tap on the **Read NFC** button to read the NFC data from the document. Make sure your device supports NFC and it is enabled.
- Tap on the **Take Picture** button to capture a selfie of yourself. Make sure your face is clearly visible and well-lit.
- Tap on the **Compare** button to compare your face with the image of the document using the FaceNet and MobileFaceNet models. The app will display the results and the confidence score on the next screen.

## Bugs

This app is still in development and may have some bugs or limitations. Here are some known issues:

- The OCR for recognizing the MRZ seems to have some problems, due to the limitations of the test. It is recommended to enter the data manually if the OCR doesn't work in your case. I'm working to find and solve the problem.
- The NFC data may not be available or readable for some documents or devices. Make sure your document and device support NFC and it is enabled.
- The face recognition and verification models may not be accurate or reliable for some faces or conditions. Make sure your face and the document image are similar and well-lit.
