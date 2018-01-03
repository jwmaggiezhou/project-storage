import os
import sys
import time
from slackclient import SlackClient
from chatterbot import ChatBot
from chatterbot.trainers import ListTrainer
from chatterbot.conversation import Statement



# starterbot's ID as an environment variable
BOT_ID = os.environ.get("BOT_ID")

# constants
AT_BOT = "<@" + BOT_ID + ">"

# instantiate Slack & Twilio clients
slack_client = SlackClient(os.environ.get('SLACK_BOT_TOKEN'))

train = False
channel_list = list()

chatbot = ChatBot("testbot",
                  read_only = True,
                  logic_adapters=[
                          {
                              "import_path": "example_app.custom_adapter.CustomAdapter",
                              "response_selection_method": "chatterbot.response_selection.get_random_response"
                          }
                      ],
                  database='chatterbot-database',
                  database_uri="mongodb://chatter:smartyBot@csczikdcapmdw02:27017",
                  storage_adapter="example_app.custom_storage.MongoDatabaseIndexAdapter",
                  statement_comparison_function = "example_app.custom_comparison.levenshtein_distance_custom",
                  preprocessors=[
                          "example_app.custom_preprocessor.emoji_removal"
                      ]
                  
                  )

chatbot.set_trainer(ListTrainer)

def handle_training(command, channel):
    """
    When the user turns on the training mode of the bot, this function processes
    question answer pairs and train the bot.
    """
    if channel.startswith('C') or channel.startswith('G'):
        slack_client.api_call("chat.postMessage", channel=channel,
                              text="Sorry I am in training mode, please train me in a direct channel. Thank you!", as_user=True)
    else:
        #print(channel_list)
        if(channel not in channel_list):
            slack_client.api_call("chat.postMessage", channel=channel,
                                  text="Please type in a new question after @Q and its corresponding response after @A. Now let's start!", as_user=True)
            channel_list.append(channel)
        else:
            try:
                p_index = command.index("@Q")
                a_index = command.index("@A")
                if(p_index >= a_index):
                    raise ValueError
                question = command[:a_index].strip("@Q").strip()
                answer = command[a_index:].strip("@A").strip()
                #print(question, answer)
                chatbot.train([question,answer])
                slack_client.api_call("chat.postMessage", channel=channel,
                                  text="Data taken!", as_user=True)            
            except ValueError:
                slack_client.api_call("chat.postMessage", channel=channel,
                                  text="Sorry I am in training mode, please type in a new question after @Q and its corresponding response after @A.", as_user=True)                 
            
        
    
    

def handle_command(command, channel):
    """
        Receives commands directed at the bot and determines if they
        are valid commands. If so, then acts on the commands. If not,
        returns back what it needs for clarification.
    """
    '''
    response = "Not sure what you mean. Use the *" + EXAMPLE_COMMAND + \
               "* command with numbers, delimited by spaces."
    if command.startswith(EXAMPLE_COMMAND):
        response = "Sure...write some more code then I can do that!"
    '''

    response = chatbot.get_response(command)
    slack_client.api_call("chat.postMessage", channel=channel,
                          text=response.text, as_user=True)


def parse_slack_output(slack_rtm_output,train):
    """
        The Slack Real Time Messaging API is an events firehose.
        this parsing function returns a message if the message is
        directed at the bot based on its ID, or if it is in a
        direct message channel, otherwise returns None
    """
    output_list = slack_rtm_output
    if output_list and len(output_list) > 0:
        for output in output_list:

            if output and 'text' in output and AT_BOT in output['text']:
                # return text after removing the @ mention, whitespace removed
                sentences = output['text'].split(AT_BOT)
                command = (sentences[0] + sentences[1]).strip()

                # print(command, \
                #      output['channel'])
                return command, \
                       output['channel']

            elif output and 'text' in output and output['channel'].startswith('D') and BOT_ID != output['user']:
                # if it is in a direct channel, and the message is not sent by
                # the bot, message is valid input

                #print (output['text'].lower(), \
                #       output['channel'])
                return output['text'], \
                        output['channel']

    return None, None


if __name__ == "__main__":
    if(len(sys.argv) > 1 and sys.argv[1] == "train"):
        train = True

    READ_WEBSOCKET_DELAY = 1 # 1 second delay between reading from firehose
    if slack_client.rtm_connect():
        print("StarterBot connected and running!")
        while True:
            command, channel = parse_slack_output(slack_client.rtm_read(),train)
            if command and channel:
                if train == True:
                    handle_training(command, channel)
                else:
                    handle_command(command, channel)
            time.sleep(READ_WEBSOCKET_DELAY)
    else:
        print("Connection failed. Invalid Slack token or bot ID?")
