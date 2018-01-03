# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
import os

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.8/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = 'fsch+6!=q+@ol&%0x!nwdl@48^ixbd4clx5f1i!5n^66y+pmn*'

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

ALLOWED_HOSTS = ['*']


# Application definition

INSTALLED_APPS = (
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',

    'chatterbot.ext.django_chatterbot',
    'example_app',
)

# ChatterBot settings

CHATTERBOT = {
    'use_django_models' : 'false',
    'storage_adapter' :'example_app.custom_storage.MongoDatabaseIndexAdapter',
    'database': 'chatterbot-database',
    'database_uri': 'mongodb://chatter:smartyBot@csczikdcapmdw02:27017',
    #'database_uri': 'mongodb://142.108.72.27:27017',
    'name': 'Django ChatterBot Example',
    'logic_adapters': [
        #'chatterbot.logic.MathematicalEvaluation',
        #'chatterbot.logic.BestMatch',
        'example_app.custom_adapter.CustomAdapter',
       
       {'import_path':'example_app.custom_specific_response.SpecificResponseAdapter',
        #'import_path':'chatterbot.logic.SpecificResponseAdapter',
        'input_text':['how are you','whats up', 'how do you do'],
        'output_text':['I am good! What about you?','I am good! Anything I can help you today?','How do you do?']         
        },
        
        
    ],
    'statement_comparison_function' : 'example_app.custom_comparison.levenshtein_distance_custom',
    #'trainer': 'example_app.many_to_many.ManyToManyTrainer',
    'trainer': 'chatterbot.trainers.ChatterBotCorpusTrainer',
    'training_data': [
        'chatterbot.corpus.french',
        'chatterbot.corpus.english.greetings',
        'chatterbot.corpus.english.conversations',
        'example_app/chatbot_corpus/spc_corpus',
    ],
    'django_app_name': 'django_chatterbot',
    'read_only': 'True',
    'response_selection_method': 'chatterbot.response_selection.get_random_response',
    'preprocessors': [
            'example_app.custom_preprocessor.emoji_removal',
            'example_app.custom_preprocessor.substitute_abbrev'
    ]    
      
    
}

MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.auth.middleware.SessionAuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
    'django.middleware.security.SecurityMiddleware',
)

ROOT_URLCONF = 'example_app.urls'

TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [],
        'APP_DIRS': True,
        'OPTIONS': {
            'context_processors': [
                'django.template.context_processors.debug',
                'django.template.context_processors.request',
                'django.contrib.auth.context_processors.auth',
                'django.contrib.messages.context_processors.messages',
            ],
        },
    },
]

WSGI_APPLICATION = 'example_app.wsgi.application'


# Database
# https://docs.djangoproject.com/en/1.8/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': os.path.join(BASE_DIR, 'db.sqlite3'),
    }
}


# Internationalization
# https://docs.djangoproject.com/en/1.8/topics/i18n/

LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'UTC'

USE_I18N = True

USE_L10N = True

USE_TZ = True


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/1.8/howto/static-files/

STATIC_URL = '/static/'

STATICFILES_DIRS = (
    os.path.join(
        os.path.dirname(__file__),
        'static',
    ),
)

HEALTH_CHECK = ['REDIS', 'ELASTIC_SEARCH', 'POSTGRES']
