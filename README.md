# Vitiligo Treatment Tracker — native Android project

This project is designed to build in the cloud without Android Studio.

## Schedule included
- Prednisolone 5 mg — 8:30 AM and 9:30 PM daily; after meals
- Tacrolimus — 3:00 PM daily; hands, feet & face
- Steroid cream — 11:00 PM daily; hands, feet & face
- Methotrexate 2.5 mg × 4 tablets = 10 mg total — Sunday 12:45 PM, once weekly
- Phototherapy — Wednesday 9:00 AM
- Folic acid 5 mg — Thursday; default tracker time 12:45 PM and editable in the app

## Reminder behavior
Each scheduled item is armed for 15 minutes before the scheduled time. If it is not marked Done, the reminder repeats every 15 minutes until Done. After Done, the next daily/weekly occurrence is automatically scheduled. Alarms are restored after reboot/app update.

## Build in the cloud
1. Create a GitHub repository.
2. Upload this entire project, preserving `.github/workflows/build-apk.yml`.
3. Open the repository's **Actions** tab and run **Build APK** (or push to `main`).
4. When the workflow finishes, open the run and download the **VitiligoTracker-debug-apk** artifact.
5. On the phone, install the APK. Android may ask you to allow notifications and exact alarms; allow both for reliable reminders.

No Android Studio is required.
