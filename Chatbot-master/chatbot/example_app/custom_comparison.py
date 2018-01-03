# -*- coding: utf-8 -*-
import re
import os
import chatterbot
from .custom_lazy_corpus_loader import LazyCorpusLoader
#from nltk.corpus.util import LazyCorpusLoader
from nltk.corpus.reader import *


class Comparator:

    def __call__(self, statement_a, statement_b):
        return self.compare(statement_a, statement_b)

    def compare(self, statement_a, statement_b):
        return 0

    def get_initialization_functions(self):
        """
        Return all initialization methods for the comparison algorithm.
        Initialization methods must start with 'initialize_' and
        take no parameters.
        """
        initialization_methods = [
            (
                method,
                getattr(self, method),
            ) for method in dir(self) if method.startswith('initialize_')
        ]

        return {
            key: value for (key, value) in initialization_methods
        }
    
class LevenshteinDistance_custom(Comparator):
    """
    Compare two statements based on the Levenshtein distance
    of each statement's text.

    For example, there is a 65% similarity between the statements
    "where is the post office?" and "looking for the post office"
    based on the Levenshtein distance algorithm.
    """
    '''
    def initialize_nltk_stopwords(self):
        """
        Download required NLTK corpora if they have not already been downloaded.
        """

        chatterbot.utils.nltk_download_corpus('corpora/stopwords')
    '''
    
    
    def remove_stopwords(self, tokens, language):
        """
        Takes a language (i.e. 'english'), and a set of word tokens.
        Returns the tokenized text with any stopwords removed.
        Stop words are words like "is, the, a, ..."
        """
        
        # Get the stopwords for the specified language
        stopwords = LazyCorpusLoader(
          'stopwords', WordListCorpusReader, r'(?!README|\.).*', encoding='utf8', nltk_data_subdir = os.path.abspath("example_app\chatbot_corpus"))
        #,nltk_data_subdir = os.path.abspath("example_app\chatbot_corpus")
        
        stop_words = stopwords.words(language)
        # Remove the stop words from the set of word tokens
        tokens = set(tokens) - set(stop_words)
        #print(tokens)
        return tokens    
        
    def compare(self, statement, other_statement):
        """
        Compare the two input statements.

        :return: The percent of similarity between the text of the statements.
        :rtype: float
        """
        import sys
        from nltk.tokenize import RegexpTokenizer

        # Use python-Levenshtein if available
        try:
            from Levenshtein.StringMatcher import StringMatcher as SequenceMatcher
        except ImportError:
            from difflib import SequenceMatcher

        PYTHON = sys.version_info[0]

        # Return 0 if either statement has a falsy text value
        if not statement.text or not other_statement.text:
            return 0

        # Get the lowercase version of both strings
        if PYTHON < 3:
            statement_text = unicode(statement.text.lower()) # NOQA
            #print("original: "+ statement_text)
            
            other_statement_text = unicode(other_statement.text.lower()) # NOQA
        else:
            statement_text = str(statement.text.lower())
            other_statement_text = str(other_statement.text.lower())
        
        
        #remove special characters and split into tokens
        
        tokenizer = RegexpTokenizer(r'\w+')
        statement_token = tokenizer.tokenize(statement_text)
        other_statement_token = tokenizer.tokenize(other_statement_text)        


        statement_token = self.remove_stopwords(statement_token, language='english')
        other_statement_token = self.remove_stopwords(other_statement_token, language='english')
        
        statement_text = ' '.join(statement_token)
        other_statement_text = ' '.join(other_statement_token)
        #print("processed: "+ statement_text)
        similarity = SequenceMatcher(
            None,
            statement_text,
            other_statement_text
        )

        # Calculate a decimal percent of the similarity
        percent = round(similarity.ratio(), 2)
        # print('the comparison percent is: '+ str(percent))
        return percent

levenshtein_distance_custom = LevenshteinDistance_custom()

