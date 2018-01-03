import logging
import os
import sys
from chatterbot.conversation import Statement, Response
from chatterbot.utils import print_progress_bar
from chatterbot.trainers import Trainer
import ruamel.yaml as yaml
import warnings
warnings.simplefilter('ignore', yaml.error.UnsafeLoaderWarning)

class ManyToManyTrainer(Trainer):
    def train(self, files):
        for file_count, text in enumerate(files):
            print_progress_bar("Many To Many Trainer", file_count + 1, len(files))
            data = None
            with open(text) as stream:
                try:
                    docs = yaml.load_all(stream)
                    for doc in docs:
                        in_response_list = doc["statement"]
                        response_list = doc["response"]
                        for in_response in in_response_list:
                            for response in response_list:
                                statement = self.get_or_create(response)
                                if in_response not in statement.in_response_to:
                                    statement.add_response(Response(in_response))
                                self.storage.update(statement)
                except yaml.YAMLError as exc:
                    print(exc)
