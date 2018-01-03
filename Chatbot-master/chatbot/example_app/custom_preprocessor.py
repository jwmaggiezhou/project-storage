import re


dash_connected_emoji = ["woman-heart-woman",
                        "man-heart-man",
                        "woman-kiss-woman",
                        "man-kiss-man",
                        "man-woman-girl",
                        "man-woman-girl-boy",
                        "man-woman-boy-boy",
                        "man-woman-girl-girl",
                        "woman-woman-boy",
                        "woman-woman-girl",
                        "woman-woman-girl-boy",
                        "woman-woman-boy-boy",
                        "woman-woman-girl-girl",
                        "man-man-boy",
                        "man-man-girl",
                        "man-man-girl-boy",
                        "man-man-boy-boy",
                        "man-man-girl-girl"]

abbreviations = {"service provider connect": "SPC",
                 "sp connect": "SPC",
                 "target and allocation": "TA",
                 "payment": "PY",
                 "purchase order": "PO",
                 "amending agreement": "AA",
                 "business plan": "BP"}

special_entity = abbreviations.keys()

def emoji_removal(chatbot, statement):
    statement.text = statement.text.replace('\n', ' ').replace('\r', ' ').replace('\t', ' ')
    statement.text = statement.text.strip()
    statement.text = re.sub(' +', ' ', statement.text)    
    low = statement.text.find(":")
    while(low != -1):
        high = statement.text.find(":", low+1)
        clean = statement.text[low+1:high].replace("_","")
        if(high != -1 and (clean.isalpha() or clean in dash_connected_emoji)):
            statement.text = statement.text[:low]+statement.text[high+1:]
            low = statement.text.find(":")
            
        else:
            low = high    
    statement.text = statement.text.strip()
    #print(statement.text)
    return statement

def substitute_abbrev(chatbot, statement):
    sentence = statement.text
    tokens = tokenize_input(sentence)
    for word in tokens:
        if word in abbreviations:
            statement.text = statement.text.replace(word, abbreviations[word])
            print(statement.text)
    statement.text = statement.text.strip()
    return statement

def tokenize_input(user_in):
    user_in = user_in.lower()
    tokens = []
    for entity in special_entity:
        start = user_in.find(entity)
        if start != -1:
            tokens += user_in[:start].strip().split(' ')
            tokens.append(user_in[start:start+len(entity)])
            tokens += user_in[start+len(entity):].strip().split(' ')
    print(tokens) 
    return tokens
