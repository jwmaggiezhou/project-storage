import { Component} from '@angular/core';
import { Http, Request, Response, RequestOptions, Headers, RequestMethod } from '@angular/http';
declare var $: any;

@Component({
  selector: 'app-help',
  templateUrl: './help.component.html',
  styleUrls: ['./help.component.css'],
})
export class HelpComponent {
  public question: string;
  private input: string;
  public response = '';
  private user_avatar = "https://lh6.googleusercontent.com/-lr2nyjhhjXw/AAAAAAAAAAI/AAAAAAAARmE/MdtfUmC0M4s/photo.jpg?sz=48";
  private chatbot_avatar = "https://medias2.prestastore.com/835054-pbig/chat-bot-for-social-networking.jpg";
  private url = 'http://10.200.19.171:9080/spcm/api/helpchat?q=';
  constructor(public http: Http) { }

  private ensureHttpHeaders(): RequestOptions {
    const headers = new Headers();
    headers.set('Content-Type', 'application/json');
    headers.set('Accept', 'application/json');
    const opts = new RequestOptions();
    opts.headers = headers;
    return opts;
  }

  private processAPIResult(response: Response, onSussessFunction: Function, onFailFunction?: Function) {
    const jsonData = response.json();
    if (jsonData) {
      if (jsonData.status === 'SUCCESS') {
        try {
          onSussessFunction(jsonData.data);
        } catch ( err ) {
        console.log('error in calling onSussessFunction, ' + err);
        }
      } else {
        if (onFailFunction) {
          try {
            onFailFunction(jsonData);
          } catch ( err ) {
            console.log('error in calling onFailFunction, ' + err);
          }
        } else {
          console.log(`http call returned, API status: ${jsonData.status}, code: ${jsonData.code}, message: ${jsonData.message}`);
        }
      }
    }
  }

  private doPOST(url: string, postData: any, onSussessFunction: Function, onFailFunction?: Function) {
    this.http.post(url, postData, this.ensureHttpHeaders())
    .toPromise()
    .then(response => {
      this.processAPIResult(response, onSussessFunction, onFailFunction);
    })
    .catch(response => {
      console.log("POST failed" + response);
    });
  }


  public inquiry () {
    this.input = this.question;
    this.question = '';
    this.insertChat('user', this.input);
    let post = this.url + this.input;
    this.doPOST(post, null,
      data => {
        this.response = data;
        this.insertChat('chatbot', data);
      },
      () => { console.log('Inquiry to Chatbot failed'); });
  }

  public insertChat(who, text){
    var control = "";
    var chatlog = $(".help");
    if (who == "chatbot"){

        control = '<li style="width:100%; padding-top:5px">' +
                    '<div style="display:flex;align-items:flex-start;width:90%;float:left">' +
                          '<img class="img-circle" style="width:50px;padding-top:5px;padding-right:5px" src="../../../assets/img/chatbot.png" />' +
                        '<div style="background:white;margin-top:5px;width:100%;border-radius:5px;padding:5px;display:flex;float:left;">' +
                            '<div style="width:100%;display:flex;flex-direction:column;padding:5px 6px;word-wrap:break-word;">' +
                            '<p style="margin:0px;">'+ text +'</p>' +
                        '</div>' +
                    '</div>' +
                  '</li>';
    }else{

      control = '<li style="width:100%;padding-top:5px">' +
                  '<div style="display:flex;align-items:flex-start;margin-top:5px;width:90%;float:right">' +
                    '<div style="background:#91d968;width:100%; border-radius:5px;padding:5px 6px; display:flex;flex-direction:column;word-wrap:break-word;">' +
                      '<p style="margin:4px;width:100%;">'+ text +'</p></div>' +
                    '<div style="display:flex;justify-content:center;align-items:flex-start;width:50px;float:right;">' +
                      '<i class="fa fa-user-circle-o" aria-hidden="true" style="font-size:2.5em;padding-left:5px"></i></div>'+
                  '</div>' +
                '</li>';

    }
    chatlog.append(control);
    chatlog.scrollTop(chatlog[0].scrollHeight);
  }
}
