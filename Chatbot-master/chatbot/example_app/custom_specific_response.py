from __future__ import unicode_literals
from chatterbot.logic import logic_adapter
import re
from chatterbot.conversation import Statement

class SpecificResponseAdapter(logic_adapter.LogicAdapter):
    """
    Return a specific response to a specific input.
    """

    def __init__(self, **kwargs):
        super(SpecificResponseAdapter, self).__init__(**kwargs)
        self.input_list = kwargs.get('input_text')
        #self.input_text = kwargs.get('input_text')
        
        self.output_list = kwargs.get('output_text')
        #output_text = kwargs.get('output_text')
        self.response_statement = Statement(text='')

    def can_process(self, statement):
        statement.text = re.sub(r'[^a-zA-Z0-9: ]', '', statement.text).lower()
        for text in self.input_list:
            if statement.text == text:
                return True
        return False

    def process(self, statement):
        statement.text = re.sub(r'[^a-zA-Z0-9: ]', '', statement.text).lower()
        for i in range(len(self.input_list)):
            if statement.text == self.input_list[i]:        
                self.response_statement.text = self.output_list[i]
                self.response_statement.confidence = 1
            else:
                self.response_statement.confidence = 2
            

        return self.response_statement
