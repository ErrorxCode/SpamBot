# SpamBot
<p align="left">
  <a href="#"><img alt="Windows OS" src="https://img.shields.io/badge/Windows-0078D6?style=flat-square&logo=windows&logoColor=white"></a>
  <a href="#"><img alt="Languages-Java" src="https://img.shields.io/badge/Linux-FCC624?style=flat-square&logo=linux&logoColor=black"></a>
  <a href="#"><img alt="Languages-Java" src="https://img.shields.io/badge/Language-Java-1DA1F2?style=flat-square&logo=java"></a>
  <a href="#"><img alt="Bot" src="https://img.shields.io/badge/Bot version-v2-orange"></a>
  <a href="https://www.instagram.com/x__coder__x/"><img alt="Instagram - x__coder__" src="https://img.shields.io/badge/Instagram-x____coder____x-lightgrey"></a>
  <a href="#"><img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/ErrorxCode/OTP-Verification-Api?style=social"></a>
  </p>

#### A java programm which automatically comment on youtube videos based on your keywords. This bot will help you to grow your channel. You can customize bot according to you by changing configuration. Please see Configuration guide below.

## Requirements
* Chrome or firefox
* Windows or linux
* A stable Internet connection
* A fresh or less secure google account

## Usage
1. Open keywords.txt
2. Enter your keyword one per line
3. Open config.txt
4. Change config according to your need.
5. Run SpamBot.exe

## Exception/Error
~~If you are not able to login to google account then try~~ :- 
1. *Disabling 2 factor authentication (from your account settings)*
2. *turn on less secure apps (from your account settings)*
3. *Check if Javascript is enabled or not if not , then enable it (from browser settings)*
4. *Make another google account with no security*


<!-- 
### **For Instagram** :- 
1. Open tags.txt
2. Enter tags ( one per line ) on which you want to comment
3. Run Insta.py (`python3 YT.py` for linux) -->

<!-- 
**Note : For linux users who are using firefox, you have to set geckodriver in $PATH in order to work. for that Run the following commands in the terminal** :-
- `sudo mv geckodriver /usr/local/bin/ (from the project directory)`
- `chmod +x geckodriver (from the /usr/local/bin/ directory )` -->


## Configuration Reference


| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| FILTER_UPLOAD | `string` | Enter anyone form "month","today","week" |
| VIDEO_PER_SEARCH  | `int`    | Enter the no. of videos to grab per search                       |
| SPAM_1_VIDEO      | `boolean` | Enter true, if you want to spam a perticular video. false otherwise |
| SPAM_COUNT | `int` |Enter the no. of comments for which *SPAM_1_VIDEO is true* |
| SPAM_VIDEO_URL  | `string` | Enter the video link for which *SPAM_1_VIDEO is true* |
| USERNAME  | `string` | Enter your google account username  |
| PASSWORD  | `string` | Enter your google account password  |
| COMMENT  | `string` | Enter your comment  |
| BROWSER  | `string` | Enter anyone from "chrome" or "firefox"  |

  
## Config Example

```
FILTER_UPLOAD = month
VIDEO_PER_SEARCH = 10
SPAM_1_VIDEO = false
SPAM_COUNT = 0
SPAM_VIDEO_URL = https://youtube.com/watch?v=MDLDDunsLqf
USERNAME = your_google_username_here
PASSWORD = your_google_password_here
COMMENT = Get this bot : https://github.com/ErrorxCode/SpamBot
BROWSER = chrome
```

## Contributing

When contributing to this repository, please first discuss the change you wish to make via issue,
email, or any other method with the owners of this repository before making a change. 

Please note we have a code of conduct, please follow it in all your interactions with the project.

## Pull Request Process

1. Ensure any install or build dependencies are removed before the end of the layer when doing a 
   build.
2. Update the README.md with details of changes to the interface, this includes new environment 
   variables, exposed ports, useful file locations and container parameters.
3. Increase the version numbers in any examples files and the README.md to the new version that this
   Pull Request would represent. The versioning scheme we use is [SemVer](http://semver.org/).
4. You may merge the Pull Request in once you have the sign-off of two other developers, or if you 
   do not have permission to do that, you may request the second reviewer to merge it for you.
   
   
## License 
```

```
