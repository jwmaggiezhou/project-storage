from chatterbot.storage import MongoDatabaseAdapter
from chatterbot.storage.mongodb import Query
from pymongo import TEXT

class MongoDatabaseIndexAdapter(MongoDatabaseAdapter):
    
    def __init__(self, **kwargs):
        super(MongoDatabaseAdapter, self).__init__(**kwargs)
        from pymongo import MongoClient

        self.database_name = self.kwargs.get(
            'database', 'chatterbot-database'
        )
        self.database_uri = self.kwargs.get(
            'database_uri', 'mongodb://localhost:27017/'
        )

        # Use the default host and port
        self.client = MongoClient(self.database_uri)

        # Specify the name of the database
        self.database = self.client[self.database_name]

        # The mongo collection of statement documents
        self.statements = self.database['statements']

        # Set a requirement for the text attribute to be unique
        self.statements.create_index([('in_response_to.text', "text")], name='search_index')
        self.base_query = Query()    
    
    
    def get_response_statements(self, input_statement):
        """
        Filter and return list of questions that contain any key words of
        the input_statement from Mongo database based on text index search on
        in_response_to.text field.
        """
        #print "using the override storage adapter"
        response_query = self.statements.find({"$text":{"$search": input_statement}})
        statement_query = []
        for item in response_query:
            for question in item['in_response_to']:
                statement_query.append(question)
        
        statement_objects = []
        for statement in list(statement_query):
            statement_objects.append(self.mongo_to_object(statement))        
        
        return statement_objects
