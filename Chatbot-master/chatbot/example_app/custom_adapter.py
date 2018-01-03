from __future__ import unicode_literals
from chatterbot.logic import LogicAdapter
from chatterbot.conversation import Statement
import pymongo
from pymongo import MongoClient
import random



class CustomAdapter(LogicAdapter):
    """
    A logic adapter that returns a response based on known responses to
    the closest matches to the input statement.
    """
    
    def __init__(self, **kwargs):
        super(CustomAdapter, self).__init__(**kwargs)

        self.confidence_threshold = kwargs.get('threshold', 0.65)

        self.default_response_list = [Statement(text="I'm sorry, I am not able to understand your inquiry. Please try rewording your sentence and including more SPC keywords. Thanks!"),
                                 Statement(text="Your inquiry exceeds my understanding. Could you please include more SPC related keywords? Thank you!"),
                                 Statement(text="Could you please include more SPC keywords in your inquiry so that I can help you more accurately? Thank you!")]


        #self.statement_list = self.chatbot.storage.get_response_statements()
        self.response_temp_holder = Statement(text=None)
    

    def get(self, input_statement):
        """
        Takes a statement string and a list of statement strings.
        Returns the closest matching statement from the list.
        """
        statement_list = self.chatbot.storage.get_response_statements(input_statement.text)  
        print("from adapter: "+ str(len(statement_list)))
        if not statement_list:
            if self.chatbot.storage.count():
                # Use a randomly picked statement
                self.logger.info(
                    'No statements have known responses. ' +
                    'Choosing a default response to return.'
                )
                input_statement.confidence = 0
                return input_statement
            else:
                raise self.EmptyDatasetException()

        closest_match = input_statement
        closest_match.confidence = 0

        # Find the closest matching known statement
        for statement in statement_list:   
            confidence = self.compare_statements(input_statement, statement)
            if confidence > closest_match.confidence:
                statement.confidence = confidence
                closest_match = statement
        return closest_match

    def can_process(self, statement):
        """
        Check that the chatbot's storage adapter is available to the logic
        adapter and there is at least ne statement in the database.
        """
        return self.chatbot.storage.count()
        
        
        

    def process(self, input_statement):
        # Select the closest match to the input statement
        closest_match = self.get(input_statement)
        
        self.logger.info('Using "{}" as a close match to "{}"'.format(
            input_statement.text, closest_match.text
        ))
        
        if(closest_match.confidence <= self.confidence_threshold):
            response = random.choice(self.default_response_list)
            response.confidence = 0
            self.record_user_input(input_statement, closest_match)
            return response

        # Get all statements that are in response to the closest match
        response_list = self.chatbot.storage.filter(
            in_response_to__contains=closest_match.text
        )
        #print(response_list)

        if response_list:
            self.logger.info(
                'Selecting response from {} optimal responses.'.format(
                    len(response_list)
                )
            )
                
                        
            response = self.select_response(input_statement, response_list)
            response.confidence = closest_match.confidence
            self.logger.info('Response selected. Using "{}"'.format(response.text))
        else:
            response = random.choice(self.default_response_list)
                       
            self.logger.info(
                'No response to "{}" found. Selecting a random response.'.format(
                    closest_match.text
                )
            )
            response.confidence = 0
        return response
    
    
    def record_user_input(self, user_input, closest_match):
    
        client = MongoClient('mongodb://chatter:smartyBot@csczikdcapmdw02:27017/')
        db = client["chatterbot-database"]
        logs = db.logs
        exist = logs.find_one({"statement": user_input.text})
        if not exist: 
            log = {"statement": user_input.text,
                   "closest_match": closest_match.text,
                   "confidence": closest_match.confidence,
                   "confidence_threshold": self.confidence_threshold}
            result = logs.insert_one(log)  

