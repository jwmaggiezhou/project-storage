# CSC Chatbot

-----

This is an app using the Django framework and the Python library ChatterBot with the purpose of answering questions related to the SPC application.

### Features
* Trained with about 400 responses to date (SPC specific and general greetings)
* NLTK English stopword removal
* SPC entity replacement with their acroynms
* Emoji removal from the Slack hosted bot
* MongoDB Text Indexes for more efficient response searches
* Separate training and running process
* Slack integration
* SPC Mobile integration

## Installation & Set up
### Local Django Webpage
First ensure that you have a Python 2.7 or 3.6 installed. If it is your first time running the chatbot, run `pip install django chatterbot` in the command line. Then, run `python manage.py runserver`. Then open up a browser and go to http://127.0.0.1:8000 to view your local chatbot django page.

### Slack Integration
Same as for the django webpage, make sure you have Python installed. For first time running the slack integration of chatbot, run `pip install slackclient` in the command line. Then set the environment variable for slack bot token and the bot id following the instructions in slack_chatbot/info.txt. Run `python slack_testbot.py` and log on to Slack workspace to start chatting

## Links
Further documentation can be found in the links below.

* ChatterBot Src: https://github.com/gunthercox/ChatterBot
* ChatterBot Documentation: http://chatterbot.readthedocs.io/en/latest/django.html
* Django: https://www.djangoproject.com
* NLTK Python Libraries: http://www.nltk.org/
* MongoDB Text Indexes: https://docs.mongodb.com/manual/core/index-text/

![Chatterbot logo](http://salvius.org/images/logos/chatterbot.png)
![Django logo](http://artandlogic.com/wp-content/uploads/2014/04/django-logo-negative-300x136.png)
![NLTK logo](https://tse4.mm.bing.net/th?id=OIP.sR1Ar05uovPTypL_2atpcQCWCW&pid=15.1)
![MongoDB logo](http://idroot.net/wp-content/uploads/2015/02/mongodb-logo.jpg)